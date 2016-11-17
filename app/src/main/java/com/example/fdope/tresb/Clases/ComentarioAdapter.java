package com.example.fdope.tresb.Clases;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.R;

import java.util.List;

/**
 * Created by fdope on 17-11-2016.
 */

public class ComentarioAdapter extends BaseAdapter {
    private Context context;
    private List<Comentario> items;

    public ComentarioAdapter(Context context, List<Comentario> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
/*
        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.lista_comentarios, parent, false);
        }

        // Set data into the view.

        TextView comentario = (TextView) rowView.findViewById(R.id.comentario);
        TextView publicadopor = (TextView) rowView.findViewById(R.id.publicadopor);

        Comentario c = this.items.get(position);
        comentario.setText(c.getComentario());
        publicadopor.setText(c.getUsuario());
*/
        return rowView;
    }
}
