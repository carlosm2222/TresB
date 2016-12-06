package com.example.fdope.tresb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.FactoriaProductos.Producto;

import java.util.ArrayList;

/**
 * Created by SS on 21-11-2016.
 */

public class ListAdapterInicio extends ArrayAdapter<Producto> {
    public Activity context;
    private ArrayList<Producto> listaProd;
    private EnviarInfoDesdeListsAdapter mCallBack;


    public ListAdapterInicio(Activity context, int resource, ArrayList<Producto> listaProd) {
        super(context, resource);
        this.context=context;
        this.listaProd = listaProd;
        mCallBack=(EnviarInfoDesdeListsAdapter)context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return this.listaProd.size();
    }

    @Override
    public Producto getItem(int position) {
        return this.listaProd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (convertView == null)
        {            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_fila, parent,false);
        }

        Producto p = listaProd.get(position);

        Bitmap bpm = BitmapFactory.decodeByteArray(p.mostrarImagen(), 0, p.mostrarImagen().length);
        ImageView imagen = (ImageView) rowView.findViewById(R.id.img);
        TextView titulo = (TextView)rowView.findViewById(R.id.titulo);
        TextView precio = (TextView)rowView.findViewById(R.id.precio);
        TextView tienda = (TextView) rowView.findViewById(R.id.tienda);
        TextView usuario = (TextView) rowView.findViewById(R.id.usuario);
        ImageButton map = (ImageButton) rowView.findViewById(R.id.botonmapa);

        imagen.setImageBitmap(bpm);
        String tit = p.mostrarMarca() + " "+p.mostrarmodelo();
        titulo.setText(tit);
        String preci = "$"+p.mostrarPrecio()+" CLP";
        precio.setText(preci);
        String prov = "Tienda: "+p.mostrarProveedor();
        tienda.setText(prov);
        String usr = "Publicado por: "+p.mostrarCreadorPublicacion();
        usuario.setText(usr);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.recibirCoordenadas(listaProd.get(position).coordenadasProducto());
            }
        });


        return rowView;
    }
}
