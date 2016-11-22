package com.example.fdope.tresb.Clases;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.Factoria.Producto;
import com.example.fdope.tresb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fdope on 17-11-2016.
 */

public class ComentarioAdapter extends ArrayAdapter<Comentario> {
    private Context context;
    private List<Comentario> items;

    public ComentarioAdapter(Activity context, int resource, ArrayList<Comentario> listaProd) {
        super(context, resource);
        this.context=context;
        this.items = listaProd;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Comentario getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (convertView == null)
        {            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.lista_comentarios, parent,false);
        }

        Comentario c = items.get(position);

        TextView usuario = (TextView)rowView.findViewById(R.id.usuario);
        TextView fecha = (TextView)rowView.findViewById(R.id.fecha);
        TextView comentario = (TextView)rowView.findViewById(R.id.comentario);
        ImageView like = (ImageView)rowView.findViewById(R.id.like);
        usuario.setText("Publicado por: "+c.getUsuario());
        fecha.setText("Fecha de publicación: "+c.getFechaPublicacion());
        comentario.setText(c.getComentario());
        if(c.isLike()){
            like.setImageResource(R.mipmap.ic_like);

        }else{
            like.setImageResource(R.mipmap.ic_dislike);
        }
        return rowView;
    }
}
