package com.luis.pojos;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class Localizacion {

    private double lat;
    private double lon;

    public Localizacion(){}

    public Localizacion(Location l) {
        this.lat = l.getLatitude();
        this.lon = l.getLongitude();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
