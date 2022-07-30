package com.luis.deportistaActivities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.luis.R;
import com.luis.pojos.Equipo;

import java.util.ArrayList;
import java.util.List;

public class EquipoArrayAdapter extends ArrayAdapter<Equipo> {

    private static final String TAG = "EquipoArrayAdapter";
    private List<Equipo> equipoAvgList = new ArrayList<>();

    static class EquipoViewHolder{
        TextView nombreEj;
        TextView numIntegrantes;
    }

    public EquipoArrayAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    @Override
    public void add(Equipo ej){
        equipoAvgList.add(ej);
        super.add(ej);
    }

    @Override
    public int getCount(){
        return this.equipoAvgList.size();
    }

    @Override
    public Equipo getItem(int index){
        return this.equipoAvgList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EquipoViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.equipo_row, parent, false);
            viewHolder = new EquipoViewHolder();
            viewHolder.nombreEj = (TextView) row.findViewById(R.id.nombreEq);
            viewHolder.numIntegrantes = (TextView) row.findViewById(R.id.numInt);
            row.setTag(viewHolder);
        } else {
            viewHolder = (EquipoViewHolder) row.getTag();
        }
        Equipo equipoAvg = getItem(position);
        viewHolder.nombreEj.setText(equipoAvg.getId());
        viewHolder.numIntegrantes.setText(Integer.toString(equipoAvg.getIntegrantes().size()));
        return row;
    }
}
