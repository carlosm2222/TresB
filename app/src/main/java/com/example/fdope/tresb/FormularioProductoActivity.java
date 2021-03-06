package com.example.fdope.tresb;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.FactoriaProductos.Celular;
import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.example.fdope.tresb.FactoriaProductos.ProductosFactory;
import com.example.fdope.tresb.FactoriaProductos.FactoriaElectronica;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FormularioProductoActivity extends AppCompatActivity  {
    private Bitmap mImageBitmap;
    private ImageView mImageView;
    private EditText inputPrecio , inputProveedor;
    private Spinner spinnerTipo, spinnerMarca,spinnerModelo;
    static  final  int request_code = 1;
    private ArrayList<String> listaModelos;
    private ArrayList<String>  listaMarcas;
    private ArrayList<String>  listaCategorias;
    private ArrayAdapter<String> adapterListaMarcas,adapterListaModelos,adapterListaCategorias;

    static  final int CAMERA_PERMISSION_CODE = 23;

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
        listaMarcas =new  ArrayList<String>();
        listaCategorias =new  ArrayList<String>();
        listaModelos= new ArrayList<String>();

        this.inputPrecio = (EditText) findViewById(R.id.inputprecio);
        this.inputPrecio.addTextChangedListener(mTextWatcher);
        this.spinnerMarca = (Spinner) findViewById(R.id.spinnermarca);
        this.spinnerTipo = (Spinner) findViewById(R.id.spinnertipo);
        this.inputProveedor = (EditText) findViewById(R.id.inputproveedor);
        this.spinnerModelo = (Spinner) findViewById(R.id.spinnermodelo);
        this.inputProveedor.addTextChangedListener(mTextWatcher);
        mImageView = (ImageView) findViewById(R.id.imageView2);

        final String tipo = spinnerTipo.getSelectedItem().toString();
                if (!tipo.equals("")){

                    listaMarcas=ConsultasProductos.listarMarcas();
                    adapterListaMarcas = new ArrayAdapter<String>(FormularioProductoActivity.this,R.layout.spinner_item,R.id.item,listaMarcas);
                    spinnerMarca.setAdapter(adapterListaMarcas);

                    spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String marca = spinnerMarca.getSelectedItem().toString();
                            listaModelos = ConsultasProductos.listarModelos(marca,tipo);
                            adapterListaModelos = new ArrayAdapter<String>(FormularioProductoActivity.this,R.layout.spinner_item,R.id.item,listaModelos);
                            spinnerModelo.setAdapter(adapterListaModelos);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

        checkFieldsForEmptyValues();

    }

    public void agregarProducto(View v)throws IOException,SQLException{

        Location location = getIntent().getParcelableExtra("coordenadas");
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        String tipo = this.spinnerTipo.getSelectedItem().toString();
        String marca = this.spinnerMarca.getSelectedItem().toString();
        String modelo= this.spinnerModelo.getSelectedItem().toString();
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
        if(isCameraAccessAllowed()){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent,request_code);
        }else
            requestCameraPermission();
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

        String precio = this.inputPrecio.getText().toString();
        String proveedor =  this.inputProveedor.getText().toString();
        if (precio.equals("")|| proveedor.equals("") || mImageBitmap==null) {
            b.setEnabled(false);
        } else
            b.setEnabled(true);
    }

    //We are calling this method to check the permission status
    private boolean isCameraAccessAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestCameraPermission(){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}


