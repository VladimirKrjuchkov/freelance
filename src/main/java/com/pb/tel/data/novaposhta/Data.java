package com.pb.tel.data.novaposhta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vladimir on 13.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Data {

    public Data(){};

    @JsonProperty("Number")
    String number;

    @JsonProperty("Status")
    String status;

    @JsonProperty("CityRecipient")
    String cityRecipient;

    @JsonProperty("ScheduledDeliveryDate")
    String scheduledDeliveryDate;

    @JsonProperty("PayerType")
    String payerType;

    @JsonProperty("StatusCode")
    String statusCode;

    @JsonProperty("CitySender")
    String citySender;

    @JsonProperty("WarehouseRecipientNumber")
    String warehouseRecipientNumber;

    @JsonProperty("RecipientDateTime")
    String recipientDateTime;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCityRecipient() {
        return cityRecipient;
    }

    public void setCityRecipient(String cityRecipient) {
        this.cityRecipient = cityRecipient;
    }

    public String getScheduledDeliveryDate() {
        return scheduledDeliveryDate;
    }

    public void setScheduledDeliveryDate(String scheduledDeliveryDate) {
        this.scheduledDeliveryDate = scheduledDeliveryDate;
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCitySender() {
        return citySender;
    }

    public void setCitySender(String citySender) {
        this.citySender = citySender;
    }

    public String getWarehouseRecipientNumber() {
        return warehouseRecipientNumber;
    }

    public void setWarehouseRecipientNumber(String warehouseRecipientNumber) {
        this.warehouseRecipientNumber = warehouseRecipientNumber;
    }

    public String getRecipientDateTime() {
        return recipientDateTime;
    }

    public void setRecipientDateTime(String recipientDateTime) {
        this.recipientDateTime = recipientDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        if (number != null ? !number.equals(data.number) : data.number != null) return false;
        if (status != null ? !status.equals(data.status) : data.status != null) return false;
        if (cityRecipient != null ? !cityRecipient.equals(data.cityRecipient) : data.cityRecipient != null)
            return false;
        if (scheduledDeliveryDate != null ? !scheduledDeliveryDate.equals(data.scheduledDeliveryDate) : data.scheduledDeliveryDate != null)
            return false;
        if (payerType != null ? !payerType.equals(data.payerType) : data.payerType != null) return false;
        if (statusCode != null ? !statusCode.equals(data.statusCode) : data.statusCode != null) return false;
        if (citySender != null ? !citySender.equals(data.citySender) : data.citySender != null) return false;
        if (warehouseRecipientNumber != null ? !warehouseRecipientNumber.equals(data.warehouseRecipientNumber) : data.warehouseRecipientNumber != null)
            return false;
        return recipientDateTime != null ? recipientDateTime.equals(data.recipientDateTime) : data.recipientDateTime == null;
    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (cityRecipient != null ? cityRecipient.hashCode() : 0);
        result = 31 * result + (scheduledDeliveryDate != null ? scheduledDeliveryDate.hashCode() : 0);
        result = 31 * result + (payerType != null ? payerType.hashCode() : 0);
        result = 31 * result + (statusCode != null ? statusCode.hashCode() : 0);
        result = 31 * result + (citySender != null ? citySender.hashCode() : 0);
        result = 31 * result + (warehouseRecipientNumber != null ? warehouseRecipientNumber.hashCode() : 0);
        result = 31 * result + (recipientDateTime != null ? recipientDateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Data{" +
                "number='" + number + '\'' +
                ", status='" + status + '\'' +
                ", cityRecipient='" + cityRecipient + '\'' +
                ", scheduledDeliveryDate='" + scheduledDeliveryDate + '\'' +
                ", payerType='" + payerType + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", citySender='" + citySender + '\'' +
                ", warehouseRecipientNumber='" + warehouseRecipientNumber + '\'' +
                ", recipientDateTime='" + recipientDateTime + '\'' +
                '}';
    }
}
