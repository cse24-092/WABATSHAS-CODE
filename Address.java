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

    public String toString() {
        return line1 + ", " + city + ", " + countryCode;
    }
}
