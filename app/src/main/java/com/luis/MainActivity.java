package com.luis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.luis.deportistaActivities.EjerciciosActivity;
import com.luis.monitorActivities.MonitorActivity;
import com.luis.pojos.Deportista;
import com.luis.pojos.Metrica;
import com.luis.pojos.Monitor;
import com.luis.pojos.Repository;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    public static final String EXTRA_MESSAGE ="msg";
    private FirebaseAuth mAuth;
    Repository r;
    ArrayAdapter<String> adapter;
    ListView listView;
    AlertDialog dialog;
    int m_Text = 0;
    int clicks;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clicks = 0;
        time = System.nanoTime();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                    }
                });
        //Repository.nuke(this);
        getSupportActionBar().hide();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Id de test");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = Integer.parseInt(input.getText().toString());
                Repository.setIDTEST(m_Text);
            }
        });

        builder.show();

        r = Repository.getInstance(this);
    }

    public void login(View view){
        Intent intent;
        EditText nameEdit = (EditText) findViewById(R.id.campoUsuario);
        EditText passEdit = (EditText) findViewById(R.id.campoPassword);
        String name = nameEdit.getText().toString();
        String pass = passEdit.getText().toString();
        r = Repository.getInstance(this);
        RadioGroup tipo = (RadioGroup) findViewById(R.id.tipoUsuario);
        int boton = tipo.getCheckedRadioButtonId();
        if(boton == R.id.deportista){
            if (!verificaDeportista(name,pass,view)) return;
            intent = new Intent(this, EjerciciosActivity.class);
        }else if (boton == R.id.monitor){
            if(!verificaMonitor(name,pass)) return;
            intent = new Intent(this, MonitorActivity.class);
        }else{
            return;
        }
        intent.putExtra(EXTRA_MESSAGE, name);
        Repository.persistInstance(this);
        startActivity(intent);
    }

    public void signIn(View view){
        EditText nameEdit = (EditText) findViewById(R.id.campoUsuario);
        EditText passEdit = (EditText) findViewById(R.id.campoPassword);
        String name = nameEdit.getText().toString();
        String pass = passEdit.getText().toString();

        RadioGroup tipo = (RadioGroup) findViewById(R.id.tipoUsuario);
        int boton = tipo.getCheckedRadioButtonId();

        if(boton == R.id.deportista) {
            Deportista d = r.getDeportista(name);
            if (d == null && name.length() > 2 && pass.length() > 2) {
                d = new Deportista(name, pass, "");
                generaUsuario(d, view);
            } else {
                Toast.makeText(this, "Error creando usuario, la longitud del nombre y contraseña debe ser mayor", Toast.LENGTH_LONG).show();
                return;
            }
        }else if (boton == R.id.monitor) {
            Monitor d = r.getMonitor(name);
            if(d == null && name.length() > 2 && pass.length() > 2){
                //Si el usuario no existe, guardar usuario en el repositorio y mostrar toast
                d = new Monitor(name, pass, new ArrayList<>());
                r.writeMonitor(d);
                login(view);
            }else{
                Toast.makeText(this, "Error creando usuario, la longitud del nombre y contraseña debe ser mayor", Toast.LENGTH_LONG).show();
                return;
            }
        }else return;

        Toast.makeText(this, "Usuario " + name + " creado", Toast.LENGTH_LONG).show();
    }

    public void finish(View view){
        Repository.persistInstance(this);
        finish();
        System.exit(0);
    }

    private boolean verificaDeportista(String name, String pass, View v){

        //Mirar si el usuario existe en el repositorio
        Deportista d = r.getDeportista(name);
        if(d != null) {
            //Si existe, comparar la contraseña guardada con la introducida
            if(d.getPass().equals(pass)) return true;
            else{
                //Si no coinciden devolver falso
                Toast.makeText(this, "Error, la contraseña no coincide", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Error, el usuario no existe", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean verificaMonitor(String name, String pass){

        Monitor d = r.getMonitor(name);
        if(d != null) {
            //Si existe, comparar la contraseña guardada con la introducida
            if(d.getPass().equals(pass)) return true;
                //Si no coinciden devolver falso
            else{
                Toast.makeText(this, "Error, la contraseña no coincide", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Error, el usuario no existe", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void generaUsuario(Deportista d, View view){
        ArrayList<String> monitores = new ArrayList<String>(r.getMonitores().keySet());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.row, null);
        listView = rowList.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, monitores);
        listView.setAdapter(adapter);


        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        dialog = alertDialog.create();
        Deportista finalD = d;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finalD.setIdMonitor(monitores.get(position));
                r.getMonitor(monitores.get(position)).getDeportistas().add(finalD.getName());
                r.writeDeportista(finalD);
                dialog.dismiss();
                login(view);
            }
        });

        dialog.show();
    }


    @Override
    public void onUserInteraction(){
        clicks ++;
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

}