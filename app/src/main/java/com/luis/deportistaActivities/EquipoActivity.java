package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Equipo;
import com.luis.pojos.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EquipoActivity extends AppCompatActivity {

    String name;
    Repository r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
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
                r = Repository.getInstance(alert.getContext());

                ArrayList<String> compis = r.getMonitor(r.getDeportista(name).getIdMonitor()).getDeportistas();
                compis.remove(name);

                //TODO: Mostrar elementos en la vista con checkbox
                ListView a = alertCustomdialog.findViewById(R.id.listaCompis);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(alert.getContext(), android.R.layout.simple_list_item_1, compis);
                a.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                //set our custom alert dialog to tha alertdialog builder
                alert.setView(alertCustomdialog);
                final AlertDialog dialog = alert.create();
                //finally show the dialog box in android all
                dialog.show();

                Button g = alertCustomdialog.findViewById(R.id.guardar);
                Button c = alertCustomdialog.findViewById(R.id.cancelar);

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: recoger los compis marcados e invocar creaEquipo
                        System.out.println("Guardado");
                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void creaEquipo(String nombre, List<String> integrantes, String name){

        Equipo e = new Equipo();
        e.setId(nombre);
        e.setIntegrantes(integrantes);
        e.getIntegrantes().add(name);

        r.getDeportista(name).getEquipos().add(nombre);
        for(String s: integrantes){
            r.getDeportista(s).getEquipos().add(nombre);
        }

        Repository.persistInstance(this);
    }

}