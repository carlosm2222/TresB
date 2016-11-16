package com.example.fdope.tresb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.Factoria.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SS on 15-11-2016.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Producto> listaProd;

    public ListAdapter(Context context, ArrayList<Producto> listaProd) {
        this.context = context;
        this.listaProd = listaProd;
    }


    @Override
    public int getCount() {
        return listaProd.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listaProd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (convertView == null)
        {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_fila, parent, false);
        }

        Bitmap bpm = BitmapFactory.decodeByteArray(listaProd.get(position).mostrarImagen(), 0, listaProd.get(position).mostrarImagen().length);
        ImageView imagen = (ImageView) rowView.findViewById(R.id.img);
        TextView titulo = (TextView)rowView.findViewById(R.id.titulo);
        TextView precio = (TextView)rowView.findViewById(R.id.precio);
        TextView tienda = (TextView) rowView.findViewById(R.id.tienda);
        TextView usuario = (TextView) rowView.findViewById(R.id.usuario);

        imagen.setImageBitmap(bpm);
        titulo.setText(listaProd.get(position).mostrarMarca() + " "+listaProd.get(position).mostrarmodelo());
        precio.setText(listaProd.get(position).mostrarPrecio());
        tienda.setText(listaProd.get(position).mostrarProveedor());
        usuario.setText(listaProd.get(position).mostrarCreadorPublicacion());

        return rowView;
    }
}
