package com.luis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.luis.pojos.MensajeChat;

public class ChatActivity extends AppCompatActivity {

    //TODO: Recibir el nombre del usuario y el nombre del chat al que conectarse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.msgField);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // TODO: Hacer publicacion del mensaje a firestore, al chat correspondiente


                // Clear the input
                input.setText("");
            }
        });
    }

    private void displayChatMessages() {
        //TODO: Mostrar los mensajes del chat
    }
}