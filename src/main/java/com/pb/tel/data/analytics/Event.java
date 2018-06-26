package com.pb.tel.data.analytics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.tel.data.enums.Action;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by vladimir on 11.06.18.
 */

@Entity(name = "BotEvents")
@NamedQueries({@NamedQuery(name = "Event.findByDay", query = "SELECT w FROM BotEvents w  where w.date between :dateFrom and :dateTo")})


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = "BotEvents")
public class Event {

    public Event(){}

    @Id
    @Column(name = "eventId", nullable=false)
    private String eventId;

    @Column(name = "extId", nullable=false)
    private String extId;

    @Column(name = "channel", nullable=false)
    @JsonProperty("Chanel")
    private String channel;

    @Column(name = "action", nullable=false)
    @JsonProperty("Action")
    private String action;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable=false)
    private Date date = new Date();

    @Column(name = "description", nullable=true)
    @JsonProperty("TTN")
    private String description;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (eventId != null ? !eventId.equals(event.eventId) : event.eventId != null) return false;
        if (extId != null ? !extId.equals(event.extId) : event.extId != null) return false;
        if (channel != null ? !channel.equals(event.channel) : event.channel != null) return false;
        if (action != null ? !action.equals(event.action) : event.action != null) return false;
        if (date != null ? !date.equals(event.date) : event.date != null) return false;
        return description != null ? description.equals(event.description) : event.description == null;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (extId != null ? extId.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", extId='" + extId + '\'' +
                ", channel='" + channel + '\'' +
                ", action='" + action + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
