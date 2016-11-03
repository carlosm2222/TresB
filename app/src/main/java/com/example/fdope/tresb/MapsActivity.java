package com.example.fdope.tresb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fdope.tresb.Clases.TresB;
import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.Factoria.Producto;
import com.example.fdope.tresb.Clases.Usuario;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Marker marcador;
    private ArrayList<Marker> listaMarcadorProductos;
    private GoogleMap mMap;
    double lat;
    double lng;
    TresB app ;
    static  final  int request_code = 1;
    private Producto p;
    private Usuario usuario;
    ImageView mImageView;
    String firstName;
    Bitmap bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle inBundle = getIntent().getExtras();//agregar desde aqui
        if (inBundle!=null){
            this.firstName = inBundle.getString("first_name");
            String lastName = inBundle.getString("last_name");

            Toast.makeText(this,"Bienvenido "+firstName,Toast.LENGTH_SHORT).show();
            //usuario = new Usuario(name,surname);
        }//hasta aqui
        this.app = new TresB();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap!=null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.ventanainfoprouctos,null);
                    TextView infoProd = (TextView) view.findViewById(R.id.infoProd);
                    TextView prodsnippet = (TextView) view.findViewById(R.id.prod_snippet);
                    mImageView = (ImageView) findViewById(R.id.imagenProd);

                    infoProd.setTextColor(Color.BLACK);
                    infoProd.setGravity(Gravity.CENTER);
                    infoProd.setTypeface(null, Typeface.BOLD);
                    infoProd.setText(marker.getTitle());
                    prodsnippet.setText(marker.getSnippet());
                    Bitmap bmp=null;

                    if (marker.getTitle().equals(firstName) )
                        mImageView=null;
                    else {
                        for (int i = 0; i < app.getListaProductos().size(); i++) {
                            if (app.getListaProductos().get(i).mostrarInfoProducto().equals(infoProd)) {
                                byte[] b = app.getListaProductos().get(i).mostrarImagen();
                                bmp = BitmapFactory.decodeByteArray(b, 100, b.length);
                                if (bmp!=null) {
                                    mImageView.setImageBitmap(bmp);
                                    System.out.printf("NO ES NULL");
                                }
                                else
                                    System.out.printf("NULL");
                            }
                        }
                    }

                    return view;
                }
            });
        }




        miUbicacion();
        //cargarDatos();
    }

    public  void autoRefresh(){
        this.app.getListaProductos().clear();
        this.app.setListaSmartphone(null);
        this.listaMarcadorProductos = null;
        mMap.clear();
        miUbicacion();
        cargarDatos();
    }

    public  void manualRefresh(View view){

        Toast.makeText(this," Actualizando mapa ",Toast.LENGTH_LONG).show();
        this.app.getListaProductos().clear();
        this.app.setListaSmartphone(null);
        this.listaMarcadorProductos = null;
        mMap.clear();
        miUbicacion();
        cargarDatos();
    }

    public void formulario(View view) {
        Intent intent = new Intent(this, FormularioActivity.class);
        Location location = obetnerUbicacion();
        intent.putExtra("coordenadas",location);
        intent.putExtra("usuario",firstName);
        startActivityForResult(intent,request_code);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_code) {
            switch (resultCode) {
                case (RESULT_OK): {
                    if (data != null) {
                        p = data.getExtras().getParcelable("productoOut");
                        this.app.addProducto(p);
                        Bundle bundle = data.getExtras();
                        this.bp = (Bitmap)bundle.get("imagen");
                        if (this.bp == null)
                            Toast.makeText(this,"no existe la foto.",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this,"SI existe la foto.",Toast.LENGTH_SHORT).show();
                        agregarMarcadorProductos(p);
                        Toast.makeText(this,"Agregado al mapa correctamente.",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(this,"No se a podido agregar el producto",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Location obetnerUbicacion() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        return location;
    }

    public void agregarMarcado(Double lat, Double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null)
            marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().
                position(coordenadas).
                title(this.firstName).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.yop)));
        mMap.animateCamera(miUbicacion);

    }


    public void agregarMarcadorProductos(Producto p){

        //Marker marcadorProducto = null;
        //marcadorProducto=
                mMap.addMarker(new MarkerOptions().
                position(p.coordenadasProducto()).
                title(p.mostrarInfoProducto()).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.pinoferta)).
                snippet(p.mostrarPrecio()));
        if (bp!=null){
            Toast.makeText(this,"IMAGEN ENTRA A MARCADOR",Toast.LENGTH_SHORT).show();
//            mImageView.setImageBitmap(bp);
        }

/*
        if(this.listaMarcadorProductos == null) {
            this.listaMarcadorProductos = new ArrayList<Marker>();
            this.listaMarcadorProductos.add(marcadorProducto);
        }
        else
            this.listaMarcadorProductos.add(marcadorProducto);
*/
    }



    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcado(lat, lng);
        }
    }


    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,150000,0,locListener);
    }


    public void cargarDatos() {

        ConsultasProductos consulta = new ConsultasProductos();

        try {
            // obtendo la lista de productos de la bd
            this.app.setListaSmartphone(consulta.listarProductos());
        } catch (Exception e) {
            e.getMessage();
        }

        if (this.app.getListaProductos()!=null)
            for (int i = 0; i < this.app.getListaProductos().size(); i++)
                agregarMarcadorProductos(app.getListaProductos().get(i));
        else
            Toast.makeText(this,"NO SE PUDE OBTENER LISTA",Toast.LENGTH_SHORT).show();
    }


    //AGREGA ESTE MÉTODO A TU CODIGO. COPIA EL LAYOUR DE ESTA ACTIVIDAD
    public void logout(View view){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}







