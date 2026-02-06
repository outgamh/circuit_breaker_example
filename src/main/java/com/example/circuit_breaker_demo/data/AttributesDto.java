package com.example.circuit_breaker_demo.data;

public class AttributesDto {
    private String culture;
    private String role;
    private Integer office;
    private Integer filial;

    public AttributesDto(){}

    public AttributesDto(String culture, String role, Integer office, Integer filial){
        this.culture = culture;
        this.role = role;
        this.office= office;
        this. filial = filial;
    }

    public String getRole() {
        return role;
    }

    public String getCulture() {
        return culture;
    }

    public Integer getOffice() {
        return office;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setOffice(Integer office) {
        this.office = office;
    }

    public void setFilial(Integer filial) {
        this.filial = filial;
    }

    public Integer getFilial() {
        return filial;
    }
}
