package com.example.fdope.tresb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.fdope.tresb.Clases.Usuario;

public class ListViewNotificacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_notificacion);

        ListView lista = (ListView)findViewById(R.id.listViewNotificaciones);

        Bundle bundle = getIntent().getExtras();
        Usuario usuario = bundle.getParcelable("user");

        if(usuario!=null){

            ListAdapterFavorito listadapter = new ListAdapterFavorito(ListViewNotificacion.this,R.layout.list_notificaciones,usuario.getListaFavoritos());
            lista.setAdapter(listadapter);
        }
    }
}
