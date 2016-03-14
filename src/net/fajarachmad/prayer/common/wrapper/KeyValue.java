package net.fajarachmad.prayer.common.wrapper;

/**
 * Created by user on 3/9/2016.
 */
public class KeyValue {

    private String key;
    private String value;
    private String rawData;

    public KeyValue() {}

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String key, String value, String rawData) {
        this.key = key;
        this.value = value;
        this.rawData = rawData;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
