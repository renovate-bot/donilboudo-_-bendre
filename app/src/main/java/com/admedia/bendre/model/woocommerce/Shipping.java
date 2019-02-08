package com.admedia.bendre.model.woocommerce;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Shipping implements Serializable {
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("company")
    private String company;

    @JsonProperty("address_1")
    private String address1;

    @JsonProperty("address_2")
    private String address2;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("postcode")
    private String postCode;

    @JsonProperty("country")
    private String country;

    public Shipping() {
        this.firstName = "";
        this.lastName = "";
        this.company = "";
        this.address1 = "";
        this.address2 = "";
        this.city = "";
        this.state = "";
        this.postCode = "";
        this.country = "";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
