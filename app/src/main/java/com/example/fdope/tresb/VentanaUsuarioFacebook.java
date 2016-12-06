package com.example.fdope.tresb;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.ventana_usuarioface, container, false);
        super.onCreate(savedInstanceState);

        mCallBack = (InfoPostDialog) getActivity();

        username = (TextView)rootView.findViewById(R.id.usuarioFacebook);
        ingresar = (Button) rootView.findViewById(R.id.botonIngresar);
        salir = (Button)rootView.findViewById(R.id.bye) ;
        username.getText().toString();
        username.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mCallBack.postIngresarUsuarioFacebook(username.getText().toString());
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

    public void checkFieldsForEmptyValues() {
        String nombre = this.username.getText().toString();

        if (nombre.equals("")) {
            ingresar.setEnabled(false);
        } else {
            ingresar.setEnabled(true);
        }
    }
}
