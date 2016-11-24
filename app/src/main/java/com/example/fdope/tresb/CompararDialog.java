package com.example.fdope.tresb;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SS on 23-11-2016.
 */

public class CompararDialog extends DialogFragment {
    private String marca1,marca2,modelo1,modelo2,tienda1,tienda2,precio1,precio2;
    private ImageView img1,img2;
    private Bitmap bmp1,bmp2;
    private byte[] b1,b2;
    private InfoPostDialog mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (InfoPostDialog) activity;
        }
        catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_marker, container, false);
        super.onCreate(savedInstanceState);

        TextView mar1 = (TextView)rootView.findViewById(R.id.marca1);
        TextView mar2 = (TextView)rootView.findViewById(R.id.marca2);
        TextView mod1 = (TextView)rootView.findViewById(R.id.modelo1);
        TextView mod2 = (TextView)rootView.findViewById(R.id.modelo2);
        TextView pre1 = (TextView)rootView.findViewById(R.id.precio1);
        TextView pre2= (TextView)rootView.findViewById(R.id.precio2);
        TextView prov1 = (TextView)rootView.findViewById(R.id.tienda1);
        TextView prov2 = (TextView)rootView.findViewById(R.id.tienda2);
        img1 = (ImageView)rootView.findViewById(R.id.img1);
        img2 = (ImageView)rootView.findViewById(R.id.img2);
        Button salir = (Button)rootView.findViewById(R.id.salirComparar);

        marca1 = getArguments().getString("marca1");
        marca2= getArguments().getString("marca2");
        modelo1 = getArguments().getString("modelo1");
        modelo2 = getArguments().getString("modelo2");
        precio1 = getArguments().getString("precio1");
        precio2= getArguments().getString("precio2");
        tienda1= getArguments().getString("tienda1");
        tienda2 = getArguments().getString("tienda2");
        b1 = getArguments().getByteArray("img1");
        b2 = getArguments().getByteArray("img2");

        mar1.setText(marca1);
        mar2.setText(marca2);
        mod1.setText(modelo1);
        mod2.setText(modelo2);
        pre1.setText(precio1);
        pre2.setText(precio2);
        prov1.setText(tienda1);
        prov2.setText(tienda2);
        bmp1 = BitmapFactory.decodeByteArray(b1, 0, b1.length);
        bmp2=BitmapFactory.decodeByteArray(b2, 0, b2.length);
        img1.setImageBitmap(bmp1);
        img2.setImageBitmap(bmp2);

        salir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return rootView;
    }
}
