package com.luis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.luis.pojos.MensajeChat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatActivity extends AppCompatActivity {

    String name, name2;
    String chatLoc;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String[] params;

        Intent intent = getIntent();
        params = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
        name = params[0];
        name2 = params[3];
        chatLoc = params[1];
        type = params[2];

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.enviarMsg);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.msgField);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("chats/"+chatLoc+"/mensajes").add(new MensajeChat(input.getText().toString(), name+" "+type));

                // Clear the input
                input.setText("");
            }
        });

        RecyclerView vistaMsgs = findViewById(R.id.chatMsgs);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats/" + chatLoc+"/mensajes").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value == null) return;
                MensajeRecyclerAdapter adapter = new MensajeRecyclerAdapter(getApplicationContext(),new ArrayList<MensajeChat>());
                vistaMsgs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                for(QueryDocumentSnapshot e: value){
                    MensajeChat a = e.toObject(MensajeChat.class);
                    if(a.getMessageUser().equals(name+" "+type)){
                        a.setMessageUser("Tú");
                    }
                    adapter.getMsgs().add(a);
                    Collections.sort(adapter.getMsgs(), new Comparator<MensajeChat>() {
                        @Override
                        public int compare(MensajeChat o1, MensajeChat o2) {
                            if(o1.getMessageTime() == o2.getMessageTime())
                                return 0;
                            return o1.getMessageTime() < o2.getMessageTime() ? -1 : 1;
                        }
                    });
                }

                vistaMsgs.setAdapter(adapter);
                getSupportActionBar().setTitle("Chat con "+name2);
            }
        });
    }
}