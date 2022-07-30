package com.luis.deportistaActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luis.ChatActivity;
import com.luis.MainActivity;
import com.luis.R;
import com.luis.pojos.Equipo;
import com.luis.pojos.Repository;

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

        ListView listaEquipos = findViewById(R.id.listaEquipos);
        EquipoArrayAdapter equipoArrayAdapter = new EquipoArrayAdapter(getApplicationContext(), R.layout.equipo_row);

        getSupportActionBar().hide();

        ArrayList<Equipo> eqs =new ArrayList<>(r.getEquipos().values());
        for(Equipo e: eqs){
            equipoArrayAdapter.add(e);
        }

        listaEquipos.setAdapter(equipoArrayAdapter);

        listaEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                String[] params = {name,eqs.get(i).getId(),"", eqs.get(i).getId()};
                intent.putExtra(MainActivity.EXTRA_MESSAGE, params);

                startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //Logica para anhadir un equipo
            @Override
            public void onClick(View view) {
                //Dialogo emergente
                View alertCustomdialog = LayoutInflater.from(EquipoActivity.this).inflate(R.layout.crea_equipo_layout,null);
                //initialize alert builder.
                AlertDialog.Builder alert = new AlertDialog.Builder(EquipoActivity.this);
                r = Repository.getInstance(alert.getContext());

                ArrayList<String> compis = r.getMonitor(r.getDeportista(name).getIdMonitor()).getDeportistas();
                compis.remove(name);

                ListView a = alertCustomdialog.findViewById(R.id.listaCompis);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(alert.getContext(), android.R.layout.simple_list_item_multiple_choice, compis);
                a.setAdapter(adapter);
                a.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                adapter.notifyDataSetChanged();


                //set our custom alert dialog to tha alertdialog builder
                alert.setView(alertCustomdialog);
                final AlertDialog dialog = alert.create();
                //finally show the dialog box in android all
                dialog.show();

                Button g = alertCustomdialog.findViewById(R.id.guardar);
                Button c = alertCustomdialog.findViewById(R.id.cancelar);

                // Guardado de los compañeros seleccionados
                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SparseBooleanArray values = a.getCheckedItemPositions();
                        ArrayList<String> selected = new ArrayList<>();

                        if(values.size() == 0){
                            Toast.makeText(alertCustomdialog.getContext(), "Selecciona al menos a un compañero", Toast.LENGTH_LONG).show();
                            return;
                        }

                        for(int i=0; i<values.size(); i++){
                            if(values.valueAt(i)){
                                selected.add(compis.get(i));
                            }
                        }

                        TextView input = alertCustomdialog.findViewById(R.id.nombreEquipo);
                        String nombreEquipo = input.getText().toString();
                        if(nombreEquipo.isEmpty()){
                            Toast.makeText(alertCustomdialog.getContext(), "Introduce un nombre de equipo", Toast.LENGTH_LONG).show();
                            return;
                        }

                        creaEquipo(nombreEquipo, selected, name);
                        dialog.dismiss();
                        equipoArrayAdapter.notifyDataSetChanged();
                        listaEquipos.setAdapter(equipoArrayAdapter);
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

        for(String s: integrantes){
            r.getDeportista(s).getEquipos().add(nombre);
        }

        r.writeEquipo(e);
        Repository.persistInstance(this);
    }

}