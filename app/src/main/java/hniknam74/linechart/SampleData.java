package hniknam74.linechart;

public class SampleData {
    private String name;
    private int value;
    private int mappedValue;

    public SampleData(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMappedValue() {
        return mappedValue;
    }

    public void setMappedValue(int mappedValue) {
        this.mappedValue = mappedValue;
    }
}
