package com.luis.monitorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.luis.MainActivity;
import com.luis.R;
import com.luis.monitorActivities.DeportistaActivity;
import com.luis.pojos.Repository;

import java.util.ArrayList;

public class MonitorActivity extends AppCompatActivity {


    Repository r;
    ArrayAdapter<String> adapter;
    public static final String EXTRA_MESSAGE = "msg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String monitor = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        System.out.println("Nombre monitor: " + monitor);
        r = Repository.getInstance(this);
        ArrayList<String> deportistas;
        if(r.getMonitor(monitor).getDeportistas() != null) {
             deportistas= r.getMonitor(monitor).getDeportistas();
        }else{
            deportistas = new ArrayList<String>();
        }
        ListView listView = findViewById(R.id.vistaUsuarios);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deportistas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DeportistaActivity.class);
                intent.putExtra(EXTRA_MESSAGE, deportistas.get(position));

                startActivity(intent);
            }
        });
    }
}