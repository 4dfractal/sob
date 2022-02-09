package com.example.demo.workstation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
<?xml version="1.0" encoding="UTF-8"?>
<AWS_Query>
   <Workstation>workstation name</Workstation>
   <Operator>operator name</Operator>
   <BID>BID</BID>
   <Timestamp>yyyyMMddHHmmss</Timestamp>
</AWS_Query>
 */


@XmlRootElement(name = "AWS_Query")
@XmlType(propOrder = {"workstation", "operator", "bid", "timestamp"})
public class AwsQuery {
    private String workstation;
    private String operator;
    private String bid;
    private Date timestamp;

    public String getWorkstation() {
        return workstation;
    }

    @XmlElement(name = "Workstation")
    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getOperator() {
        return operator;
    }

    @XmlElement(name = "Operator")
    public void setOperator(String operator) {
        this.operator = operator;
    }

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
    @XmlJavaTypeAdapter(AwsResponce.DateAdapter.class)
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AwsQuery{" +
                "workstation='" + workstation + '\'' +
                ", operator='" + operator + '\'' +
                ", bid='" + bid + '\'' +
                ", Timestamp='" + timestamp + '\'' +
                '}';
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
