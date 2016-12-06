package com.example.fdope.tresb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

public class InicioActivity extends AppCompatActivity implements EnviarInfoDesdeListsAdapter {
    public Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ListView listView = (ListView)findViewById(R.id.listViewInicio);
        Button mapa = (Button) findViewById(R.id.gomap);
        Button salir = (Button) findViewById(R.id.exit);

        Bundle inBundle = getIntent().getExtras();//agregar desde aqui
        if (inBundle != null) {
            usuario = inBundle.getParcelable("UsuarioIn");
            if (usuario==null)
                Toast.makeText(this, "ERROR EN LA CONEXION", Toast.LENGTH_SHORT).show();

            ArrayList<Producto>  recientes = ConsultasProductos.listarRecientes();

            if (recientes!=null){
                if (recientes.size()>0){
                    ListAdapterInicio listadapter = new ListAdapterInicio(InicioActivity.this,R.layout.list_fila,recientes);
                    listView.setAdapter(listadapter);
                }
                else {
                    irMapa();
                }
            }else
                Toast.makeText(this,"No se pudo cargar los datos",Toast.LENGTH_SHORT).show();
        }
    }

    public void irMapa(View view){

        nextActivity(usuario);
    }

    public void irMapa(){
        nextActivity(usuario);
    }
    public void salir(View view){
        finish();
    }

    private void nextActivity(Usuario u) {
        Toast.makeText(this,"Cargando ofertas...",Toast.LENGTH_SHORT).show();
        Intent main = new Intent(this, MapsActivity.class);
        main.putExtra("UsuarioIn",u);
        startActivity(main);
        finish();
    }
    @Override
    public void productoEliminado(Producto delete) {

    }

    @Override
    public void recibirCoordenadas(LatLng latLng) {
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("coordenadas",latLng);
        intent.putExtra("UsuarioIn",usuario);
        intent.putExtra("flag",true);
        startActivity(intent);
        finish();

    }
}
