package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.luis.ChatActivity;
import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Metrica;
import com.luis.pojos.Repository;

public class EjerciciosActivity extends AppCompatActivity {

    String name;
    public static final String EXTRA_MESSAGE ="msg";

    int clicks;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        clicks = 0;
        time = System.nanoTime();

        TextView textView = findViewById(R.id.textView);
        textView.setText("Bienvenido, " + name);
        getSupportActionBar().hide();

    }

    public void andar(View view){
        Intent intent = new Intent(this, AndarActivity.class);

        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void correr(View view){
        Intent intent = new Intent(this, CorrerActivity.class);

        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void bici(View view){
        Intent intent = new Intent(this, BiciActivity.class);

        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void flexiones(View view){
        Intent intent = new Intent(this, FlexionesActivity.class);

        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void equipos(View view){
        Intent intent = new Intent(this, EquipoActivity.class);

        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void monitor(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        Repository r = Repository.getInstance(this);
        String monitor = r.getDeportista(name).getIdMonitor();
        String[] params = {name, monitor+name, "(deportista)", monitor};
        intent.putExtra(EXTRA_MESSAGE, params);
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