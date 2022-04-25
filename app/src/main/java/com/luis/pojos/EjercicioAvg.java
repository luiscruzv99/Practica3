package com.luis.pojos;

public class EjercicioAvg extends Ejercicio {

    private int instancias;
    private String tipo;
    private int icono;

    public EjercicioAvg(){}

    public EjercicioAvg(long millisActivity, int instancias, String tipo, int icono) {
        super(millisActivity);
        this.instancias = instancias;
        this.tipo = tipo;
        this.icono = icono;
    }

    public int getInstancias() {
        return instancias;
    }

    public void setInstancias(int instancias) {
        this.instancias = instancias;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
