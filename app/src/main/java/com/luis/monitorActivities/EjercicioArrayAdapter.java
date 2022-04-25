package com.luis.monitorActivities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luis.R;
import com.luis.pojos.EjercicioAvg;

import java.util.ArrayList;
import java.util.List;

public class EjercicioArrayAdapter extends ArrayAdapter<EjercicioAvg> {

    private static final String TAG = "EjercicioArrayAdapter";
    private List<EjercicioAvg> ejercicioAvgList = new ArrayList<>();

    static class EjercicioViewHolder{
        ImageView iconoEj;
        TextView nombreEj;
        TextView tiempoMed;
        TextView instEj;
    }

    public EjercicioArrayAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    @Override
    public void add(EjercicioAvg ej){
        ejercicioAvgList.add(ej);
        super.add(ej);
    }

    @Override
    public int getCount(){
        return this.ejercicioAvgList.size();
    }

    @Override
    public EjercicioAvg getItem(int index){
        return this.ejercicioAvgList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EjercicioViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.ejercicio_row, parent, false);
            viewHolder = new EjercicioViewHolder();
            viewHolder.iconoEj = (ImageView) row.findViewById(R.id.iconoEj);
            viewHolder.nombreEj = (TextView) row.findViewById(R.id.nombreEj);
            viewHolder.tiempoMed = (TextView) row.findViewById(R.id.tiempoMed);
            viewHolder.instEj = (TextView) row.findViewById(R.id.instEj);
            row.setTag(viewHolder);
        } else {
            viewHolder = (EjercicioViewHolder) row.getTag();
        }
        EjercicioAvg ejercicioAvg = getItem(position);
        viewHolder.iconoEj.setImageResource(ejercicioAvg.getIcono());
        viewHolder.nombreEj.setText(ejercicioAvg.getTipo());
        viewHolder.tiempoMed.setText(Long.toString(ejercicioAvg.getMillisActivity()));
        viewHolder.instEj.setText(Integer.toString(ejercicioAvg.getInstancias()));
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
