package com.luis.pojos;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    String id;
    List<String> integrantes;

    public Equipo() {
        integrantes = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<String> integrantes) {
        this.integrantes = integrantes;
    }
}
