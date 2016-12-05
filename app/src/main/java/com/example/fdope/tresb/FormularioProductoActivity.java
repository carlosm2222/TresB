package com.example.fdope.tresb;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.fdope.tresb.FactoriaProductos.Celular;
import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.example.fdope.tresb.FactoriaProductos.ProductosFactory;
import com.example.fdope.tresb.FactoriaProductos.FactoriaElectronica;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.sql.SQLException;

public class FormularioProductoActivity extends AppCompatActivity {
    private Bitmap mImageBitmap;
    private ImageView mImageView;
    private EditText inputModelo, inputPrecio , inputProveedor;
    private Spinner spinnerTipo, spinnerMarca;
    static  final  int request_code = 1;
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
        setContentView(R.layout.activity_formulario);

        this.inputModelo = (EditText) findViewById(R.id.inputmodelo);
        this.inputModelo.addTextChangedListener(mTextWatcher);
        this.inputPrecio = (EditText) findViewById(R.id.inputprecio);
        this.inputPrecio.addTextChangedListener(mTextWatcher);
        this.spinnerMarca = (Spinner) findViewById(R.id.spinnermarca);
        this.spinnerTipo = (Spinner) findViewById(R.id.spinnertipo);
        this.inputProveedor = (EditText) findViewById(R.id.inputproveedor);
        this.inputProveedor.addTextChangedListener(mTextWatcher);
        mImageView = (ImageView) findViewById(R.id.imageView2);
        checkFieldsForEmptyValues();

    }

    public void agregarProducto(View v)throws IOException,SQLException{

        Location location = getIntent().getParcelableExtra("coordenadas");
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        String tipo = this.spinnerTipo.getSelectedItem().toString();
        String marca = this.spinnerMarca.getSelectedItem().toString();
        String modelo= this.inputModelo.getText().toString();
        int precio = Integer.parseInt(this.inputPrecio.getText().toString());
        String proveedor = this.inputProveedor.getText().toString();
        String usuario = getIntent().getStringExtra("usuario");

        //USO DE FACTORIA
        usarFactoria(usuario,tipo,marca,modelo,precio,proveedor,latLng,mImageBitmap);
    }

    public  void usarFactoria(String usuario,String nombre_categoria, String marca, String modelo, int precio, String proveedor,LatLng latLng,Bitmap img)throws IOException,SQLException{

        if (nombre_categoria.equals("Smartphone")){ /// FACTORIA SMARTPHONE

            ProductosFactory pf = new FactoriaElectronica();
            Producto producto = pf.crearProducto(usuario,nombre_categoria,marca,modelo,precio,proveedor,latLng,img);
            pf.guardarProductoBD(producto);
            if ( producto !=null){
                mostrarMensaje();
                Intent intent = new Intent();
                intent.putExtra("productoOut",(Celular)producto);
                setResult(RESULT_OK,intent);
            }
            else {
                Toast.makeText(this,"Error, no se pudo guardar.", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        /// OTRAS FACTORIAS ...
    }

    private void mostrarMensaje() {
        FragmentManager fm = getFragmentManager();
        MyDialogFragment dialogFragment = new MyDialogFragment ();
        dialogFragment.show(fm, "Sample Fragment");
    }


    public void takePicture (View v){
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,request_code);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_code) {
            switch (resultCode) {
                case (RESULT_OK): {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        mImageBitmap = (Bitmap)bundle.get("data");
                        mImageView.setImageBitmap(mImageBitmap);
                        checkFieldsForEmptyValues();
                    }
                }
            }
        }
    }

    public void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.agregar);

        String modelo   = this.inputModelo.getText().toString();   //Método que valida si los campos del login están vacios o no
        String precio = this.inputPrecio.getText().toString();
        String proveedor =  this.inputProveedor.getText().toString();
        if (modelo.equals("") || precio.equals("")|| proveedor.equals("") || mImageBitmap==null) {
            b.setEnabled(false);
        } else
            b.setEnabled(true);
    }
}
