package com.example.demo.service;

import com.example.demo.bhs.BhsResponce;
import com.example.demo.db.bhs.BhsRepository;
import com.example.demo.db.infoserv.QueryRepository;
import com.example.demo.db.infoserv.Query;
import com.example.demo.workstation.AwsQuery;
import com.example.demo.workstation.AwsResponce;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

@Builder
@RequiredArgsConstructor
@Service
//@Slf4j
//@Transactional(readOnly = true)
public class ProcessMessageService {
    private final Logger log = LogManager.getLogger(ProcessMessageService.class);

    private final QueryRepository queryRepository;
    private final BhsService bhsService;

    public Message<String> processQuery(byte[] payload, MessageHeaders messageHeaders) {
        String connectionId = (String) messageHeaders.get(IpHeaders.CONNECTION_ID);
        log.info("MessageHeaders - CONNECTION_ID: {}", connectionId);

        String messageStr = new String(payload);
        AwsQuery query = unmarshallQuery(messageStr);
        log.info("AwsQuery: {}", query);

        //Request Bhs
        BhsResponce bhsData = bhsService.getBhsResponce("118218114");//query.getBid());
        log.info("bhsData: {}", bhsData);

        //Compose responce object
        AwsResponce awsResponce = new AwsResponce();
        awsResponce.setBid(query.getBid());
        awsResponce.setTimestamp(new Date());
        AwsResponce.FormattedData fData = new AwsResponce.FormattedData();
        awsResponce.setFormattedData(fData);
        fData.setVersion("1.0");
        ArrayList kvPairList = new ArrayList();
        fData.setKvPair(kvPairList);
        kvPairList.add(new AwsResponce.KVPair("Tag", "IATA Tag"));
        if (bhsData != null) {
            kvPairList.add(new AwsResponce.KVPair(
                    "Passenger",
                    bhsData.getBhsRespData().getPassengerName().getName()
                            + " " + bhsData.getBhsRespData().getPassengerName().getSurname()));
            kvPairList.add(new AwsResponce.KVPair(
                    "To",
                    bhsData.getBhsRespData().getFlightInfo().getAirportCode()
                            + " "
                            + bhsData.getBhsRespData().getFlightInfo().getFlightNumber()
            ));
            kvPairList.add(new AwsResponce.KVPair("L2_Reason", "L2 Reason"));
        }

        //Marshal responce object
        String responce = marshallResponce(awsResponce);
        log.info("Responce: {}", responce);

        Query request = new Query();
        request.setQuery(messageStr);
        request.setResponce(responce);
        queryRepository.save(request);

        log.info("RecordCount: {}", queryRepository.count());

        return MessageBuilder.withPayload(responce)
                      .setHeader(IpHeaders.CONNECTION_ID, connectionId)
                      .build();
        //return new GenericMessage<>(marshallResponce(awsResponce));

    }

    public AwsQuery unmarshallQuery(String query) {
        try {
            JAXBContext context = JAXBContext.newInstance(AwsQuery.class);
            StringReader reader = new StringReader(query);
            return (AwsQuery) context.createUnmarshaller()
                                     .unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String marshallResponce(AwsResponce awsResponce) {
        try {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(AwsResponce.class);
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.marshal(awsResponce, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }


}
