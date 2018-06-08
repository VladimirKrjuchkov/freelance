package com.pb.tel.data.meest;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by vladimir on 07.06.18.
 */

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result_table")
public class ResultTable {

    public ResultTable(){}

    @XmlElement
    private List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultTable that = (ResultTable) o;

        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ResultTable{" +
                "items=" + items +
                '}';
    }
}
