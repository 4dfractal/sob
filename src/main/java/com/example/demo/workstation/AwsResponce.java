package com.example.demo.workstation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
<?xml version="1.0" encoding="UTF-8"?>
<AWSIS_Response>
   <BID>BID</BID>
   <Timestamp>yyyyMMddHHmmss</Timestamp>
   <FormattedData version="1.0">
      <KVPairList>
         <KVPair>
            <key>Tag</key>
            <val>IATA Tag</val>
         </KVPair>
         <KVPair>
            <key>Passenger</key>
            <val>Passenger Name</val>
         </KVPair>
         <KVPair>
            <key>To</key>
            <val>Airport, Flight</val>
         </KVPair>
         <KVPair>
            <key>L2_Reason</key>
            <val>L2 Reason</val>
         </KVPair>
      </KVPairList>
   </FormattedData>
</AWSIS_Response>
*/

@XmlRootElement(name = "AWSIS_Response")
@XmlType(propOrder = {"bid", "timestamp", "formattedData"})
public class AwsResponce {
    private String bid;
    private Date timestamp;
    private FormattedData formattedData;

    public String getBid() {
        return bid;
    }

    @XmlElement(name = "BID")
    public void setBid(String bid) {
        this.bid = bid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @XmlElement(name = "Timestamp")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public FormattedData getFormattedData() {
        return formattedData;
    }

    @XmlElement(name = "FormattedData")
    public void setFormattedData(FormattedData formattedData) {
        this.formattedData = formattedData;
    }

    @Override
    public String toString() {
        return "AwsResponce{" +
                "bid='" + bid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", formattedData=" + formattedData +
                '}';
    }

    public static class FormattedData {


        private String version;
        private List<KVPair> kvPair;

        public String getVersion() {
            return version;
        }

        @XmlAttribute(name = "version")
        public void setVersion(String version) {
            this.version = version;
        }

        public List<KVPair> getKvPair() {
            return kvPair;
        }

        @XmlElementWrapper(name="KVPairList")
        @XmlElement(name = "KVPair")
        public void setKvPair(List<KVPair> kvPair) {
            this.kvPair = kvPair;
        }

        @Override
        public String toString() {
            return "FormattedData{" +
                    "version='" + version + '\'' +
                    ", kvPair=" + kvPair +
                    '}';
        }
    }

    public static class KVPair {

        private String key;
        private String value;

        public KVPair() {
        }

        public KVPair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        @XmlElement(name = "key")
        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        @XmlElement(name = "val")
        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "KVPair{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class DateAdapter extends XmlAdapter<String, Date> {

        private final ThreadLocal<DateFormat> dateFormat
                = new ThreadLocal<DateFormat>() {

            @Override
            protected DateFormat initialValue() {
                return new SimpleDateFormat("yyyyMMddHHmmss");
            }
        };

        @Override
        public Date unmarshal(String v) throws Exception {
            return dateFormat.get().parse(v);
        }

        @Override
        public String marshal(Date v) throws Exception {
            return dateFormat.get().format(v);
        }

    }

}

