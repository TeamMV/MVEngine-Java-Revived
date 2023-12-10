package dev.mv.engine.logic.unit;

public class TimeValue {
    private TimeUnit unit;
    private float value;

    public TimeValue(float value, TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getMS() {
        return unit.getMS(value);
    }
}
