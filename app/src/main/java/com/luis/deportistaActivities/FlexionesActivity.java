
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

import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Repository;
import com.luis.pojos.Flexiones;

public class FlexionesActivity extends AppCompatActivity implements SensorEventListener{


    boolean empezado;
    int flexiones;
    TextView flexView;
    Repository r;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexiones);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor sensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensor!=null) {
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_UI);
            System.out.println("Sensor Funcionando");
        }

        empezado = false;
        flexiones = 0;
        flexView = findViewById(R.id.flexiones);

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
                System.out.println(flexiones);
                flexView.setText("Flexiones: "+flexiones);
            }
        }


    }

    public void empieza(View view){

        Button btn = findViewById(R.id.empiezaFlex);
        Chronometer chrono = findViewById(R.id.chronoFlex);

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
}