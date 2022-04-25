package com.luis.pojos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Query;

import java.util.ArrayList;

public class Deportista extends Usuario{


    private String idMonitor;

    private ArrayList<Andar> paseos;
    private ArrayList<Flexiones> flexiones;
    private ArrayList<Bici> bicicletas;
    private ArrayList<Carrera> carreras;
    private ArrayList<Integer> equipos;

    public Deportista(){
        super();
        paseos = new ArrayList<>();
        flexiones = new ArrayList<>();
        bicicletas = new ArrayList<>();
        carreras = new ArrayList<>();
        equipos = new ArrayList<>();
    }

    public Deportista(String name, String pass, String idMonitor) {
        super(name, pass);
        paseos = new ArrayList<>();
        flexiones = new ArrayList<>();
        bicicletas = new ArrayList<>();
        carreras = new ArrayList<>();
        equipos = new ArrayList<>();

        this.idMonitor = idMonitor;
    }

    public String getIdMonitor() {
        return idMonitor;
    }

    public void setIdMonitor(String idMonitor) {
        this.idMonitor = idMonitor;
    }

    public ArrayList<Andar> getPaseos() {
        return paseos;
    }

    public void setPaseos(ArrayList<Andar> paseos) {
        this.paseos = paseos;
    }

    public ArrayList<Flexiones> getFlexiones() {
        return flexiones;
    }

    public void setFlexiones(ArrayList<Flexiones> flexiones) {
        this.flexiones = flexiones;
    }

    public ArrayList<Bici> getBicicletas() {
        return bicicletas;
    }

    public void setBicicletas(ArrayList<Bici> bicicletas) {
        this.bicicletas = bicicletas;
    }

    public ArrayList<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(ArrayList<Carrera> carreras) {
        this.carreras = carreras;
    }

    public ArrayList<Integer> getEquipos() {
        return equipos;
    }

    public void setEquipos(ArrayList<Integer> equipos) {
        this.equipos = equipos;
    }
}
