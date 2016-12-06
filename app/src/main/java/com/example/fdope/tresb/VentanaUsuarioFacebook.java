package com.example.fdope.tresb;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.DB.ConsultasUsuarios;

/**
 * Created by SS on 05-12-2016.
 */

public class VentanaUsuarioFacebook extends DialogFragment {

    private TextView username;
    private Button ingresar,salir;
    private InfoPostDialog mCallBack;
    private String nombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.ventana_usuarioface, container, false);
        super.onCreate(savedInstanceState);

        mCallBack = (InfoPostDialog) getActivity();

        username = (TextView)rootView.findViewById(R.id.usuarioFacebook);
        ingresar = (Button) rootView.findViewById(R.id.botonIngresar);
        salir = (Button)rootView.findViewById(R.id.bye) ;

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombre = username.getText().toString();
                if (!nombre.equals(""))
                    mCallBack.postIngresarUsuarioFacebook(nombre);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.salir(true);
                dismiss();
            }
        });
        return rootView;
    }
}
