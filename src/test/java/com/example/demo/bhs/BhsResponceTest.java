package com.example.demo.bhs;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * AwsResponceTest.
 */
class BhsResponceTest {

    private final String bhsRespXml =
            "<?xml version = '1.0'?>\n" +
                    " <BaggageData>\n" +
                    "   <Header>\n" +
                    "      <MessageTime>2022-01-27T02:13:17.966Z</MessageTime>\n" +
                    "      <Source>\n" +
                    "         <ProcessID>BSIS</ProcessID>\n" +
                    "      </Source>\n" +
                    "      <Target>\n" +
                    "         <ProcessID>BDM</ProcessID>\n" +
                    "      </Target>\n" +
                    "   </Header>\n" +
                    "   <Data Action=\"CHG\">\n" +
                    "      <OperationalOutboundFlightInformation Airline=\"SU\" FlightNumber=\"0035\" Date=\"2022-01-27\" AirportCode=\"SVO\" ClassOfTravel=\"Y\"/>\n" +
                    "      <BaggageTagNumber>8555160957</BaggageTagNumber>\n" +
                    "      <PassengerName Surname=\"IVANOV\" Name=\"IVAN\"/>\n" +
                    "      <InternalAirlineData>.02LED/GTE/ZTC</InternalAirlineData>\n" +
                    "      <BaggageReconciliationData AuthorityToLoad=\"Y\" SeatNumber=\"24B\" PassengerStatus=\"B\" SequenceNumber=\"067\" PassengerProfileStatus=\"N\" BaggageTagStatus=\"A\"/>\n" +
                    "      <VersionAndSupplementaryData DataDictionaryVersionNumber=\"1\" BaggageSourceIndicator=\"L\" AirportCode=\"LED\"/>\n" +
                    "      <BaggageSecurityScreening BaggageStatus=\"CLR\" SecurityMethod=\"XRAY\"/>\n" +
                    "      <FrequentTravellerNumber FrequentTravellerIDNumber=\"SU184230653\"/>\n" +
                    "      <SecuritySuspectPassenger>Accept</SecuritySuspectPassenger>\n" +
                    "      <CustomsSuspectPassenger>Accept</CustomsSuspectPassenger>\n" +
                    "      <CustomsClear>Accept</CustomsClear>\n" +
                    "      <TransferType>NotTransfer</TransferType>\n" +
                    "      <Onward>N</Onward>\n" +
                    "      <Onward>N</Onward>\n" +
                    "      <Onward>N</Onward>\n" +
                    "   </Data>\n" +
                    "</BaggageData>";

    private final String bhsRespXmlResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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


    @Test
    void unmarshalTest() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(BhsResponce.class);
        StringReader reader = new StringReader(bhsRespXml);
        BhsResponce bhsRespObject = (BhsResponce) context.createUnmarshaller().unmarshal(reader);
        System.out.println(bhsRespObject);
        Assert.assertNotNull(bhsRespObject);
        Assert.assertEquals("SU", bhsRespObject.getBhsRespData().getFlightInfo().getAirline());
    }

    @Test
    void marshalTest() throws JAXBException {

        BhsResponce.BhsRespHeader header = new BhsResponce.BhsRespHeader();
        BhsResponce.DateAdapter adapter = new BhsResponce.DateAdapter();
        try {
            header.setMessageTime(adapter.unmarshal("2022-01-27T02:13:17.966Z"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        BhsResponce.BhsRespData data = new BhsResponce.BhsRespData();
        data.setAction("123");
        data.setBaggageTagNumber("1234567890");

        BhsResponce bhsResponce = new BhsResponce();
        bhsResponce.setBhsRespData(data);
        bhsResponce.setBhsRespHeader(header);

        BhsResponce.PassengerName pName = new BhsResponce.PassengerName();
        pName.setName("Иван");
        pName.setSurname("Иванов");
        data.setPassengerName(pName);

        BhsResponce.FlightInfo fInfo = new BhsResponce.FlightInfo();
        fInfo.setAirline("SU");
        fInfo.setFlightNumber("0035");
        fInfo.setDate("2022-01-27");
        fInfo.setClassOfTravel("Y");
        fInfo.setAirportCode("SVO");
        data.setFlightInfo(fInfo);

        System.out.println(data.toString());

        StringWriter sw = new StringWriter();

        JAXBContext context = JAXBContext.newInstance(BhsResponce.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(bhsResponce, sw);

        System.out.println(sw.toString());

        Assert.assertNotNull(sw);
        Assert.assertEquals(bhsRespXmlResult, sw.toString().trim());
    }

}