package com.example.rfid_mobile;

public class ObjectClass {
    public String id;
    public String name;
    public String description;
    public boolean status;
    public String category;

    ObjectClass(String id, String name, String description, boolean status, String category){
        this.name = name;
        this.id=id;
        this.description=description;
        this.status=status;
        this.category=category;
    }


}
