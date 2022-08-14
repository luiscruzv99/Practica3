package com.luis.monitorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.luis.ChatActivity;
import com.luis.MainActivity;
import com.luis.R;
import com.luis.TrackEjActivity;
import com.luis.deportistaActivities.AndarActivity;
import com.luis.pojos.Andar;
import com.luis.pojos.Bici;
import com.luis.pojos.Carrera;
import com.luis.pojos.Deportista;
import com.luis.pojos.EjercicioAvg;
import com.luis.pojos.Flexiones;
import com.luis.pojos.Metrica;
import com.luis.pojos.Monitor;
import com.luis.pojos.Repository;

import java.util.ArrayList;

public class DeportistaActivity extends AppCompatActivity {

    String name;
    Repository r;
    Monitor m;
    ArrayList<EjercicioAvg> stats;
    public static final String EXTRA_MESSAGE ="msg";

    int clicks;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deportista);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        r = Repository.getInstance(this);
        m = r.getMonitor(r.getDeportista(name).getIdMonitor());

        clicks = 0;
        time = System.nanoTime();

        TextView nameView = findViewById(R.id.deportName);
        nameView.setText(name);

        getSupportActionBar().hide();

        //Creamos la lista con stats
        ListView listaDeportista = (ListView) findViewById(R.id.listaStats);
        EjercicioArrayAdapter ejercicioArrayAdapter = new EjercicioArrayAdapter(getApplicationContext(), R.layout.ejercicio_row);
        listaDeportista.setAdapter(ejercicioArrayAdapter);
        stats = generaStats(r.getDeportista(name));

        for(EjercicioAvg e: stats){
            ejercicioArrayAdapter.add(e);
        }

        FloatingActionButton fab = findViewById(R.id.openChat);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                String[] params = {m.getName(), m.getName()+name, "(Monitor)", name};
                intent.putExtra(EXTRA_MESSAGE, params);
                startActivity(intent);
            }
        });
    }

    private ArrayList<EjercicioAvg> generaStats(Deportista d){

        ArrayList<EjercicioAvg> ejercicioAvgs = new ArrayList<>();

        //Recorremos paseos y hacemos media
        int minutosMedios=0;
        int conteo=0;
        String nombre = "Paseos";
        int iconoId = getResources().getIdentifier("walking","drawable", "com.luis");
        for(Andar p: d.getPaseos()){
            conteo ++;
            minutosMedios += p.getMillisActivity();
        }
        minutosMedios = (int) (minutosMedios / (d.getPaseos().size() + 0.001));
        minutosMedios = minutosMedios / (1000*60);

        ejercicioAvgs.add(new EjercicioAvg(minutosMedios, conteo, nombre, iconoId));

        //Recorremos carreras y hacemos media
        minutosMedios=0;
        conteo=0;
        nombre = "Carreras";
        iconoId = getResources().getIdentifier("running","drawable", "com.luis");
        for(Carrera p: d.getCarreras()){
            conteo ++;
            minutosMedios += p.getMillisActivity();
        }
        minutosMedios = (int) (minutosMedios / (d.getCarreras().size() + 0.001));
        minutosMedios = minutosMedios / (1000*60);

        ejercicioAvgs.add(new EjercicioAvg(minutosMedios, conteo, nombre, iconoId));

        //Recorremos bicis y hacemos media
        minutosMedios=0;
        conteo=0;
        nombre = "Bici";
        iconoId = getResources().getIdentifier("biking","drawable", "com.luis");
        for(Bici p: d.getBicicletas()){
            conteo ++;
            minutosMedios += p.getMillisActivity();
        }
        minutosMedios = (int) (minutosMedios / (d.getBicicletas().size() + 0.001));
        minutosMedios = minutosMedios / (1000*60);

        ejercicioAvgs.add(new EjercicioAvg(minutosMedios, conteo, nombre, iconoId));

        //Recorremos flexiones y hacemos media
        minutosMedios=0;
        conteo=0;
        nombre = "Flexiones";
        iconoId = getResources().getIdentifier("pushups","drawable", "com.luis");
        for(Flexiones p: d.getFlexiones()){
            conteo ++;
            minutosMedios += p.getMillisActivity();
        }
        minutosMedios = (int) (minutosMedios / (d.getFlexiones().size() + 0.001));
        minutosMedios = minutosMedios / (1000*60);

        ejercicioAvgs.add(new EjercicioAvg(minutosMedios, conteo, nombre, iconoId));

        return ejercicioAvgs;
    }

    public void launchMapRunning(View view){

        Intent intent = new Intent(getApplicationContext(), TrackEjActivity.class);
        String[] datos = {name, "correr"};
        intent.putExtra(EXTRA_MESSAGE, datos);
        startActivity(intent);

    }

    public void launchMapBiking(View view){

        Intent intent = new Intent(getApplicationContext(), TrackEjActivity.class);
        String[] datos = {name, "bici"};
        intent.putExtra(EXTRA_MESSAGE, datos);
        startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();

        time = System.nanoTime() - time;
        time /= 1000000000;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Metrica m = new Metrica(clicks, time, this.getClass().getSimpleName(), Repository.getIDTEST());
        db.collection("metricas").add(m);

        clicks = 0;
        time = System.nanoTime();
    }

    @Override
    public void onUserInteraction(){
        clicks ++;
    }

}