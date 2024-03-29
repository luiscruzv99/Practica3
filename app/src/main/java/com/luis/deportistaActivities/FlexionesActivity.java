
package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.luis.pojos.Flexiones;

public class FlexionesActivity extends AppCompatActivity implements SensorEventListener{


    boolean empezado;
    int flexiones;
    TextView flexView;
    Repository r;
    String name;

    int clicks;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexiones);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor sensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensor!=null) {
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_UI);
        }

        clicks = 0;
        time = System.nanoTime();

        empezado = false;
        flexiones = 0;

        r = Repository.getInstance(this);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        getSupportActionBar().hide();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(empezado){
            if(sensorEvent.values[0] < 5){
                flexiones ++;
                flexView.setText("Flexiones: "+flexiones);
            }
        }


    }

    public void empieza(View view){

        Button btn = findViewById(R.id.empiezaFlex);
        Chronometer chrono = findViewById(R.id.chronoFlex);
        flexView = (TextView)findViewById(R.id.flexiones);


        if(btn.getText().equals("Empezar")){
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            empezado = true;
            btn.setText("Terminar");
            flexView.setText("Flexiones: 0");

        }else if(btn.getText().equals("Terminar")) {
            chrono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
            r.getDeportista(name).getFlexiones().add(new Flexiones(elapsedMillis, flexiones));
            Repository.persistInstance(this);
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

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