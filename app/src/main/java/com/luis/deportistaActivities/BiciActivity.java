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

import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Repository;
import com.luis.pojos.Bici;
import com.luis.pojos.Localizacion;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BiciActivity extends AppCompatActivity implements LocationListener {

    float dist;
    Location loc;
    boolean empezado;
    float vel;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    Chronometer chrono;
    TextView velocidad;
    Repository r;
    String name;
    ArrayList<Localizacion> localizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bici);

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

        dist = 0.0f;
        vel = 0.0f;
        this.loc = location;
        empezado = false;
        chrono = findViewById(R.id.chronoBici);
        velocidad = findViewById(R.id.velocidad);

        r = Repository.getInstance(this);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        localizaciones = new ArrayList<>();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if(empezado) {
            dist += this.loc.distanceTo(location);
            localizaciones.add(new Localizacion(location));
            long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
            double tiempo = elapsedMillis / (1000.0 * 3600.0);
            velocidad.setText("Velocidad: " + df.format(dist/(1000*tiempo)) + " km/h");
            this.loc = location;

        }
    }
    public void empieza(View view){

        Button btn = findViewById(R.id.empiezaBici);

        if(btn.getText().equals("Empezar")){
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            empezado = true;
            btn.setText("Terminar");
            velocidad.setText("Velocidad: 0 km/h");
        }else if(btn.getText().equals("Terminar")) {
            chrono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
            r.getDeportista(name).getBicicletas().add(new Bici(elapsedMillis, vel, localizaciones));
            Repository.persistInstance(this);
            finish();
        }
    }

}