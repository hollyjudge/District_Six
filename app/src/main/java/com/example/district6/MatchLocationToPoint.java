package com.example.district6;

import com.esri.arcgisruntime.geometry.AngularUnit;
import com.esri.arcgisruntime.geometry.AngularUnitId;
import com.esri.arcgisruntime.geometry.GeodeticCurveType;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.LinearUnit;
import com.esri.arcgisruntime.geometry.LinearUnitId;
import com.esri.arcgisruntime.geometry.Point;

public class MatchLocationToPoint {
    private manageSites sites= new manageSites();
    private String message="";
    private double radius=5;
    private Point site=null;
    private int numberForMediaArray=0;

    public Point returnPoint(Point userLocation){

        if (GeometryEngine.distanceGeodetic(userLocation, sites.getStMarks(),
                new LinearUnit(LinearUnitId.METERS),
                new AngularUnit(AngularUnitId.DEGREES),
                GeodeticCurveType.NORMAL_SECTION).getDistance()<=radius) {
            site=sites.getStMarks();
            message= "This is St. Marks Church!";
            numberForMediaArray=0;
        }

        if (GeometryEngine.distanceGeodetic(userLocation, sites.getStarCinema(),
                new LinearUnit(LinearUnitId.METERS),
                new AngularUnit(AngularUnitId.DEGREES),
                GeodeticCurveType.NORMAL_SECTION).getDistance()<=radius) {
            site=sites.getStarCinema();
            message= "This is where Star Cinema was!";
            numberForMediaArray=1;
        }

        if (GeometryEngine.distanceGeodetic(userLocation, sites.getHanoverStreet(),
                new LinearUnit(LinearUnitId.METERS),
                new AngularUnit(AngularUnitId.DEGREES),
                GeodeticCurveType.NORMAL_SECTION).getDistance()<=radius) {
            site=sites.getHanoverStreet();
            message= "This is Hanover Street!";
            numberForMediaArray=2;
        }

        if (GeometryEngine.distanceGeodetic(userLocation, sites.getSevenSteps(),
                new LinearUnit(LinearUnitId.METERS),
                new AngularUnit(AngularUnitId.DEGREES),
                GeodeticCurveType.NORMAL_SECTION).getDistance()<=radius) {
            site=sites.getSevenSteps();
            message= "This is where the Seven Steps were!";
            numberForMediaArray=3;
        }

        if (GeometryEngine.distanceGeodetic(userLocation, sites.getPublicWashHouse(),
                new LinearUnit(LinearUnitId.METERS),
                new AngularUnit(AngularUnitId.DEGREES),
                GeodeticCurveType.NORMAL_SECTION).getDistance()<=radius) {
            site=sites.getPublicWashHouse();
            message= "This is where the Public Wash House was!";
            numberForMediaArray=4;
        }

        return site;
    }

    public String returnMessage() {
        return message;
    }

    public int getNumberForMediaArray() {
        return numberForMediaArray;
    }
}
