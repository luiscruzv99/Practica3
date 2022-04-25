package com.luis.pojos;

import java.util.ArrayList;

public abstract class ETracked extends Ejercicio{

    private ArrayList<Localizacion> path;

    public ETracked(){super();}

    public ETracked(long millisActivity, ArrayList<Localizacion> path) {
        super(millisActivity);
        this.path = path;
    }

    public ArrayList<Localizacion> getPath() {
        return path;
    }

    public void setPath(ArrayList<Localizacion> path) {
        this.path = path;
    }
}
