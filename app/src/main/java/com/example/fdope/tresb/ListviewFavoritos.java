package com.example.fdope.tresb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.Factoria.Producto;

import java.util.ArrayList;

public class ListviewFavoritos extends AppCompatActivity {

    public ListView lista;
    public ArrayList<Producto> listaFavoritos = new ArrayList<Producto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_favoritos);

        Bundle bundle = new Bundle();
        listaFavoritos = (ArrayList<Producto>) bundle.get("lista");
        //listaFavoritos = u.getListaFavoritos();



        lista = (ListView) findViewById(R.id.listaProd);

        ListAdapter adapter = new ListAdapter(getApplicationContext(),listaFavoritos);
        lista.setAdapter(adapter);

    }
}
