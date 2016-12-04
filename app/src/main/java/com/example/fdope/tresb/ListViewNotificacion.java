package com.example.fdope.tresb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;

public class ListViewNotificacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_notificacion);

        ListView lista = (ListView)findViewById(R.id.listViewNotificaciones);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            Usuario usuario = bundle.getParcelable("noti");
            if (usuario != null) {
               if (usuario.getNotificaciones() != null){

                    ListAdapterNotificacion listadapter = new ListAdapterNotificacion(ListViewNotificacion.this, R.layout.list_notificaciones, usuario.getNotificaciones());
                    lista.setAdapter(listadapter);
                }
                else
                 Toast.makeText(this,"No se pudo obtener notificaciones",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"No se pudo el perfil de usuario",Toast.LENGTH_SHORT).show();
        }
    }
}
