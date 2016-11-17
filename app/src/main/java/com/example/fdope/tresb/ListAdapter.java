package com.example.fdope.tresb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.Factoria.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SS on 15-11-2016.
 */

public class ListAdapter extends ArrayAdapter<Producto> {

    public Activity context;
    private ArrayList<Producto> listaProd;

    public ListAdapter(Activity context, int resource, ArrayList<Producto> listaProd) {
        super(context, resource);
        this.context=context;
        this.listaProd = listaProd;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (convertView == null)
        {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_fila, null);
        }

        Producto p = listaProd.get(position);

        Bitmap bpm = BitmapFactory.decodeByteArray(p.mostrarImagen(), 0, p.mostrarImagen().length);
        ImageView imagen = (ImageView) rowView.findViewById(R.id.img);
        TextView titulo = (TextView)rowView.findViewById(R.id.titulo);
        TextView precio = (TextView)rowView.findViewById(R.id.precio);
        TextView tienda = (TextView) rowView.findViewById(R.id.tienda);
        TextView usuario = (TextView) rowView.findViewById(R.id.usuario);

        imagen.setImageBitmap(bpm);
        String tit = p.mostrarMarca() + " "+p.mostrarmodelo();
        titulo.setText(tit);
        precio.setText(p.mostrarPrecio());
        tienda.setText(p.mostrarProveedor());
        usuario.setText(p.mostrarCreadorPublicacion());

        return rowView;
    }
}
