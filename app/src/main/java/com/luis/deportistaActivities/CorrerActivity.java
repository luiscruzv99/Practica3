package com.luis.deportistaActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Metrica;
import com.luis.pojos.Repository;
import com.luis.pojos.Carrera;
import com.luis.pojos.Localizacion;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CorrerActivity extends AppCompatActivity implements LocationListener {

    float dist;
    Location loc;
    boolean empezado;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    TextView distancia;
    Repository r;
    String name;
    ArrayList<Localizacion> localizaciones;

    int clicks;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correr);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }

        Location location;
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        clicks = 0;
        time = System.nanoTime();

        dist = 0.0f;
        empezado = false;
        this.loc = location;

        r = Repository.getInstance(this);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        localizaciones = new ArrayList<>();
        getSupportActionBar().hide();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if(empezado) {
            dist += this.loc.distanceTo(location);
            localizaciones.add(new Localizacion(location));
            distancia.setText("Distancia: " + df.format(dist/1000) + " km");
            this.loc = location;

        }
    }


    public void empieza(View view){

        Button btn = findViewById(R.id.empiezaCorrer);
        Chronometer chrono = findViewById(R.id.chronoCorrer);
        distancia = (TextView)findViewById(R.id.distancia);


        if(btn.getText().equals("Empezar")){

            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            empezado = true;
            btn.setText("Terminar");
            distancia.setText("Distancia: 0 km");

        }else if(btn.getText().equals("Terminar")){
            chrono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
            r.getDeportista(name).getCarreras().add(new Carrera(elapsedMillis, localizaciones));
            Repository.persistInstance(this);
            finish();
        }


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