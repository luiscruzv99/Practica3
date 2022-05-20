package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.luis.ChatActivity;
import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Repository;

public class EjerciciosActivity extends AppCompatActivity {

    String name;
    public static final String EXTRA_MESSAGE ="msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.textView);
        textView.setText("Bienvenido, " + name);

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
        String[] params = {name, monitor+name, "(deportista)"};
        intent.putExtra(EXTRA_MESSAGE, params);
        startActivity(intent);
    }

}