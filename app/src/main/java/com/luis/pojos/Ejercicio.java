package com.luis.pojos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public abstract class Ejercicio {

    private int id;
    private long millisActivity;

    public Ejercicio(){}

    public Ejercicio(long millisActivity) {
        this.millisActivity = millisActivity;
    }

    public long getMillisActivity() {
        return millisActivity;
    }

    public void setMillisActivity(long millisActivity) {
        this.millisActivity = millisActivity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
