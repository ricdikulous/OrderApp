package com.dikulous.ric.orderapp.model;

/**
 * Created by ric on 24/04/16.
 */
public class Address {
    long orderFk;
    String unitNumber;
    String streetNumber;
    String steetName;
    String suburb;
    String postcode;
    String contactNumber;

    public Address(long orderFk, String unitNumber, String streetNumber, String steetName, String suburb, String postcode, String contactNumber) {
        this.orderFk = orderFk;
        this.unitNumber = unitNumber;
        this.streetNumber = streetNumber;
        this.steetName = steetName;
        this.suburb = suburb;
        this.postcode = postcode;
        this.contactNumber = contactNumber;
    }

    public Address(long orderFk, String streetNumber, String steetName, String suburb, String postcode, String contactNumber) {
        this.streetNumber = streetNumber;
        this.steetName = steetName;
        this.postcode = postcode;
        this.suburb = suburb;
        this.contactNumber = contactNumber;
        this.orderFk = orderFk;
    }

    public long getOrderFk() {
        return orderFk;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getSteetName() {
        return steetName;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
