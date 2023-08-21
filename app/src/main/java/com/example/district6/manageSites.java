package com.example.district6;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;

/*
Stores coordinates of the points and provides getter methods to
return the coordinates
 */
public class manageSites {
    Point stMarks = new Point( 18.431426366, -33.930871012, SpatialReferences.getWgs84());
    Point starCinema = new Point(18.431512923, -33.930622967, SpatialReferences.getWgs84());
    Point hanoverStreet = new Point(18.431203350, -33.930111583, SpatialReferences.getWgs84());
    Point sevenSteps = new Point(18.430697974, -33.929899071, SpatialReferences.getWgs84());
    Point publicWashHouse = new Point(18.430347690, -33.929730591, SpatialReferences.getWgs84());
    Point busStop = new Point(18.4259005, -33.9409073, SpatialReferences.getWgs84());

    public Point getStarCinema(){ return starCinema; }
    public Point getStMarks(){ return stMarks; }
    public Point getHanoverStreet(){ return hanoverStreet; }
    public Point getSevenSteps(){ return sevenSteps; }
    public Point getPublicWashHouse(){ return publicWashHouse; }
    public Point getBusStop() { return busStop; }

    public String getX_stMarks(){
        return String.format("%.6g%n", stMarks.getX());
    }

    public String getY_stMarks(){
        return String.format("%.6g%n", stMarks.getY());
    }

    public String getX_starCinema(){
        return String.format("%.6g%n", starCinema.getX());
    }

    public String getY_starCinema(){
        return String.format("%.6g%n", starCinema.getY());
    }

    public String getX_hanoverStreet(){
        return String.format("%.6g%n", hanoverStreet.getX());
    }

    public String getY_hanoverStreet(){
        return String.format("%.6g%n", hanoverStreet.getY());
    }

    public String getX_sevenSteps(){
        return String.format("%.6g%n", sevenSteps.getX());
    }

    public String getY_sevenSteps(){
        return String.format("%.6g%n", sevenSteps.getY());
    }

    public String getX_publicWashHouse(){
        return String.format("%.6g%n", publicWashHouse.getX());
    }

    public String getY_publicWashHouse(){
        return String.format("%.6g%n", publicWashHouse.getY());
    }


}
