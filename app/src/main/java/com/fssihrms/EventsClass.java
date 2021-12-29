package com.fssihrms;

/**
 * Created by User on 6/27/2017.
 */

public class EventsClass {

    int _id;
    String EventsTitle;
    String EventsDetails;
    //String image_path;
   // String Types;
    byte[] EventsImage;


    public EventsClass(){}

    public EventsClass(int id, String eventstitle,String eventsdetails,byte[] eventsimage) {

        this._id = id;
        this.EventsTitle = eventstitle;
        this.EventsDetails=eventsdetails;
        this.EventsImage = eventsimage;
    }

    public EventsClass(String eventstitle,String eventsdetails,byte[] eventsimage)
    {
        this.EventsTitle = eventstitle;
        this.EventsDetails=eventsdetails;
        this.EventsImage = eventsimage;
    }

    public String getEventsTitle(){
        return this.EventsTitle;
    }
    // setting id
    public void setEventsTitle(String eventstitle){
        this.EventsTitle = eventstitle;
    }

    public String getEventsDetails(){
        return this.EventsDetails;
    }
    // setting id
    public void setEventsDetails(String eventsdetails){
        this.EventsDetails = eventsdetails;
    }

    public byte[] setEventsImage(byte[] eventsimage){
        return this.EventsImage=eventsimage;
    }

    public byte[] getEventsImage(){
        return this.EventsImage;
    }



}//End of EventsClass
