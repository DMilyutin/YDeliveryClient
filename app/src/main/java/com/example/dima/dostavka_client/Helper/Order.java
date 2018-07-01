package com.example.dima.dostavka_client.Helper;

public class Order {

    private String nameCustomer;
    private String phoneCustomer;
    private String addressCustomer;
    private String nameOrder;

    private String timeFiled;
    private String numberOfAddress;
    private String coastOrder;

    private String nameAddresser;
    private String phoneAddresser;
    private String addressAddresser;

    private String id;

    public Order(String nameCustomer, String telephonCustomer, String addressCustomer, String timeFiled, String addreses, String coastOrder) {
        this.nameCustomer = nameCustomer;
        this.phoneCustomer = telephonCustomer;
        this.addressCustomer = addressCustomer;
        this.timeFiled = timeFiled;
        this.numberOfAddress = addreses;
        this.coastOrder = coastOrder;
    }

    public String getId() {
        return id;
    }

    public String getNameAddresser() {
        return nameAddresser;
    }

    public void setNameAddresser(String nameAddresser) {
        this.nameAddresser = nameAddresser;
    }

    public String getPhoneAddresser() {
        return phoneAddresser;
    }

    public void setPhoneAddresser(String phoneAddresser) {
        this.phoneAddresser = phoneAddresser;
    }

    public String getAddressAddresser() {
        return addressAddresser;
    }

    public void setAddressAddresser(String addressAddresser) {
        this.addressAddresser = addressAddresser;
    }


    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public void setAddressCustomer(String addressCustomer) {
        this.addressCustomer = addressCustomer;
    }

    public String getTimeFiled() {
        return timeFiled;
    }

    public void setTimeFiled(String timeFiled) {
        this.timeFiled = timeFiled;
    }

    public String getNumberOfAddress() {
        return numberOfAddress;
    }

    public void setNumberOfAddress(String numberOfAddress) {
        this.numberOfAddress = numberOfAddress;
    }

    public String getCoastOrder() {
        return coastOrder;
    }

    public void setCoastOrder(String coastOrder) {
        this.coastOrder = coastOrder;
    }

    public String getNameOrder() { return nameOrder; }

    public void setNameOrder(String nameOrder) { this.nameOrder = nameOrder; }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public String getAddressCustomer() {
        return addressCustomer;
    }
}
