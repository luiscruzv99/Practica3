package com.luis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.luis.pojos.MensajeChat;

import java.util.ArrayList;
import java.util.List;

public class MensajeArrayAdapter extends ArrayAdapter<MensajeChat> {

    private static final String TAG = "MensajeArrayAdapter";
    private List<MensajeChat> mensajeList = new ArrayList<>();

    static class MensajeViewHolder{
        TextView nombreUsuario;
        TextView contenido;
        TextView fecha;
    }

    public MensajeArrayAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    @Override
    public void add(MensajeChat ej){
        mensajeList.add(ej);
        super.add(ej);
    }

    @Override
    public int getCount(){
        return this.mensajeList.size();
    }

    @Override
    public MensajeChat getItem(int index){
        return this.mensajeList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MensajeArrayAdapter.MensajeViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.mensaje_me, parent, false);
            viewHolder = new MensajeArrayAdapter.MensajeViewHolder();
            viewHolder.nombreUsuario = (TextView) row.findViewById(R.id.message_user);
            viewHolder.contenido = (TextView) row.findViewById(R.id.message_text);
            viewHolder.fecha = (TextView) row.findViewById(R.id.message_time);
            row.setTag(viewHolder);
        } else {
            viewHolder = (MensajeArrayAdapter.MensajeViewHolder) row.getTag();
        }
        MensajeChat mensaje = getItem(position);

        System.out.println(mensaje.getMessageUser());
        viewHolder.nombreUsuario.setText(mensaje.getMessageUser());
        viewHolder.contenido.setText(mensaje.getMessageText());
        return row;
    }
}
