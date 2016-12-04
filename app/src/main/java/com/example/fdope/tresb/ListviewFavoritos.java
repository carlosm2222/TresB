package com.example.fdope.tresb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.FactoriaProductos.Producto;

public class ListviewFavoritos extends AppCompatActivity implements PostEliminarFav{

    private ListView lista;
    private Usuario usuario;
    private CheckBox notificacion;
    public ListAdapterFavorito listadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_favoritos);
        lista = (ListView) findViewById(R.id.listaProd);
        notificacion = (CheckBox) findViewById(R.id.checkBoxNoti);

        usuario = getIntent().getExtras().getParcelable("user");

        if (usuario!=null){

            notificacion.setChecked(usuario.estadoRecibirNotificacion());
            notificacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usuario.actYDesacNotificacion(notificacion.isChecked());
                }
            });

            if (usuario.getListaFavoritos().size()<1)
                Toast.makeText(this,"No tienes productos guardados en favoritos.",Toast.LENGTH_LONG).show();

            listadapter = new ListAdapterFavorito(ListviewFavoritos.this,R.layout.list_fila_fav,usuario.getListaFavoritos());
            lista.setAdapter(listadapter);

        }
    }

    @Override
    public void productoEliminado(Producto delete) {

            if(usuario.eliminarFavPorID(delete.mostrarIdEvento())) {
                Toast.makeText(this, "Eliminado de favoritos.", Toast.LENGTH_SHORT).show();
                listadapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("usuarioOut",usuario);
                setResult(RESULT_OK,intent);
            }
           else
                Toast.makeText(this,"Error al eliminar", Toast.LENGTH_SHORT).show();

    }
}
