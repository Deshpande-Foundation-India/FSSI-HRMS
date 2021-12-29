package com.fssihrms;

/**
 * Created by User on 6/26/2017.
 */

public class BirthdaybuddyClass {

    int _id;
    String Name;

    byte[] BirthdayPic;



  public BirthdaybuddyClass(){}

    public BirthdaybuddyClass(int id, String name,byte[] birthdaypic) {
        this._id = id;
        this.Name = name;
        this.BirthdayPic = birthdaypic;
    }

    public BirthdaybuddyClass(String name,byte[] birthdaypic) {

        this.Name = name;
        this.BirthdayPic = birthdaypic;
    }

    public String getName(){
        return this.Name;
    }
    public void setName(String name){
        this.Name = name;
    }

    public byte[] getBirthdayPic(){
        return this.BirthdayPic;
    }

    public byte[] setBirthdayPic(byte[] birthdayPic){
        return this.BirthdayPic=birthdayPic;
    }



}// End of BirthdaybuddyClass
