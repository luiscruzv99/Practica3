package com.luis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.luis.pojos.MensajeChat;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    //TODO: Recibir el nombre del usuario y el nombre del chat al que conectarse
    String name;
    String chatLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String[] params;

        Intent intent = getIntent();
        params = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
        name = params[0];
        chatLoc = params[1];

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.enviarMsg);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.msgField);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // TODO: Hacer publicacion del mensaje a firestore, al chat correspondiente
                db.collection("chats/"+chatLoc+"/mensajes").add(new MensajeChat(input.getText().toString(), name));

                // Clear the input
                input.setText("");
            }
        });

        ListView vistaMsgs = findViewById(R.id.listaMsgs);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats/" + chatLoc+"/mensajes").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value == null) return;
                ArrayAdapter<MensajeChat> adapter = new ArrayAdapter<MensajeChat>(getApplicationContext(), R.layout.mensaje);
                for(QueryDocumentSnapshot e: value){
                    adapter.add(e.toObject(MensajeChat.class));
                }

                vistaMsgs.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void displayChatMessages() {
        //TODO: Mostrar los mensajes del chat

    }
}