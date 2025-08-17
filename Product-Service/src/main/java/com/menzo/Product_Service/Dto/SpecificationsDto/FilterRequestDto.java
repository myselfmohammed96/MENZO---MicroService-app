package com.menzo.Product_Service.Dto.SpecificationsDto;

public class FilterRequestDto {
    private String filterType;
    private String values;

    public FilterRequestDto() {}

    public FilterRequestDto(String filterType, String values) {
        this.filterType = filterType;
        this.values = values;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String toString() {
        return "FilterRequestDto:\nfilterType: " + filterType + "\nvalues: " + values + "\n";
    }
}
