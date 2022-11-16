package com.example.rfid_mobile;

public class RentalClass {
    public String name;
    public String startDate;
    public String endDate;
    public String objectID;

    RentalClass(String name, String startDate, String endDate, String objectID){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.objectID = objectID;
    }


}
