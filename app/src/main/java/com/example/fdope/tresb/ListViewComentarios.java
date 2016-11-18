package com.example.fdope.tresb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Comentario;
import com.example.fdope.tresb.Clases.ComentarioAdapter;
import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.DB.ConsultaComentarios;

import java.util.ArrayList;

/**
 * Created by fdope on 17-11-2016.
 */

public class ListViewComentarios extends AppCompatActivity {
    public ListView lista;
    private ArrayList<Comentario> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_comentarios);
        lista = (ListView) findViewById(R.id.listaProd);


        Bundle bundle = getIntent().getExtras();
        int idEvento = bundle.getInt("idEvento");
        list= ConsultaComentarios.listarComentarios(idEvento);
        if (!list.isEmpty()){
             ComentarioAdapter listadapter = new ComentarioAdapter(ListViewComentarios.this,R.layout.lista_comentarios,list);
             lista.setAdapter(listadapter);
        } else
            Toast.makeText(this, "No hay comentarios para esta publicación.", Toast.LENGTH_SHORT).show();

    }
}
