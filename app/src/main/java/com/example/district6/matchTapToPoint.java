package com.example.district6;

import com.esri.arcgisruntime.geometry.Point;

/*
    Checks if the point on the screen the user tapped matches to a site
    If it does returns corresponding site and updates
    "message" to the name of the site
 */

public class matchTapToPoint {
    private manageSites sites= new manageSites();
    private String message;
    private int numberForMediaArray;

    /*
        Check if user tapped site
        if user did, return the Point of the site
     */
    public Point getPoint(Point wgs84Point){
        Point returnPoint=null;

        if (String.format("%.6g%n", wgs84Point.getY()).equals(sites.getY_stMarks())
                &&  String.format("%.6g%n", wgs84Point.getX()).equals(sites.getX_stMarks())){
            returnPoint= sites.getStMarks();
            message="This is St. Marks Church!";
            numberForMediaArray=0;
        }

        if (String.format("%.6g%n", wgs84Point.getY()).equals(sites.getY_starCinema())
                &&  String.format("%.6g%n", wgs84Point.getX()).equals(sites.getX_starCinema())){
            returnPoint= sites.getStarCinema();
            message="This is where Star Cinema was!";
            numberForMediaArray=1;
        }

        if (String.format("%.6g%n", wgs84Point.getY()).equals(sites.getY_hanoverStreet())
                &&  String.format("%.6g%n", wgs84Point.getX()).equals(sites.getX_hanoverStreet())){
            returnPoint= sites.getHanoverStreet();
            message="This is Hanover Street!";
            numberForMediaArray=2;
        }

        if (String.format("%.6g%n", wgs84Point.getY()).equals(sites.getY_sevenSteps())
                &&  String.format("%.6g%n", wgs84Point.getX()).equals(sites.getX_sevenSteps())){
            returnPoint= sites.getSevenSteps();
            message="This is where the Seven Steps were!";
            numberForMediaArray=3;
        }

        if (String.format("%.6g%n", wgs84Point.getY()).equals(sites.getY_publicWashHouse())
                &&  String.format("%.6g%n", wgs84Point.getX()).equals(sites.getX_publicWashHouse())){
            returnPoint= sites.getPublicWashHouse();
            message="This is where the Public Wash House was!";
            numberForMediaArray=4;
        }

        return returnPoint;
    }
    /*
        getter method to retrieve the message
     */
    public String getMessage(){
        return message;
    }

    public int getNumberForMediaArray() {
        return numberForMediaArray;
    }
}
