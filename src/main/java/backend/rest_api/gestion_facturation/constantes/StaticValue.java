package backend.rest_api.gestion_facturation.constantes;

import java.util.List;

public class StaticValue {

    private String key;
    private String value;

    public StaticValue() {

    }

    public StaticValue(String k, String v) {
        this.key = k;
        this.value = v;
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

    public StaticValue findByKey(String key, List<StaticValue> slov) {

        StaticValue sv = new StaticValue();

        for (StaticValue _sv : slov) {
            if (_sv.getKey().equals(key)) {
                sv = new StaticValue(_sv.getKey(), _sv.getValue());
                break;
            }
        }
        return sv;
    }

    public StaticValue findByValues(String values, List<StaticValue> slov) {

        StaticValue sv = new StaticValue();

        for (StaticValue _sv : slov) {

            if (_sv.getValue().equals(values)) {
                sv = new StaticValue(_sv.getKey(), _sv.getValue());
                break;
            }
        }
        return sv;
    }
}
