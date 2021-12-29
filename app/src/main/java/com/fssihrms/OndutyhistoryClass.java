package com.fssihrms;

/**
 * Created by User on 7/1/2017.
 */

public class OndutyhistoryClass {

    int _id;
    String Location;
    String Days;
    String Status;
    String TypeofOD;
    String Description;
    String FromOD_Date;
    String ToOD_Date;

public OndutyhistoryClass(){}


    public OndutyhistoryClass(int id, String location,String days,String status,String typeofod,String description,String fromdateod,String todateod) {

        this._id = id;
        this.Location = location;
        this.Days=days;
        this.Status = status;
        this.TypeofOD=typeofod;
        this.Description = description;
        this.FromOD_Date=fromdateod;
        this.ToOD_Date= todateod;
    }

    public OndutyhistoryClass(String location,String days,String status,String typeofod,String description,String fromdateod,String todateod)
    {
        this.Location = location;
        this.Days=days;
        this.Status = status;
        this.TypeofOD=typeofod;
        this.Description =description;
        this.FromOD_Date=fromdateod;
        this.ToOD_Date= todateod;
    }

    public String getLocation(){
        return this.Location;
    }
    // setting id
    public void setLocation(String location){
        this.Location = location;
    }

    public String getDays(){
        return this.Days;
    }
    public void setDays(String days){
        this.Days = days;
    }


    public String getStatus(){
        return this.Status;
    }
    public void setStatus(String status){
        this.Status = status;
    }

    public String getTypeofOD(){
        return this.TypeofOD;
    }
    public void setTypeofOD(String typeofOD){
        this.TypeofOD = typeofOD;
    }

    public String getDescription(){
        return this.Description;
    }
    public void setDescription(String description){
        this.Description = description;
    }

    public String getfromOD_date(){
        return this.FromOD_Date;
    }
    public void setfromOD_date(String fromdateod){
        this.FromOD_Date = fromdateod;
    }


    public String gettoOD_date(){
        return this.ToOD_Date;
    }
    public void settoOD_date(String todateod){
        this.ToOD_Date = todateod;
    }






}// End of class
