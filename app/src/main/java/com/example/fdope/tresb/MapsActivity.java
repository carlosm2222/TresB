package com.example.fdope.tresb;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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

import com.example.fdope.tresb.Clases.Filtro;
import com.example.fdope.tresb.Clases.TresB;
import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.FactoriaProductos.Producto;
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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,InfoPostDialog {
    private GoogleMap mMap;
    private double lat;
    private double lng;
    private TresB app;
    static final int request_code = 1;
    static final int request_code_fav = 5;
    static final int request_code_filtro = 2;
    static final int NOTIFICACION_ID=5;
    private Producto p,prodMomentaneo,productoComp1,productocom2;
    private Usuario usuario;
    int contDenuncia;
    boolean isOpen = false;
    private boolean flagFiltro = false;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3,floatingActionButton4,floatingActionButton5;
    private int flagfav=0; /// flag= true es favorito , false no es favorito
    private int flagComparacion1=0;
    private int flagComparacion2=0;
    private boolean flagInicio=false;
    private LatLng coordenadasProductoInicio=null;

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
                if (flagFiltro==false)
                filtrarProducto(v);
                else if (flagFiltro==true) {
                    cargarDatos();
                    floatingActionButton2.setImageResource(R.drawable.ic_search_black_24dp);
                    floatingActionButton2.setLabelText("Filtrar");
                    flagFiltro=false;
                }
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
                final View view= v;
                final ProgressDialog ringProgressDialog = ProgressDialog.show(MapsActivity.this, "", "Cargando favoritos ...", true);
                ringProgressDialog.setCancelable(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // Here you should write your time consuming task...
                            ventanaFav(view);
                            // Let the progress ring for 10 seconds...
                            Thread.sleep(10000);
                        } catch (Exception e) {
                        }
                        ringProgressDialog.dismiss();
                    }
                }).start();


            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle inBundle = getIntent().getExtras();//agregar desde aqui


        if (inBundle != null) {

            usuario = inBundle.getParcelable("UsuarioIn");
            coordenadasProductoInicio = inBundle.getParcelable("coordenadas");
            flagInicio=inBundle.getBoolean("flag");

            if (usuario!=null) {
                Toast.makeText(this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                contDenuncia=usuario.getNumeroDenuncias();

            }
            else {
                Toast.makeText(this, "NO HAY CONEXION ", Toast.LENGTH_SHORT).show();
                logout();
                finish();
            }
        }

        //Timer
        Timer timer = new Timer();
        Timer timer2 = new Timer();

        //Que actue cada 2 minutos
        //Empezando des de el segundo 30seg iniciado el mapa
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //La función a ejecutar
                    if ( !flagFiltro )
                        autoRefresh();
                    if ( usuario.estadoRecibirNotificacion() )// si tiene activada el recibir notificacion se ejecuta las notificaciones
                        autoNotificaciones();

            }
        }, 30000, 120000);

    // inicia alos 5 seg de abrir la app en mapa y cada 5 min  ejecuta
        timer2.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                revisarDenunciasUsuario();
            }
        }, 5000, 600000);
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        cargarDatos();
        if(flagInicio) {
            moverCamaraHaciaProducto(coordenadasProductoInicio);
        }
        else
            miUbicacion();

        if (usuario!=null) {
            obtenerFavs();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                byte[] b=null;
                boolean fav=false; /// flag= true es favorito , false no es favorito
                Producto prodFav=null;
                if (app.getListaProductos()!=null)
                    for (int i = 0; i < app.getListaProductos().size(); i++) {

                        String titulo  = app.getListaProductos().get(i).mostrarMarca()+" "+app.getListaProductos().get(i).mostrarmodelo();
                        String spin = "$ "+app.getListaProductos().get(i).mostrarPrecio()+" CLP en Tienda: "+app.getListaProductos().get(i).mostrarProveedor()+". Publicado por: "+app.getListaProductos().get(i).mostrarCreadorPublicacion();

                        if (titulo.equals(marker.getTitle()) && spin.equals(marker.getSnippet())) {

                            int idEvento = app.getListaProductos().get(i).mostrarIdEvento();

                            prodMomentaneo=app.getListaProductos().get(i);
                            b = app.getListaProductos().get(i).mostrarImagen();// se obtiene la imagen del producto
                            prodFav = usuario.buscarFav(app.getListaProductos().get(i)); // SE OBTIENE PRODUCTO FAVORITO SI ESQUE EXISTE

                            if (prodFav!=null) // SE SETEA EL FLAG A TRUE SI EXISTE FAV
                                fav = true;

                            if ((productoComp1!=null && productoComp1==prodMomentaneo) && (!fav && flagfav==1)) {
                                mostrarVentanaPin(marker.getTitle(),marker.getSnippet(),b,true, idEvento,true); // se envia el titulo y el snippet del marcador ala ventana del pin con la foto y el FLAG de favorito
                                flagfav = 0;
                                break;
                            }
                            if ((productoComp1!=null && productoComp1==prodMomentaneo) && (flagfav==2 && fav)){
                                mostrarVentanaPin(marker.getTitle(),marker.getSnippet(),b,false, idEvento,true);
                                flagfav = 0;
                                break;
                            }
                            if (productoComp1!=null && productoComp1==prodMomentaneo)
                                mostrarVentanaPin(marker.getTitle(),marker.getSnippet(),b,fav,idEvento,true); // se envia el titulo y el snippet del marcador ala ventana del pin con la foto y el FLAG de favorito
                            else
                                mostrarVentanaPin(marker.getTitle(),marker.getSnippet(),b,fav,idEvento,false);

                            flagfav = 0;
                            break;
                        }
                    }
                moverCamaraHaciaProducto(marker.getPosition());
                    return true;
                }
            });
    }

    public void moverCamaraHaciaProducto(LatLng latLng){
        CameraUpdate ubicacionpin= CameraUpdateFactory.newLatLngZoom(latLng,20);
        mMap.animateCamera(ubicacionpin);
    }

    @Override // RECIBE EL ESTADO DEL CHECKBOX FAVORITOS
    public void onFinishDialogFavorito(boolean flag) {
        // flag 0 neutro , 1 true,2 false
        if (flagfav == 0 && flag==true){ /// SI NO ERA FAVORITO
            if(usuario.agregarFavorito(prodMomentaneo)){
                Toast.makeText(this, "Agregado a favorito", Toast.LENGTH_SHORT).show();
                refresh();
            }
        }
        if(flag==false)
        {
            if(usuario.eliminarFavorito(prodMomentaneo)){
                Toast.makeText(this, "Eliminado de favorito", Toast.LENGTH_SHORT).show();
                flagfav = 0;
                refresh();
            }
        }
    }

    @Override
    public void onFinishDialogComparar(boolean flag) {

        if (flagComparacion1 == 1 && flagComparacion2 == 0 && flag==false){
            productocom2=null;
            productoComp1=null;
            flagComparacion2=0;
            flagComparacion1=0;
            Toast.makeText(this,"Producto descartado para comparar",Toast.LENGTH_LONG).show();
        }
        if (flagComparacion1 == 0 && flagComparacion2 == 0 && flag==true){
            flagComparacion1=1;
            productoComp1=prodMomentaneo;
            Toast.makeText(this,"Has seleccionado 1 producto para comparar, selecciona otro !",Toast.LENGTH_LONG).show();
        }
        if (flagComparacion1==1 && flagComparacion2==0 && flag==true){
            if (productoComp1!=prodMomentaneo)
            {
                flagComparacion2=1;
                productocom2=prodMomentaneo;
                if (productocom2 != null && productoComp1!=null)
                    mostrarVentanaCompararPin(productoComp1,productocom2);
            }
        }
    }

    @Override
    public void salir(boolean salir) {
    }

    @Override
    public void postIngresarUsuarioFacebook(String username) {
    }

    private void mostrarVentanaPin(String titulo, String info, byte[] img, boolean flag, int idEvento, boolean flagChecbox) {
        FragmentManager fm = getFragmentManager();
        MarkerActivity dialogFragment = new MarkerActivity ();
        Bundle bundle = new Bundle();
        bundle.putString("titulo",titulo);
        bundle.putString("info",info);
        bundle.putString("username",usuario.getUsername());
        bundle.putInt("idEvento",idEvento);
        bundle.putByteArray("img",img);
        bundle.putBoolean("flag",flag);
        bundle.putBoolean("flagCheckbox",flagChecbox);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Sample Fragment");
    }

    private void mostrarVentanaCompararPin(Producto p1, Producto p2){

        FragmentManager fm1 = getFragmentManager();
        CompararDialog compararDialog = new CompararDialog();
        Bundle bundle1 =new Bundle();
        bundle1.putString("marca1", p1.mostrarMarca());
        bundle1.putString("marca2", p2.mostrarMarca());
        bundle1.putString("modelo1",p1.mostrarmodelo());
        bundle1.putString("modelo2",p2.mostrarmodelo());
        bundle1.putString("tienda1",p1.mostrarProveedor());
        bundle1.putString("tienda2",p2.mostrarProveedor());
        bundle1.putInt("precio1",p1.mostrarPrecio());
        bundle1.putInt("precio2",p2.mostrarPrecio());
        bundle1.putByteArray("img1",p1.mostrarImagen());
        bundle1.putByteArray("img2",p2.mostrarImagen());
        compararDialog.setArguments(bundle1);
        compararDialog.show(fm1," comparar");

        productocom2=null;
        productoComp1=null;
        flagComparacion2=0;
        flagComparacion1=0;
    }

    public void refresh() {
        mMap.clear();
        this.app.getListaProductos().clear();
        this.app.setListaSmartphone(null);
        cargarDatos();
    }

    public void formulario(View view) {
        Intent intent = new Intent(this, FormularioProductoActivity.class);
        Location location = obetnerUbicacion();
        intent.putExtra("coordenadas", location);
        intent.putExtra("usuario",usuario.getUsername());
        startActivity(intent);
    }

    public void ventanaFav(View view){
        Intent intent = new Intent(this,ListviewFavoritos.class);
        intent.putExtra("user",usuario);
        startActivityForResult(intent, request_code_fav);
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
                        refresh();
                        Toast.makeText(this, "Agregado al mapa correctamente.", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == request_code_filtro) {
            switch (requestCode) {
                case (FILTRO_OK): {
                    if (data!=null) {
                        Filtro filtro = data.getExtras().getParcelable("filtro");
                        filtrarProductos(filtro);
                    }
                }
            }
        }
        if (requestCode == request_code_fav)
            if (data != null){
                Toast.makeText(this, "Favoritos actualizados", Toast.LENGTH_SHORT).show();
                usuario = (data.getExtras().getParcelable("usuarioOut"));

            }
    }

    private void filtrarProductos(Filtro filtro) {

            ArrayList<Producto> lista = ConsultasProductos.listarFiltrados(filtro);
            if (!lista.isEmpty()){
                mMap.clear();
                for (int i = 0; i < lista.size(); i++) {
                    agregarMarcadorProductos(lista.get(i));
                }
                Toast.makeText(this,"Filtro aplicado.",Toast.LENGTH_SHORT).show();
                flagFiltro = true;
                floatingActionButton2.setImageResource(R.drawable.ic_equis);
                floatingActionButton2.setLabelText("Deshacer Filtro");
            }else if (lista.isEmpty())
                Toast.makeText(this, "No hay ofertas con esas características", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Intent intent = new Intent(this, ActivityFiltrarProductos.class);
        startActivityForResult(intent,request_code_filtro);
        flagFiltro=false;
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

    public void logout(View view){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void cargarDatos() {
        try {
            // obtendo la lista de productos de la bd
            app.obtenerProductos();
        } catch (Exception e) {
            e.getMessage();
        }

        if (this.app.getListaProductos()!=null) {
            for (int i = 0; i < this.app.getListaProductos().size(); i++)
                agregarMarcadorProductos(app.getListaProductos().get(i));
        }

    }

    public ArrayList<Producto> buscarProductoPorMarcaModelo(Producto p){
        ArrayList<Producto> lista = new ArrayList<Producto>();

        //busco coincidencias
        for (int i = 0; i< app.getListaProductos().size(); i++){
            if (p.mostrarMarca().equals(app.getListaProductos().get(i).mostrarMarca()))
                if ( (p.mostrarmodelo().equals(app.getListaProductos().get(i).mostrarmodelo())) && ( p.mostrarIdEvento() != app.getListaProductos().get(i).mostrarIdEvento())   )
                    lista.add(app.getListaProductos().get(i));
        }

        return lista;
    }

    public ArrayList<Producto> buscarProdParaNotificar(){
        ArrayList<Producto> listProdNoti = new ArrayList<Producto>();

        for (int i= 0; i< usuario.getListaFavoritos().size() ; i++){
            ArrayList<Producto> temp =  buscarProductoPorMarcaModelo(usuario.getListaFavoritos().get(i));
            if (temp !=null){
                for (int j=0; j<temp.size(); j++)
                    listProdNoti.add(temp.get(j));
            }
        }
        return listProdNoti;
    }

    public void obtenerFavs(){
        ArrayList<Integer> idsEventos = usuario.listarFavoritos();
        ArrayList<Producto> favs = new ArrayList<Producto>();
        if (idsEventos != null) {
            for (int i = 0; i < app.getListaProductos().size(); i++)
                for (int j = 0; j < idsEventos.size(); j++)
                    if (app.getListaProductos().get(i).mostrarIdEvento() == idsEventos.get(j)) {
                        favs.add(app.getListaProductos().get(i));
                    }
        }

        if (favs!=null)
            usuario.setListaFavoritos(favs);
    }

    public void autoNotificaciones(){
        this.runOnUiThread(notificacionesRunnable);
    }

    public void autoRefresh() {
        this.runOnUiThread(refreshRunnable);
    }

    public void revisarDenunciasUsuario(){
        this.runOnUiThread(revisarDenunciasUsuarioRunnable);
    }

    private Runnable refreshRunnable = new Runnable() {
        public void run() {
            mMap.clear();
            app.getListaProductos().clear();
            app.setListaSmartphone(null);
            cargarDatos();
        }
    };

    private Runnable revisarDenunciasUsuarioRunnable= new Runnable() {
        public void run() {

            int numAdv=app.buscarYNotificarDenunciasUsuario(usuario.getUsername());

                if (numAdv < usuario.getNumeroDenuncias() ){
                    usuario.setNumeroDenuncias(numAdv);

                    Intent intent = new Intent();
                    PendingIntent pendingIntent = PendingIntent.getActivity(MapsActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    //notificacion
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setSmallIcon(android.R.drawable.stat_notify_error);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));

                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(sound);
                    builder.setContentTitle("¡ ADVERTENCIA !");
                    builder.setContentText("Tu publicacion ha sido eliminada.");
                    builder.setSubText("Si tienes 3 publicaciones eliminadas quedaras bloqueado en la App");

                    //Enviar notificacion
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICACION_ID, builder.build());
                }
                if (numAdv == 0){
                    usuario.setNumeroDenuncias(0);
                    Intent intent = new Intent(MapsActivity.this,LoginActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MapsActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    //notificacion
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setSmallIcon(android.R.drawable.stat_notify_error);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));

                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(sound);

                    builder.setContentTitle("¡Has sido bloqueado! ");
                    builder.setContentText("Motivo: Tener mas de 3 publicaciones eliminadas por denuncias.");


                    //Enviar notificacion
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICACION_ID, builder.build());
                }

        }
    };

    private Runnable notificacionesRunnable = new Runnable() {
        public void run() {

            ArrayList<Producto> coicidencias = buscarProdParaNotificar();
            if (coicidencias!=null){
                if (coicidencias.size()>0){
                    for (int i = 0; i < coicidencias.size(); i++) {

                        if (usuario.buscarNotificacionBD(coicidencias.get(i)) == false) {
                            if (usuario.agregarNotificacion(coicidencias.get(i))) {

                                Intent intent = new Intent(MapsActivity.this, ListViewNotificacion.class);
                                intent.putExtra("noti", usuario);
                                PendingIntent pendingIntent = PendingIntent.getActivity(MapsActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                //notificacion
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this);
                                builder.setContentIntent(pendingIntent);
                                builder.setAutoCancel(true);
                                builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));

                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                builder.setSound(sound);

                                builder.setContentTitle("Nuevos productos!");
                                builder.setContentText("Productos de su interes: " + coicidencias.get(i).mostrarMarca() + " " + coicidencias.get(i).mostrarmodelo());
                                builder.setSubText("toque para ver los productos, apresurate !");

                                //Enviar notificacion
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(NOTIFICACION_ID, builder.build());
                            }
                        }
                    }
                }
            }
        }
    };
}
