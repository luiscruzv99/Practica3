package com.luis.pojos;

import java.util.ArrayList;
import java.util.HashMap;

public class Monitor extends Usuario{

    private ArrayList<String> deportistas;

    public Monitor(){}

    public Monitor(String name, String pass, ArrayList<String> deportistas) {
        super(name, pass);
        this.deportistas = deportistas;
    }

    public ArrayList<String> getDeportistas() {
        return deportistas;
    }

    public void setDeportistas(ArrayList<String> deportistas) {
        this.deportistas = deportistas;
    }


}
