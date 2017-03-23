package com.dam.borja.proyectolocalizacion.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.borja.proyectolocalizacion.R;

import java.util.ArrayList;

/**
 * Created by borja on 22/3/17.
 */

public class AdaptadorSpinner extends BaseAdapter {

    Context context;
    ArrayList lista;

    public AdaptadorSpinner(Context context) {
        this.context = context;
        if (lista == null) {
            lista = new ArrayList();
            lista.add("Normal");
            lista.add("Satelite");
            lista.add("Hibrida");
        }
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_maptype_layout, parent, false);
        ImageView img = (ImageView) convertView.findViewById(R.id.spinner_img);
        TextView txt = (TextView) convertView.findViewById(R.id.textmap_type);

        switch (position) {
            case 0:
                img.setImageResource(R.drawable.spinner_map);
                break;
            case 1:
                img.setImageResource(R.drawable.spinner_sat);
                break;
            case 2:
                img.setImageResource(R.drawable.spinner_hib);
                break;
        }

        txt.setText((CharSequence) lista.get(position));

        return convertView;
    }
}
