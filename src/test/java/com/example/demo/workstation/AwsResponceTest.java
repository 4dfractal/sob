package com.example.demo.workstation;

import com.example.demo.workstation.AwsResponce;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * AwsResponceTest.
 */
class AwsResponceTest {

    @Test
    void unmarshalTest() throws JAXBException {
        String responceXml = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
                "<AWSIS_Response>\n" +
                "<BID>BID</BID>\n" +
                "<Timestamp>yyyyMMddHHmmss</Timestamp>\n" +
                "<FormattedData version=\"1.0\">\n" +
                "<KVPairList>\n" +
                "<KVPair>\n" +
                "<key>Tag</key>\n" +
                "<val>IATA Tag</val>\n" +
                "</KVPair>\n" +
                "<KVPair>\n" +
                "<key>Passenger</key>\n" +
                "<val>Passenger Name</val>\n" +
                "</KVPair>\n" +
                "<KVPair>\n" +
                "<key>To</key>\n" +
                "<val>Airport, Flight</val>\n" +
                "</KVPair>\n" +
                "<KVPair>\n" +
                "<key>L2_Reason</key>\n" +
                "<val>L2 Reason</val>\n" +
                "</KVPair>\n" +
                "</KVPairList>\n" +
                "</FormattedData>\n" +
                "</AWSIS_Response>";
        JAXBContext context = JAXBContext.newInstance(AwsResponce.class);
        StringReader reader = new StringReader(responceXml);
        AwsResponce queryObject = (AwsResponce) context.createUnmarshaller().unmarshal(reader);
        System.out.println(queryObject);
        Assert.assertNotNull(queryObject);
        Assert.assertEquals("BID", queryObject.getBid());
    }

    @Test
    void marshalTest() throws JAXBException {
        String responceXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<AWSIS_Response>\n" +
                "    <BID>BID</BID>\n" +
                "    <Timestamp>20220125102030</Timestamp>\n" +
                "    <FormattedData version=\"1.0\">\n" +
                "        <KVPairList>\n" +
                "            <KVPair>\n" +
                "                <key>Tag</key>\n" +
                "                <val>IATA Tag</val>\n" +
                "            </KVPair>\n" +
                "            <KVPair>\n" +
                "                <key>Passenger</key>\n" +
                "                <val>Passenger Name</val>\n" +
                "            </KVPair>\n" +
                "            <KVPair>\n" +
                "                <key>To</key>\n" +
                "                <val>Airport, Flight</val>\n" +
                "            </KVPair>\n" +
                "            <KVPair>\n" +
                "                <key>L2_Reason</key>\n" +
                "                <val>L2 Reason</val>\n" +
                "            </KVPair>\n" +
                "        </KVPairList>\n" +
                "    </FormattedData>\n" +
                "</AWSIS_Response>";

        AwsResponce awsResponce = new AwsResponce();
        awsResponce.setBid("BID");
        AwsResponce.DateAdapter adapter = new AwsResponce.DateAdapter();
        try {
            awsResponce.setTimestamp(adapter.unmarshal("20220125102030"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        AwsResponce.FormattedData fData = new AwsResponce.FormattedData();
        awsResponce.setFormattedData(fData);
        fData.setVersion("1.0");
        ArrayList kvPairList = new ArrayList();
        fData.setKvPair(kvPairList);
        kvPairList.add(new AwsResponce.KVPair("Tag", "IATA Tag"));
        kvPairList.add(new AwsResponce.KVPair(        "Passenger", "Passenger Name"));
        kvPairList.add(new AwsResponce.KVPair(        "To", "Airport, Flight"));
        kvPairList.add(new AwsResponce.KVPair(        "L2_Reason", "L2 Reason"));

        StringWriter sw = new StringWriter();

        JAXBContext context = JAXBContext.newInstance(AwsResponce.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(awsResponce, sw);

        System.out.println(sw.toString());

        Assert.assertNotNull(sw);
        Assert.assertEquals(responceXml, sw.toString().trim());
    }

}