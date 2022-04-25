package com.luis.pojos;

import android.location.Location;

import androidx.room.Entity;

import java.util.ArrayList;

public class Carrera extends ETracked{


    public Carrera(){}

    public Carrera(long millisActivity, ArrayList<Localizacion> path) {
        super(millisActivity, path);
    }
}
