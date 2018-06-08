package com.pb.tel.data.meest;

import com.pb.tel.adapter.DateAdapter;
import com.pb.tel.data.enums.Locale;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Created by vladimir on 07.06.18.
 */

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "items")
public class Items {

    public Items(){};

    @XmlElement(name = "ShipmentIdRef")
    private String shipmentIdRef;

    @XmlElement(name = "DocumentIdRef")
    private String documentIdRef;

    @XmlElement(name = "ShipmentNumber")
    private String shipmentNumber;

    @XmlElement(name = "ShipmentNumberSender")
    private String shipmentNumberSender;

    @XmlElement(name = "ShipmentNumberTransit")
    private String shipmentNumberTransit;

    @XmlElement(name = "Barcode")
    private String barcode;

    @XmlElement(name = "DateTimeAction")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date dateTimeAction;

    @XmlElement(name = "Country")
    private String country;

    @XmlElement(name = "Country_RU")
    private String country_RU;

    @XmlElement(name = "Country_EN")
    private String country_EN;

    @XmlElement(name = "City")
    private String city;

    @XmlElement(name = "City_RU")
    private String city_RU;

    @XmlElement(name = "City_EN")
    private String city_EN;

    @XmlElement(name = "AgentsIdRef")
    private String agentsIdRef;

    @XmlElement(name = "ActionId")
    private String actionId;

    @XmlElement(name = "AgentCode")
    private String agentCode;

    @XmlElement(name = "ActionMessages")
    private String actionMessages;

    @XmlElement(name = "DetailMessages")
    private String detailMessages;

    @XmlElement(name = "ActionMessages_RU")
    private String actionMessages_RU;

    @XmlElement(name = "DetailMessages_RU")
    private String detailMessages_RU;

    @XmlElement(name = "ActionMessages_EN")
    private String actionMessages_EN;

    @XmlElement(name = "DetailMessages_EN")
    private String detailMessages_EN;

    @XmlElement(name = "DetailPlacesAction")
    private String detailPlacesAction;

    @XmlElement(name = "CountryDel")
    private String countryDel;


    /* Методы которые не генерируются автоматически */
    public String getCountryByLocale(Locale locale){
        return (locale == Locale.RU)?country_RU:country;
    }

    public String getCityByLocale(Locale locale){
        return (locale == Locale.RU)?city_RU:city;
    }

    public String getActionByLocale(Locale locale){
        return (locale == Locale.RU)?actionMessages_RU:actionMessages;
    }
    /*************************************************/

    public String getShipmentIdRef() {
        return shipmentIdRef;
    }

    public void setShipmentIdRef(String shipmentIdRef) {
        this.shipmentIdRef = shipmentIdRef;
    }

    public String getDocumentIdRef() {
        return documentIdRef;
    }

