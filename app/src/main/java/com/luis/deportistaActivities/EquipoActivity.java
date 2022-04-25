package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luis.R;
import com.luis.pojos.Repository;

public class EquipoActivity extends AppCompatActivity {

    Repository r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        Intent intent = getIntent();

        r = Repository.getInstance(this);

        ListView listaEquipos = (ListView) findViewById(R.id.listaEquipos);
        EquipoArrayAdapter equipoArrayAdapter = new EquipoArrayAdapter(getApplicationContext(), R.layout.equipo_row);
        listaEquipos.setAdapter(equipoArrayAdapter);

        equipoArrayAdapter.addAll(r.getEquipos().values());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View alertCustomdialog = LayoutInflater.from(EquipoActivity.this).inflate(R.layout.crea_equipo_layout,null);
                //initialize alert builder.
                AlertDialog.Builder alert = new AlertDialog.Builder(EquipoActivity.this);

                //TODO: Coger lista de nombres del monitor
                //TODO: Mostrarlas en la vista con array adaper, con checkbox

                //set our custom alert dialog to tha alertdialog builder
                alert.setView(alertCustomdialog);
                final AlertDialog dialog = alert.create();
                //finally show the dialog box in android all
                dialog.show();
            }
        });
    }
    //TODO: Para crear equipo, mostrar popup con lista de deportistas bajo mismo monitor


}