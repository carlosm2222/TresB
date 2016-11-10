package com.example.fdope.tresb.Factoria;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.example.fdope.tresb.R;

public class MarkerActivity extends DialogFragment {
    ConsultasUsuarios consultasUsuarios;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_marker, container, false);
        super.onCreate(savedInstanceState);

        TextView infoProd = (TextView) rootView.findViewById(R.id.infoProd);
        TextView prodsnippet = (TextView) rootView.findViewById(R.id.prod_snippet);
        ImageView mImageView = (ImageView) rootView.findViewById(R.id.imagenProd);
        CheckBox fav = (CheckBox) rootView.findViewById(R.id.checkBoxFavorito);
        Button salir = (Button) rootView.findViewById(R.id.botonSalir) ;

        String titulo = getArguments().getString("titulo");
        String info = getArguments().getString("info");
        byte[] b = getArguments().getByteArray("img");
        Bitmap bmp=null;
        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        mImageView.setImageBitmap(bmp);

        infoProd.setText(titulo);
        infoProd.setTextColor(Color.BLACK);
        infoProd.setGravity(Gravity.CENTER);
        infoProd.setTypeface(null, Typeface.BOLD);
        prodsnippet.setText(info);


        fav.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        salir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }

    ///FALTA IMPLEMENTAR
    public void agregarFavorito(){
        consultasUsuarios.agregarFav();

    }
    public void eliminarFav(){
        consultasUsuarios.eliminarFav();
    }

}
