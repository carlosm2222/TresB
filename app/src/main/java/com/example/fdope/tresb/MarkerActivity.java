package com.example.fdope.tresb;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MarkerActivity extends DialogFragment {
    public String titulo,info;
    public boolean flag;
    public CheckBox fav;

    public interface EnviarFlagFavorito {
        void onFinishDialog(boolean flag);
    }

    static MarkerActivity newInstance() {
        return new MarkerActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_marker, container, false);
        super.onCreate(savedInstanceState);

        TextView infoProd = (TextView) rootView.findViewById(R.id.infoProd);
        TextView prodsnippet = (TextView) rootView.findViewById(R.id.prod_snippet);
        ImageView mImageView = (ImageView) rootView.findViewById(R.id.imagenProd);
        fav = (CheckBox) rootView.findViewById(R.id.checkBoxFavorito);
        Button salir = (Button) rootView.findViewById(R.id.botonSalir) ;

        titulo = getArguments().getString("titulo");
        info = getArguments().getString("info");
        byte[] b = getArguments().getByteArray("img");
        flag = getArguments().getBoolean("flag");
        Bitmap bmp=null;
        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        mImageView.setImageBitmap(bmp);

        infoProd.setText(titulo);
        infoProd.setTextColor(Color.BLACK);
        infoProd.setGravity(Gravity.CENTER);
        infoProd.setTypeface(null, Typeface.BOLD);
        prodsnippet.setText(info);

        if (flag)
            fav.setChecked(true);
        else
            fav.setChecked(false);

        fav.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                flag= fav.isChecked();
                EnviarFlagFavorito activity = (EnviarFlagFavorito) getActivity();
                activity.onFinishDialog(flag);
                //dismiss();
/*
                Bundle bundle = new Bundle();
                bundle.putBoolean("res",flag);
                setArguments(bundle);



                Intent intent = new Intent(getActivity(),MapsActivity.class);
                intent.putExtra("res",flag);
                startActivity(intent);

*/
            }
        });

        salir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

}
