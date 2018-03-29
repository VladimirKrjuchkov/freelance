package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 22.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChannelCreate implements Create{

    public ChannelCreate(){};

    private String companyId;

    private String type;

    private String channelId;

    private List<String> invites;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<String> getInvites() {
        return invites;
    }

    public void setInvites(List<String> invites) {
        this.invites = invites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelCreate that = (ChannelCreate) o;

        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (channelId != null ? !channelId.equals(that.channelId) : that.channelId != null) return false;
        return invites != null ? invites.equals(that.invites) : that.invites == null;
    }

    @Override
    public int hashCode() {
        int result = companyId != null ? companyId.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
        result = 31 * result + (invites != null ? invites.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChannelCreate{" +
                "companyId='" + companyId + '\'' +
                ", type='" + type + '\'' +
                ", channelId='" + channelId + '\'' +
                ", invites=" + invites +
                '}';
    }
}
