package com.example.fdope.tresb;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;
import com.example.fdope.tresb.Clases.TresB;
import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.example.fdope.tresb.Factoria.Producto;
import com.example.fdope.tresb.Clases.Usuario;
import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.fdope.tresb.ActivityFiltrarProductos.FILTRO_OK;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,EnviarFlagFavorito {
    private GoogleMap mMap;
    private double lat;
    private double lng;
    private TresB app;
    static final int request_code = 1;
    static final int request_code_filtro = 2;
    static final int NOTIFICACION_ID=5;
    private Producto p,prodMomentaneo;
    private Usuario usuario;
    boolean isOpen = false;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3,floatingActionButton4;
    private int flagfav=0; /// flag= true es favorito , false no es favorito



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.app = new TresB();

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                formulario(v);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filtrarProducto(v);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout(v);

            }
        });

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaFav(v);

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Creamos el Timer
        Timer timer = new Timer();
        //Que actue cada 2 minutos
        //Empezando des de el segundo 30seg iniciado el mapa
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //La función a ejecutar
                autoRefresh();
               //autoNotificaciones();

            }
        }, 30000, 120000);


        Bundle inBundle = getIntent().getExtras();//agregar desde aqui
        if (inBundle != null) {
            usuario = inBundle.getParcelable("UsuarioIn");
            if (usuario!=null)
                Toast.makeText(this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "NOSE PUDO OBTENER USUARIIO ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        cargarDatos();
        miUbicacion();
        obtenerFavs();
       // usuario.setNotificaciones(buscarProdParaNotificar());


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bitmap bmp=null;
                byte[] b=null;
                boolean fav=false; /// flag= true es favorito , false no es favorito
                Producto prodFav=null;
                if (app.getListaProductos()!=null)
                    for (int i = 0; i < app.getListaProductos().size(); i++) {

                        String titulo  = app.getListaProductos().get(i).mostrarMarca()+" "+app.getListaProductos().get(i).mostrarmodelo();
                        String spin = "$ "+app.getListaProductos().get(i).mostrarPrecio()+" CLP en Tienda: "+app.getListaProductos().get(i).mostrarProveedor()+". Publicado por: "+app.getListaProductos().get(i).mostrarCreadorPublicacion();
                        int idEvento = app.getListaProductos().get(i).mostrarIdEvento();

                        if (titulo.equals(marker.getTitle()) && spin.equals(marker.getSnippet())) {

                            prodMomentaneo=app.getListaProductos().get(i);
                            b = app.getListaProductos().get(i).mostrarImagen();// se obtiene la imagen del producto
                            prodFav = buscarFav(app.getListaProductos().get(i)); // SE OBTIENE PRODUCTO FAVORITO SI ESQUE EXISTE
                            if (prodFav!=null) // SE SETEA EL FLAG A TRUE SI EXISTE FAV
                                fav = true;

                            if (fav==false && flagfav==1) {
                                mostrarMensaje(marker.getTitle(),marker.getSnippet(),b,true, idEvento); // se envia el titulo y el snippet del marcador ala ventana del pin con la foto y el FLAG de favorito
                                flagfav = 0;
                                break;
                            }
                            if (flagfav==2 && fav==true){
                                mostrarMensaje(marker.getTitle(),marker.getSnippet(),b,false, idEvento);
                                flagfav = 0;
                                break;
                            }
                            mostrarMensaje(marker.getTitle(),marker.getSnippet(),b,fav,idEvento); // se envia el titulo y el snippet del marcador ala ventana del pin con la foto y el FLAG de favorito
                            flagfav = 0;
                            break;
                        }
                    }
                CameraUpdate ubicacionpin= CameraUpdateFactory.newLatLngZoom(marker.getPosition(),20);
                mMap.animateCamera(ubicacionpin);
                    return true;
                }
            });

    }


    @Override // RECIBE EL ESTADO DEL CHECKBOX FAVORITOS
    public void onFinishDialog(boolean flag) {
        // flag 0 neutro , 1 true,2 false
        if (flagfav == 0 && flag==true){ /// SI NO ERA FAVORITO
            Toast.makeText(this,"Agregado a favorito",Toast.LENGTH_SHORT).show();
            agregarFavorito(prodMomentaneo);
        }
        if(flag==false)
        {
            Toast.makeText(this,"Eliminado de favorito",Toast.LENGTH_SHORT).show();
            eliminarFavorito(prodMomentaneo);
            flagfav=0;
        }
    }

    private void mostrarMensaje(String titulo, String info, byte[] img, boolean flag, int idEvento) {
        FragmentManager fm = getFragmentManager();
        MarkerActivity dialogFragment = new MarkerActivity ();
        Bundle bundle = new Bundle();
        bundle.putString("titulo",titulo);
        bundle.putString("info",info);
        bundle.putString("username",usuario.getUsername());
        bundle.putInt("idEvento",idEvento);
        bundle.putByteArray("img",img);
        bundle.putBoolean("flag",flag);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Sample Fragment");
    }

    public void manualRefresh(View view) {
        mMap.clear();
        Toast.makeText(this, " Actualizando mapa ", Toast.LENGTH_LONG).show();
        this.app.getListaProductos().clear();
        this.app.setListaSmartphone(null);
        miUbicacion();
        cargarDatos();
    }

    public void formulario(View view) {
        Intent intent = new Intent(this, FormularioActivity.class);
        Location location = obetnerUbicacion();
        intent.putExtra("coordenadas", location);
        intent.putExtra("usuario",usuario.getUsername());

        startActivityForResult(intent, request_code);
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
                        agregarMarcadorProductos(p);
                        Toast.makeText(this, "Agregado al mapa correctamente.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == request_code_filtro) {
            switch (requestCode) {
                case (FILTRO_OK): {
                    if (data!=null) {
                        Filtro filtro = data.getExtras().getParcelable("filtro");
                        filtrarProductos(filtro);
                    }
                }
            }
        }
    }

    private void filtrarProductos(Filtro filtro) {
        if (this.app.getListaProductos() != null) {
            mMap.clear();
            for (int i = 0; i < this.app.getListaProductos().size(); i++) {
                if (comparar(app.getListaProductos().get(i), filtro))
                    agregarMarcadorProductos(app.getListaProductos().get(i));
            }
        }
    }


    private boolean comparar(Producto producto, Filtro filtro) {

        if ((p.mostrarMarca().equals(filtro.getMarca())))
            if ((p.mostrarPrecio() >= Integer.parseInt(filtro.getPrecioMin())) && (p.mostrarPrecio()) <= Integer.parseInt(filtro.getPrecioMax()))
                return true;
        return false;
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
        /*if (marcador != null)
            marcador.remove();*/
        /*marcador = mMap.addMarker(new MarkerOptions().
                position(coordenadas).
                title(this.firstName).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.yop)));*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(miUbicacion);
    }

    public void agregarMarcadorProductos(Producto p){

                mMap.addMarker(new MarkerOptions().
                position(p.coordenadasProducto()).
                title(p.mostrarMarca()+" "+p.mostrarmodelo()).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.pinoferta)).
                snippet("$ "+p.mostrarPrecio()+" CLP en Tienda: "+p.mostrarProveedor()+". Publicado por: "+p.mostrarCreadorPublicacion()));
    }

    public void filtrarProducto(View view){
        int respuestaFiltro=0;
        Intent intent = new Intent(this, ActivityFiltrarProductos.class);
        startActivityForResult(intent,request_code_filtro);
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
        try {
            // obtendo la lista de productos de la bd
            this.app.setListaSmartphone(ConsultasProductos.listarProductos());
        } catch (Exception e) {
            e.getMessage();
        }

        if (this.app.getListaProductos()!=null) {
            for (int i = 0; i < this.app.getListaProductos().size(); i++)
                agregarMarcadorProductos(app.getListaProductos().get(i));
        }

    }

    public Producto buscarFav(Producto p){
        for (int i=0; i<usuario.getListaFavoritos().size() ; i++){
           if (usuario.getListaFavoritos().get(i).mostrarCategoria().equals(p.mostrarCategoria()))
                if (usuario.getListaFavoritos().get(i).coordenadasProducto().latitude == p.coordenadasProducto().latitude && usuario.getListaFavoritos().get(i).coordenadasProducto().longitude == p.coordenadasProducto().longitude)
                     if (usuario.getListaFavoritos().get(i).mostrarmodelo().equals(p.mostrarmodelo()))
                         if (usuario.getListaFavoritos().get(i).mostrarMarca().equals(p.mostrarMarca()))
                             if (usuario.getListaFavoritos().get(i).mostrarPrecio() == p.mostrarPrecio())
                                    return usuario.getListaFavoritos().get(i);
        }
        return null;
    }
    public boolean buscarEnNotificaciones(Producto p){
        for (int i=0; i<usuario.getNotificaciones().size() ; i++)
          if (usuario.getNotificaciones().get(i).mostrarIdEvento() == (p.mostrarIdEvento()))
              return true;

        return false;
    }

    public ArrayList<Producto> buscarProductoPorMarcaModelo(Producto p){
        ArrayList<Integer> posiciones = new ArrayList<Integer>();
        ArrayList<Producto> lista = new ArrayList<Producto>();

        //busco coincidencias
        for (int i = 0; i< app.getListaProductos().size(); i++){
            if (p.mostrarMarca().equals(app.getListaProductos().get(i).mostrarMarca()))
                if (p.mostrarmodelo().equals(app.getListaProductos().get(i).mostrarmodelo()))
                    lista.add(app.getListaProductos().get(i));
        }

        //elimino las concidencias que estan como favoritas
        for (int j=0; j< usuario.getListaFavoritos().size();j++)
            for (int q =0 ; q<lista.size(); q++)
                if (usuario.getListaFavoritos().get(j).mostrarIdEvento() == lista.get(q).mostrarIdEvento())
                    lista.remove(q);


        return lista;
    }
    public ArrayList<Producto> buscarProdParaNotificar(){
        ArrayList<Integer> posiciones = new ArrayList<Integer>();

        ArrayList<Producto> listProdNoti = new ArrayList<Producto>();
        for (int i= 0; i< usuario.getListaFavoritos().size() ; i++){
            if (buscarProductoPorMarcaModelo(usuario.getListaFavoritos().get(i)) !=null){
                ArrayList<Producto> temp =  buscarProductoPorMarcaModelo(usuario.getListaFavoritos().get(i));
                for (int j=0; j<temp.size(); j++)
                    listProdNoti.add(temp.get(j));
            }
        }


        for (int j = 0; j<usuario.getNotificaciones().size() ; j++){
            for (int q = 0 ; q<listProdNoti.size(); q++){
                if (usuario.getNotificaciones().get(j).mostrarIdEvento() == listProdNoti.get(q).mostrarIdEvento())
                    listProdNoti.remove(q);
            }
        }

        return listProdNoti;
    }


    public boolean agregarFavorito(Producto p){
        if (buscarFav(p) == null){ // si no esta repetido se agrega
            usuario.getListaFavoritos().add(p);
            ConsultasUsuarios.agregarFav(usuario.getUsername(),p.mostrarIdEvento());
            return true;
        }
        return false;
    }
    public boolean eliminarFavorito(Producto p){
        if (buscarFav(p)!=null){
            usuario.getListaFavoritos().remove(p);
            ConsultasUsuarios.eliminarFav(usuario.getUsername(),p.mostrarIdEvento());
            return true;
        }
        return false;
    }

    public void obtenerFavs(){
        ArrayList<Integer> idsEventos = ConsultasUsuarios.obtenerFavoritos(usuario.getUsername());
        ArrayList<Producto> favs = new ArrayList<Producto>();

        for (int i = 0 ; i< app.getListaProductos().size() ; i++)
            for (int j = 0 ; j<idsEventos.size(); j++)
                if (app.getListaProductos().get(i).mostrarIdEvento() == idsEventos.get(j))
                    favs.add(app.getListaProductos().get(i));

        usuario.setListaFavoritos(favs);
    }


    //AGREGA ESTE MÉTODO A TU CODIGO. COPIA EL LAYOUR DE ESTA ACTIVIDAD
    public void logout(View view){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void ventanaFav(View view){
        Intent intent = new Intent(this,ListviewFavoritos.class);
        intent.putExtra("user",usuario);
        startActivity(intent);
    }

    public void autoNotificaciones(){
        this.runOnUiThread(notificacionesRunnable);
    }


    public void autoRefresh() {
        this.runOnUiThread(refreshRunnable);
    }

    private Runnable refreshRunnable = new Runnable() {
        public void run() {
            mMap.clear();
            app.getListaProductos().clear();
            app.setListaSmartphone(null);
            cargarDatos();
        }
    };

    private Runnable notificacionesRunnable = new Runnable() {
        public void run() {
            ArrayList<Producto> coicidencias = buscarProdParaNotificar();

            if (coicidencias.size()>0){

                for (int i=0;i<coicidencias.size();i++)
                    usuario.getNotificaciones().add(coicidencias.get(i));

                Intent intent = new Intent(MapsActivity.this,ListViewNotificacion.class);
                intent.putExtra("noti",usuario);
                PendingIntent pendingIntent = PendingIntent.getActivity(MapsActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

                //notificacion
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));

                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(sound);

                builder.setContentTitle("Nuevos productos!");
                builder.setContentText("Productos de su interes: "+coicidencias.size());
                builder.setSubText("toque para ver los productos, apresurate !");

                //Enviar notificacion
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICACION_ID,builder.build());
            }
            coicidencias=null;
        }
    };
}







