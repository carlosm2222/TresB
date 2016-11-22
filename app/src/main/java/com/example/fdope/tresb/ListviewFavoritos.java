package com.example.fdope.tresb;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;

public class ListviewFavoritos extends AppCompatActivity {

    private ListView lista;
    private TextView numeroFav;
    private FloatingActionButton botonFav;
    private EnviarFlagFavorito mCallback;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_favoritos);
        lista = (ListView) findViewById(R.id.listaProd);
        numeroFav = (TextView) findViewById(R.id.numFav);

        Bundle bundle = getIntent().getExtras();
        Usuario usuario = bundle.getParcelable("user");

        if (usuario!=null){
            if (usuario.getListaFavoritos().size()>0)
                numeroFav.setText(String.valueOf(usuario.getListaFavoritos().size()));
            else
                numeroFav.setText("0");


            ListAdapterFavorito listadapter = new ListAdapterFavorito(ListviewFavoritos.this,R.layout.list_fila,usuario.getListaFavoritos());
            lista.setAdapter(listadapter);

        }
        else
            Toast.makeText(this,"No se pudo obtener",Toast.LENGTH_SHORT).show();
    }


}
