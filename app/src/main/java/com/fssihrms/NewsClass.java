package com.fssihrms;

/**
 * Created by User on 6/23/2017.
 */

public class NewsClass {

    int _id;
    String NewsTitle;
   String NewsDetails;
    //String image_path;
    String Types;
    byte[] NewsImage;


    public NewsClass(){}


    public NewsClass(int id, String newstitle,String newsdetails ,String types , byte[] newsimage) {
        this._id = id;
        this.NewsTitle = newstitle;
        this.NewsDetails=newsdetails;
        this.Types = types;
        this.NewsImage = newsimage;
    }

    public NewsClass(String newstitle,String newsdetails, String types , byte[] newsimage)
    {
        this.NewsTitle = newstitle;
        this.NewsDetails=newsdetails;
        this.Types = types;

        this.NewsImage = newsimage;
    }

    public String getNewsTitle(){
        return this.NewsTitle;
    }
    // setting id
    public void setNewsTitle(String newstitle){
        this.NewsTitle = newstitle;
    }

    public String getNewsDetails(){
        return this.NewsDetails;
    }
    // setting id
    public void setNewsDetails(String newsdetails){
        this.NewsDetails = newsdetails;
    }



    public String getTypes(){
        return this.Types;
    }
    // setting id
    public void setTypes(String types){
        this.Types = types;
    }

    public byte[] setNewsImage(byte[] newsimage){
        return this.NewsImage=newsimage;
    }

    public byte[] getNewsImage(){
        return this.NewsImage;
    }


}//End of NewsClass

