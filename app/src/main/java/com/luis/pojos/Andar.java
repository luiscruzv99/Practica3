package com.luis.pojos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class Andar extends EUnTracked {

    private int pasos;

    public Andar(){}

    public Andar(int pasos, long millisActivity) {
        super(millisActivity);
        this.pasos = pasos;
    }

    public int getPasos() {
        return pasos;
    }

    public void setPasos(int pasos) {
        this.pasos = pasos;
    }
}
