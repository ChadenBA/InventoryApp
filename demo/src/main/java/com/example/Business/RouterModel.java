package com.example.Business;

public class RouterModel {

    private String brand;
    private String model;
    private String Serialnbre;
    private String ipAddress; 
    private String username; 
    private String password;

    public RouterModel() {
       
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialnbre() {
        return Serialnbre;
    }

    public void setSerialnbre(String Snbr) {
        this.Serialnbre = Snbr;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return password ;
    }

    public void setPwd(String password) {
        this.password = password;
    }
      

    public RouterModel(String brand, String model, String serialnbre, String ipAddress, String username, String pwd) {
        this.brand = brand;
        this.model = model;
        this.Serialnbre = serialnbre;
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = pwd;
    }
    
}