    public void setDocumentIdRef(String documentIdRef) {
        this.documentIdRef = documentIdRef;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public String getShipmentNumberSender() {
        return shipmentNumberSender;
    }

    public void setShipmentNumberSender(String shipmentNumberSender) {
        this.shipmentNumberSender = shipmentNumberSender;
    }

    public String getShipmentNumberTransit() {
        return shipmentNumberTransit;
    }

    public void setShipmentNumberTransit(String shipmentNumberTransit) {
        this.shipmentNumberTransit = shipmentNumberTransit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getDateTimeAction() {
        return dateTimeAction;
    }

    public void setDateTimeAction(Date dateTimeAction) {
        this.dateTimeAction = dateTimeAction;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_RU() {
        return country_RU;
    }

    public void setCountry_RU(String country_RU) {
        this.country_RU = country_RU;
    }

    public String getCountry_EN() {
        return country_EN;
    }

    public void setCountry_EN(String country_EN) {
        this.country_EN = country_EN;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_RU() {
        return city_RU;
    }

    public void setCity_RU(String city_RU) {
        this.city_RU = city_RU;
    }

    public String getCity_EN() {
        return city_EN;
    }

    public void setCity_EN(String city_EN) {
        this.city_EN = city_EN;
    }

    public String getAgentsIdRef() {
        return agentsIdRef;
    }

    public void setAgentsIdRef(String agentsIdRef) {
        this.agentsIdRef = agentsIdRef;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getActionMessages() {
        return actionMessages;
    }

    public void setActionMessages(String actionMessages) {
        this.actionMessages = actionMessages;
    }

    public String getDetailMessages() {
        return detailMessages;
    }

    public void setDetailMessages(String detailMessages) {
        this.detailMessages = detailMessages;
    }

    public String getActionMessages_RU() {
        return actionMessages_RU;
    }

    public void setActionMessages_RU(String actionMessages_RU) {
        this.actionMessages_RU = actionMessages_RU;
    }

    public String getDetailMessages_RU() {
        return detailMessages_RU;
    }

    public void setDetailMessages_RU(String detailMessages_RU) {
        this.detailMessages_RU = detailMessages_RU;
    }

    public String getActionMessages_EN() {
        return actionMessages_EN;
    }

    public void setActionMessages_EN(String actionMessages_EN) {
        this.actionMessages_EN = actionMessages_EN;
    }

    public String getDetailMessages_EN() {
        return detailMessages_EN;
    }

    public void setDetailMessages_EN(String detailMessages_EN) {
        this.detailMessages_EN = detailMessages_EN;
    }

    public String getDetailPlacesAction() {
        return detailPlacesAction;
    }

    public void setDetailPlacesAction(String detailPlacesAction) {
        this.detailPlacesAction = detailPlacesAction;
    }

    public String getCountryDel() {
        return countryDel;
    }

    public void setCountryDel(String countryDel) {
        this.countryDel = countryDel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Items items = (Items) o;

        if (shipmentIdRef != null ? !shipmentIdRef.equals(items.shipmentIdRef) : items.shipmentIdRef != null)
            return false;
        if (documentIdRef != null ? !documentIdRef.equals(items.documentIdRef) : items.documentIdRef != null)
            return false;
        if (shipmentNumber != null ? !shipmentNumber.equals(items.shipmentNumber) : items.shipmentNumber != null)
            return false;
        if (shipmentNumberSender != null ? !shipmentNumberSender.equals(items.shipmentNumberSender) : items.shipmentNumberSender != null)
            return false;
        if (shipmentNumberTransit != null ? !shipmentNumberTransit.equals(items.shipmentNumberTransit) : items.shipmentNumberTransit != null)
            return false;
        if (barcode != null ? !barcode.equals(items.barcode) : items.barcode != null) return false;
        if (dateTimeAction != null ? !dateTimeAction.equals(items.dateTimeAction) : items.dateTimeAction != null)
            return false;
        if (country != null ? !country.equals(items.country) : items.country != null) return false;
        if (country_RU != null ? !country_RU.equals(items.country_RU) : items.country_RU != null) return false;
        if (country_EN != null ? !country_EN.equals(items.country_EN) : items.country_EN != null) return false;
        if (city != null ? !city.equals(items.city) : items.city != null) return false;
        if (city_RU != null ? !city_RU.equals(items.city_RU) : items.city_RU != null) return false;
        if (city_EN != null ? !city_EN.equals(items.city_EN) : items.city_EN != null) return false;
        if (agentsIdRef != null ? !agentsIdRef.equals(items.agentsIdRef) : items.agentsIdRef != null) return false;
        if (actionId != null ? !actionId.equals(items.actionId) : items.actionId != null) return false;
        if (agentCode != null ? !agentCode.equals(items.agentCode) : items.agentCode != null) return false;
        if (actionMessages != null ? !actionMessages.equals(items.actionMessages) : items.actionMessages != null)
            return false;
        if (detailMessages != null ? !detailMessages.equals(items.detailMessages) : items.detailMessages != null)
            return false;
        if (actionMessages_RU != null ? !actionMessages_RU.equals(items.actionMessages_RU) : items.actionMessages_RU != null)
            return false;
        if (detailMessages_RU != null ? !detailMessages_RU.equals(items.detailMessages_RU) : items.detailMessages_RU != null)
            return false;
        if (actionMessages_EN != null ? !actionMessages_EN.equals(items.actionMessages_EN) : items.actionMessages_EN != null)
            return false;
        if (detailMessages_EN != null ? !detailMessages_EN.equals(items.detailMessages_EN) : items.detailMessages_EN != null)
            return false;
        if (detailPlacesAction != null ? !detailPlacesAction.equals(items.detailPlacesAction) : items.detailPlacesAction != null)
            return false;
        return countryDel != null ? countryDel.equals(items.countryDel) : items.countryDel == null;
    }

    @Override
    public int hashCode() {
        int result = shipmentIdRef != null ? shipmentIdRef.hashCode() : 0;
        result = 31 * result + (documentIdRef != null ? documentIdRef.hashCode() : 0);
        result = 31 * result + (shipmentNumber != null ? shipmentNumber.hashCode() : 0);
        result = 31 * result + (shipmentNumberSender != null ? shipmentNumberSender.hashCode() : 0);
        result = 31 * result + (shipmentNumberTransit != null ? shipmentNumberTransit.hashCode() : 0);
        result = 31 * result + (barcode != null ? barcode.hashCode() : 0);
        result = 31 * result + (dateTimeAction != null ? dateTimeAction.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (country_RU != null ? country_RU.hashCode() : 0);
        result = 31 * result + (country_EN != null ? country_EN.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (city_RU != null ? city_RU.hashCode() : 0);
        result = 31 * result + (city_EN != null ? city_EN.hashCode() : 0);
        result = 31 * result + (agentsIdRef != null ? agentsIdRef.hashCode() : 0);
        result = 31 * result + (actionId != null ? actionId.hashCode() : 0);
        result = 31 * result + (agentCode != null ? agentCode.hashCode() : 0);
        result = 31 * result + (actionMessages != null ? actionMessages.hashCode() : 0);
        result = 31 * result + (detailMessages != null ? detailMessages.hashCode() : 0);
        result = 31 * result + (actionMessages_RU != null ? actionMessages_RU.hashCode() : 0);
        result = 31 * result + (detailMessages_RU != null ? detailMessages_RU.hashCode() : 0);
        result = 31 * result + (actionMessages_EN != null ? actionMessages_EN.hashCode() : 0);
        result = 31 * result + (detailMessages_EN != null ? detailMessages_EN.hashCode() : 0);
        result = 31 * result + (detailPlacesAction != null ? detailPlacesAction.hashCode() : 0);
        result = 31 * result + (countryDel != null ? countryDel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Items{" +
                "shipmentIdRef='" + shipmentIdRef + '\'' +
                ", documentIdRef='" + documentIdRef + '\'' +
                ", shipmentNumber='" + shipmentNumber + '\'' +
                ", shipmentNumberSender='" + shipmentNumberSender + '\'' +
                ", shipmentNumberTransit='" + shipmentNumberTransit + '\'' +
                ", barcode='" + barcode + '\'' +
                ", dateTimeAction=" + dateTimeAction +
                ", country='" + country + '\'' +
                ", country_RU='" + country_RU + '\'' +
                ", country_EN='" + country_EN + '\'' +
                ", city='" + city + '\'' +
                ", city_RU='" + city_RU + '\'' +
                ", city_EN='" + city_EN + '\'' +
                ", agentsIdRef='" + agentsIdRef + '\'' +
                ", actionId='" + actionId + '\'' +
                ", agentCode='" + agentCode + '\'' +
                ", actionMessages='" + actionMessages + '\'' +
                ", detailMessages='" + detailMessages + '\'' +
                ", actionMessages_RU='" + actionMessages_RU + '\'' +
                ", detailMessages_RU='" + detailMessages_RU + '\'' +
                ", actionMessages_EN='" + actionMessages_EN + '\'' +
                ", detailMessages_EN='" + detailMessages_EN + '\'' +
                ", detailPlacesAction='" + detailPlacesAction + '\'' +
                ", countryDel='" + countryDel + '\'' +
                '}';
    }
}
