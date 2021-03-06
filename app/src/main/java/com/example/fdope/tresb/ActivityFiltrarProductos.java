package com.example.fdope.tresb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Filtro;
import com.example.fdope.tresb.DB.ConsultasProductos;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ActivityFiltrarProductos extends AppCompatActivity {
    private EditText inputPrecioMin , inputPrecioMax;
    private Spinner spinnerTipo, spinnerMarca;
    public static final int FILTRO_OK=2;
    private ArrayAdapter<String> adapterListaMarcas;
    private ArrayList<String> listaMarcas;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaMarcas= new ArrayList<String>();
        setContentView(R.layout.activity_filtrar_productos);
        inputPrecioMax = (EditText) findViewById(R.id.inputpreciomaximo);
        inputPrecioMin = (EditText) findViewById(R.id.inputpreciominimo);
        spinnerMarca = (Spinner) findViewById(R.id.spinnermarca);
        spinnerTipo = (Spinner) findViewById(R.id.spinnertipo);

        listaMarcas= ConsultasProductos.listarMarcas();
        adapterListaMarcas = new ArrayAdapter<String>(ActivityFiltrarProductos.this,R.layout.spinner_item,R.id.item,listaMarcas);
        spinnerMarca.setAdapter(adapterListaMarcas);

        inputPrecioMin.addTextChangedListener(mTextWatcher);
        inputPrecioMax.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
    }
    public void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.filtrar);

        String precioMax = this.inputPrecioMax.getText().toString();   //Método que valida si los campos del login están vacios o no
        String precioMin = this.inputPrecioMin.getText().toString();

        if (precioMax.equals("") || precioMin.equals("")) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
        }
    }

    public void  enviarFiltros (View v){
        String marca = this.spinnerMarca.getSelectedItem().toString();
        String tipo = this.spinnerTipo.getSelectedItem().toString();
        String precioMin = this.inputPrecioMin.getText().toString();
        String precioMax= this.inputPrecioMax.getText().toString();
        nextActivity(marca,tipo,precioMax,precioMin);

    }

    public void nextActivity(String marca, String tipo, String precioMax, String precioMin) {
        Filtro filtro = new Filtro(marca,tipo,precioMax,precioMin);
        Intent intent = getIntent();
        intent.putExtra("filtro",filtro);
        setResult(FILTRO_OK,intent);
        finish();
    }
}