package com.example.fdope.tresb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;

public class ListviewFavoritos extends AppCompatActivity {

    public ListView lista;
    public TextView numeroFav;

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
