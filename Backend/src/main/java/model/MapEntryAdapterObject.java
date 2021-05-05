package model;

import java.io.Serializable;

public class MapEntryAdapterObject implements Serializable {

    String key;
    Object value;

    public MapEntryAdapterObject() {
    }

    public MapEntryAdapterObject(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
