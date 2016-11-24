package com.example.fdope.tresb;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;

public class ListviewFavoritos extends AppCompatActivity {

    private ListView lista;
    private TextView numeroFav;
    private FloatingActionButton botonFav;
    private InfoPostDialog mCallback;
    private boolean flag;
    private Usuario usuario;
    private Button delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_favoritos);
        lista = (ListView) findViewById(R.id.listaProd);
        numeroFav = (TextView) findViewById(R.id.numFav);
       // delete = (Button)findViewById(R.id.botonElimiarFav);

        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getParcelable("user");

        if (usuario!=null){
            if (usuario.getListaFavoritos().size()>0)
                numeroFav.setText(String.valueOf(usuario.getListaFavoritos().size()));
            else
                numeroFav.setText("0");


            ListAdapterFavorito listadapter = new ListAdapterFavorito(ListviewFavoritos.this,R.layout.list_fila_fav,usuario.getListaFavoritos());
            lista.setAdapter(listadapter);


        }
        else
            Toast.makeText(this,"No se pudo obtener",Toast.LENGTH_SHORT).show();
    }

}
