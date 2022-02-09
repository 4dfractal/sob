package com.example.demo.service;

import com.example.demo.bhs.BhsResponce;
import com.example.demo.db.bhs.BhsRepository;
import com.example.demo.db.bhs.BsmDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;

@Builder
@RequiredArgsConstructor
@Service
public class BhsService {

    private final BhsRepository bhsRepository;

    BhsResponce getBhsResponce(String bId){

        String baggageXml = getBaggageDataXml(bId);
        return unmarshallBaggageDataXml(baggageXml);

    }

    private String getBaggageDataXml(String bId) {
/*        final String bhsRespXmlResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<BaggageData>\n" +
                "    <Header>\n" +
                "        <MessageTime>2022-01-27T02:13:17.966Z</MessageTime>\n" +
                "    </Header>\n" +
                "    <Data Action=\"123\">\n" +
                "        <OperationalOutboundFlightInformation Airline=\"SU\" FlightNumber=\"0035\" Date=\"2022-01-27\" AirportCode=\"SVO\" ClassOfTravel=\"Y\"/>\n" +
                "        <BaggageTagNumber>1234567890</BaggageTagNumber>\n" +
                "        <PassengerName Surname=\"Иванов\" Name=\"Иван\"/>\n" +
                "    </Data>\n" +
                "</BaggageData>";
        return bhsRespXmlResult;*/
        BsmDto dto =bhsRepository.findBsmById(bId);
        return dto != null ? dto.getBsm() : null;
    }

    public BhsResponce unmarshallBaggageDataXml(String query) {
        if (query != null) {
            try {
                JAXBContext context = JAXBContext.newInstance(BhsResponce.class);
                StringReader reader = new StringReader(query);
                return (BhsResponce) context.createUnmarshaller()
                                            .unmarshal(reader);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
