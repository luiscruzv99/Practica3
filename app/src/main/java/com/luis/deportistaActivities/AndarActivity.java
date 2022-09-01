package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.luis.pojos.Andar;

public class AndarActivity extends AppCompatActivity implements SensorEventListener {

    boolean empezado;
    int pasos;
    TextView pasoView;
    Repository r;
    String name;

    int clicks;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andar);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor sensor= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (sensor!=null) {
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_UI);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 123);
            return;
        }

        empezado = false;
        pasos = 0;

        clicks = 0;
        time = System.nanoTime();

        r = Repository.getInstance(this);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        getSupportActionBar().hide();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(empezado){
            pasos++;
            pasoView.setText("Pasos: " + pasos);
        }
    }


    public void empieza(View view){

        Button btn = findViewById(R.id.empiezaAndar);
        Chronometer chrono = findViewById(R.id.chronoAndar);
        pasoView = (TextView) findViewById(R.id.pasos);

        if(btn.getText().equals("Empezar")){
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            empezado = true;
            btn.setText("Terminar");
            pasoView.setText("Pasos: 0");

        }else if(btn.getText().equals("Terminar")) {
            chrono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
            r.getDeportista(name).getPaseos().add(new Andar(pasos, elapsedMillis));
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}