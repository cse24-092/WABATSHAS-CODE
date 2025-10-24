package com.elitebank;

public class Address {
    private String line1;
    private String city;
    private String countryCode;

    public Address(String line1, String city, String countryCode) {
        this.line1 = line1;
        this.city = city;
        this.countryCode = countryCode;
    }

    public String getLine1() { return line1; }
    public String getCity() { return city; }
    public String getCountryCode() { return countryCode; }

    public String toString() {
        return line1 + ", " + city + ", " + countryCode;
    }
}