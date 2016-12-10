package com.example.fdope.tresb;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.github.clans.fab.FloatingActionButton;

public class MarkerActivity extends DialogFragment {
    public String titulo,info;
    public boolean flag;
    FloatingActionButton botonFav,botonComentarios, botonDenuncia;
    private InfoPostDialog mCallback;
    public int idEvento;
    private String username;
    CheckBox comparar;
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

        TextView infoProd = (TextView) rootView.findViewById(R.id.infoProd);
        TextView prodsnippet = (TextView) rootView.findViewById(R.id.prod_snippet);
        ImageView mImageView = (ImageView) rootView.findViewById(R.id.img);
        comparar=(CheckBox)rootView.findViewById(R.id.checkboxCom);

        Button salir = (Button) rootView.findViewById(R.id.botonSalir) ;
        username= getArguments().getString("username");
        titulo = getArguments().getString("titulo");
        info = getArguments().getString("info");
        byte[] b = getArguments().getByteArray("img");
        flag = getArguments().getBoolean("flag");
        idEvento=getArguments().getInt("idEvento");
        boolean flagComparar = getArguments().getBoolean("flagCheckbox");
        Bitmap bmp=null;
        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        mImageView.setImageBitmap(bmp);
        botonFav = (FloatingActionButton) rootView.findViewById(R.id.favorito);
        botonDenuncia = (FloatingActionButton) rootView.findViewById(R.id.denunciar);
       
        if (flag)
            botonFav.setImageResource(R.drawable.ic_star_black_24dp);
        else
            botonFav.setImageResource(R.drawable.ic_star_border_black_24dp);

        botonFav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag==false)
                    Toast.makeText(getActivity(), "Agregar a favoritos", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Eliminar de favoritos", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        botonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==false) {
                    botonFav.setImageResource(R.drawable.ic_star_black_24dp);
                    flag=true;
                    mCallback.onFinishDialogFavorito(flag);
                }
                else{
                    botonFav.setImageResource(R.drawable.ic_star_border_black_24dp);
                    flag=false;
                    mCallback.onFinishDialogFavorito(flag);
                }
            }
        });

        botonDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DenunciarActivity.class);
                intent.putExtra("idEvento",idEvento);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        botonDenuncia.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Denunciar", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        botonComentarios= (FloatingActionButton)rootView.findViewById(R.id.comentarios);
        botonComentarios.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Comentarios", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        botonComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "", "Cargando comentarios ...", true);
                ringProgressDialog.setCancelable(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // Here you should write your time consuming task...
                            Intent intent = new Intent(getActivity(),ListViewComentarios.class);
                            intent.putExtra("idEvento",idEvento);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            // Let the progress ring for 10 seconds...
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                        ringProgressDialog.dismiss();
                    }
                }).start();
            }
        });

        if (flagComparar)
            comparar.setChecked(true);
        else
            comparar.setChecked(false);

        comparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mCallback.onFinishDialogComparar(comparar.isChecked());
                dismiss();
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
