package com.example.demo.bhs;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
<?xml version = '1.0'?>
 <BaggageData>
   <Header>
      <MessageTime>2022-01-27T02:13:17.966Z</MessageTime>
      <Source>
         <ProcessID>BSIS</ProcessID>
      </Source>
      <Target>
         <ProcessID>BDM</ProcessID>
      </Target>
   </Header>
   <Data Action="CHG">
      <OperationalOutboundFlightInformation Airline="SU" FlightNumber="0035" Date="2022-01-27" AirportCode="SVO" ClassOfTravel="Y"/>
      <BaggageTagNumber>8555160957</BaggageTagNumber>
      <PassengerName Surname="IVANOV" Name="IVAN"/>
      <InternalAirlineData>.02LED/GTE/ZTC</InternalAirlineData>
      <BaggageReconciliationData AuthorityToLoad="Y" SeatNumber="24B" PassengerStatus="B" SequenceNumber="067" PassengerProfileStatus="N" BaggageTagStatus="A"/>
      <VersionAndSupplementaryData DataDictionaryVersionNumber="1" BaggageSourceIndicator="L" AirportCode="LED"/>
      <BaggageSecurityScreening BaggageStatus="CLR" SecurityMethod="XRAY"/>
      <FrequentTravellerNumber FrequentTravellerIDNumber="SU184230653"/>
      <SecuritySuspectPassenger>Accept</SecuritySuspectPassenger>
      <CustomsSuspectPassenger>Accept</CustomsSuspectPassenger>
      <CustomsClear>Accept</CustomsClear>
      <TransferType>NotTransfer</TransferType>
      <Onward>N</Onward>
      <Onward>N</Onward>
      <Onward>N</Onward>
   </Data>
</BaggageData>
 */


@XmlRootElement(name = "BaggageData")
@XmlType(propOrder = {"bhsRespHeader", "bhsRespData"})
public class BhsResponce {
    private BhsRespHeader bhsRespHeader;
    private BhsRespData bhsRespData;

    public BhsRespHeader getBhsRespHeader() {
        return bhsRespHeader;
    }

    @XmlElement(name = "Header")
    public void setBhsRespHeader(BhsRespHeader bhsRespHeader) {
        this.bhsRespHeader = bhsRespHeader;
    }

    public BhsRespData getBhsRespData() {
        return bhsRespData;
    }

    @XmlElement(name = "Data")
    public void setBhsRespData(BhsRespData bhsRespData) {
        this.bhsRespData = bhsRespData;
    }

    @Override
    public String toString() {
        return "BhsResponce{" +
                "bhsRespHeader=" + bhsRespHeader +
                ", bhsRespData=" + bhsRespData +
                '}';
    }

    public static class DateAdapter extends XmlAdapter<String, Date> {

        private final ThreadLocal<DateFormat> dateFormat
                = new ThreadLocal<DateFormat>() {

            @Override
            protected DateFormat initialValue() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                return format;
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

    public static class BhsRespHeader {

        private Date messageTime;

        public Date getMessageTime() {
            return messageTime;
        }

        @XmlElement(name = "MessageTime")
        @XmlJavaTypeAdapter(BhsResponce.DateAdapter.class)
        public void setMessageTime(Date messageTime) {
            this.messageTime = messageTime;
        }

        @Override
        public String toString() {
            return "BhsRespHeader{" +
                    "messageTime=" + messageTime +
                    '}';
        }
    }

    @XmlType(propOrder = {"action", "flightInfo", "baggageTagNumber", "passengerName"})
    public static class BhsRespData {

        private String action;

        private String baggageTagNumber;

        private FlightInfo flightInfo;

        private PassengerName passengerName;

        public String getAction() {
            return action;
        }

        @XmlAttribute(name = "Action")
        public void setAction(String action) {
            this.action = action;
        }

        public String getBaggageTagNumber() {
            return baggageTagNumber;
        }

        @XmlElement(name = "BaggageTagNumber")
        public void setBaggageTagNumber(String baggageTagNumber) {
            this.baggageTagNumber = baggageTagNumber;
        }

        public FlightInfo getFlightInfo() {
            return flightInfo;
        }

        @XmlElement(name = "OperationalOutboundFlightInformation")
        public void setFlightInfo(FlightInfo flightInfo) {
            this.flightInfo = flightInfo;
        }

        public PassengerName getPassengerName() {
            return passengerName;
        }

        @XmlElement(name = "PassengerName")
        public void setPassengerName(PassengerName passengerName) {
            this.passengerName = passengerName;
        }

        @Override
        public String toString() {
            return "BhsRespData{" +
                    "action='" + action + '\'' +
                    ", baggageTagNumber='" + baggageTagNumber + '\'' +
                    ", flightInfo=" + flightInfo +
                    ", passengerName=" + passengerName +
                    '}';
        }
    }

    @XmlType(propOrder = {"surname", "name"})
    public static class PassengerName {

        private String surname;
        private String name;

        public String getName() {
            return name;
        }

        @XmlAttribute(name = "Name")
        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        @XmlAttribute(name = "Surname")
        public void setSurname(String surname) {
            this.surname = surname;
        }

        @Override
        public String toString() {
            return "PassengerName{" +
                    "surname='" + surname + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @XmlType(propOrder = {"airline", "flightNumber", "date", "airportCode", "classOfTravel"})
    public static class FlightInfo {

        private String airline;
        private String flightNumber;
        private String date;
        private String airportCode;
        private String classOfTravel;

        public String getAirline() {
            return airline;
        }

        @XmlAttribute(name = "Airline")
        public void setAirline(String airline) {
            this.airline = airline;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        @XmlAttribute(name = "FlightNumber")
        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }

        public String getDate() {
            return date;
        }

        @XmlAttribute(name = "Date")
        public void setDate(String date) {
            this.date = date;
        }

        public String getAirportCode() {
            return airportCode;
        }

        @XmlAttribute(name = "AirportCode")
        public void setAirportCode(String airportCode) {
            this.airportCode = airportCode;
        }

        public String getClassOfTravel() {
            return classOfTravel;
        }

        @XmlAttribute(name = "ClassOfTravel")
        public void setClassOfTravel(String classOfTravel) {
            this.classOfTravel = classOfTravel;
        }

        @Override
        public String toString() {
            return "FlightInfo{" +
                    "airline='" + airline + '\'' +
                    ", flightNumber='" + flightNumber + '\'' +
                    ", date='" + date + '\'' +
                    ", airportCode='" + airportCode + '\'' +
                    ", classOfTravel='" + classOfTravel + '\'' +
                    '}';
        }
    }

}
