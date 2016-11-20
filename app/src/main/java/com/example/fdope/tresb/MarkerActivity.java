package com.example.fdope.tresb;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

public class MarkerActivity extends DialogFragment {
    public String titulo,info;
    public boolean flag;
    FloatingActionButton botonFav,botonComentarios, botonDenuncia;
    private EnviarFlagFavorito mCallback;
    public int idEvento;
    private String username;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (EnviarFlagFavorito) activity;
        }
        catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_marker, container, false);
        super.onCreate(savedInstanceState);

        TextView infoProd = (TextView) rootView.findViewById(R.id.infoProd);
        TextView prodsnippet = (TextView) rootView.findViewById(R.id.prod_snippet);
        ImageView mImageView = (ImageView) rootView.findViewById(R.id.img);

        Button salir = (Button) rootView.findViewById(R.id.botonSalir) ;
        username= getArguments().getString("username");
        titulo = getArguments().getString("titulo");
        info = getArguments().getString("info");
        byte[] b = getArguments().getByteArray("img");
        flag = getArguments().getBoolean("flag");
        idEvento=getArguments().getInt("idEvento");
        Bitmap bmp=null;
        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        mImageView.setImageBitmap(bmp);
        botonFav = (FloatingActionButton) rootView.findViewById(R.id.favorito);


        if (flag)
            botonFav.setImageResource(R.drawable.ic_star_black_24dp);
        else
            botonFav.setImageResource(R.drawable.ic_star_border_black_24dp);

        botonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==false) {
                    botonFav.setImageResource(R.drawable.ic_star_black_24dp);
                    flag=true;
                    mCallback.onFinishDialog(flag);
                    //dismiss();
                }
                else{
                    botonFav.setImageResource(R.drawable.ic_star_border_black_24dp);
                    flag=false;
                    mCallback.onFinishDialog(flag);
                    //dismiss();
                }
            }
        });

        botonComentarios= (FloatingActionButton)rootView.findViewById(R.id.comentarios);

        botonComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListViewComentarios.class);
                intent.putExtra("idEvento",idEvento);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        infoProd.setText(titulo);
        infoProd.setTextColor(Color.BLACK);
        infoProd.setGravity(Gravity.CENTER);
        infoProd.setTypeface(null, Typeface.BOLD);
        prodsnippet.setText(info);

        salir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

}
