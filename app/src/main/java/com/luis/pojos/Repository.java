package com.luis.pojos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Repository {

    private static Repository INSTANCE = null;
    private static int ORDER = 0;
    private static int IDTEST = -1;

    //El monitor guarda sus deportistas
    private HashMap<String, Monitor> monitores;
    private HashMap<String, Deportista> deportistas;
    private HashMap<String, Equipo> equipos;

    private Repository(){
        monitores = new HashMap<String, Monitor>();
        deportistas = new HashMap<String, Deportista>();
        equipos = new HashMap<String, Equipo>();
    }

    public static int getORDER(){return ++ORDER; }

    public static void setIDTEST(int id){IDTEST = id;}

    public static int getIDTEST(){return IDTEST;}

    public static Repository getInstance(Context a){
        if(INSTANCE == null) return loadInstance(a);
        return INSTANCE;
    }

    public HashMap<String, Monitor> getMonitores() {
        return monitores;
    }

    public void setMonitores(HashMap<String, Monitor> monitores) {
        this.monitores = monitores;
    }

    private static Repository loadInstance(Context a){
        INSTANCE = new Repository();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document("datos/repository");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        INSTANCE=document.toObject(Repository.class);
                        System.out.println("AAAAAA"+INSTANCE.monitores.keySet().size());
                    } else {
                        Gson g = new Gson();

                        try {
                            INSTANCE = g.fromJson(new BufferedReader(new InputStreamReader(
                                    a.openFileInput("datos.json"))),Repository.class);
                        } catch (FileNotFoundException e) {
                            INSTANCE = new Repository();
                        }
                    }
                } else {
                    Gson g = new Gson();

                    try {
                        INSTANCE = g.fromJson(new BufferedReader(new InputStreamReader(
                                a.openFileInput("datos.json"))),Repository.class);
                    } catch (FileNotFoundException e) {
                        INSTANCE = new Repository();
                    }
                }
            }
        });
        return INSTANCE;
    }

    public static boolean persistInstance(Context a){
        Gson g = new GsonBuilder().setPrettyPrinting().create();

        try {
            String s = g.toJson(INSTANCE);
            FileOutputStream fos = a.openFileOutput("datos.json", Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {
            return false;
        }

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.document("datos/repository")
                    .set(INSTANCE, SetOptions.merge());
        }catch (Exception e){
        }

        return true;
    }

    public HashMap<String, Deportista> getDeportistas() {
        return deportistas;
    }

    public void setDeportistas(HashMap<String, Deportista> deportistas) {
        this.deportistas = deportistas;
    }

    public Monitor getMonitor(String nombreMonitor){
        return monitores.get(nombreMonitor);
    }

    public boolean writeMonitor(Monitor monitor){
        return monitores.put(monitor.getName(), monitor) != null;
    }

    public Deportista getDeportista(String nombreDeportista){
        return deportistas.get(nombreDeportista);
    }

    public boolean writeDeportista(Deportista deportista){
        return deportistas.put(deportista.getName(), deportista) != null;
    }

    public HashMap<String, Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(HashMap<String, Equipo> equipos) {
        this.equipos = equipos;
    }

    public Equipo getEquipo(int id){
        return equipos.get(id);
    }

    public boolean writeEquipo(Equipo equipo){
        return equipos.put(equipo.getId(), equipo) != null;
    }

    public static void nuke(Context a){
        INSTANCE = new Repository();
        persistInstance(a);
    }
}
