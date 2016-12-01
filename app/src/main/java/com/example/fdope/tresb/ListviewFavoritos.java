package com.example.fdope.tresb;

import android.content.DialogInterface;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.Factoria.Producto;

public class ListviewFavoritos extends AppCompatActivity implements PostEliminarFav{

    private ListView lista;
    private Usuario usuario;
    private Producto p;
    private CheckBox notificacion;
    public ListAdapterFavorito listadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_favoritos);
        lista = (ListView) findViewById(R.id.listaProd);
        notificacion = (CheckBox) findViewById(R.id.checkBoxNoti);

        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getParcelable("user");

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

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        AlertDialog.Builder adb=new AlertDialog.Builder(ListviewFavoritos.this);
                        adb.setTitle("Delete?");
                        adb.setMessage("Are you sure you want to delete " + position);
                        final int positionToRemove = position;
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                usuario.getListaFavoritos().remove(positionToRemove);
                                listadapter.notifyDataSetChanged();
                            }});
                        adb.show();
                    }
            });
        }
    }

    @Override
    public void productoEliminado(Producto peliminado) {

        if(usuario.eliminarFavorito(peliminado)){
            listadapter.notifyDataSetChanged();
            if (usuario.buscarFav(peliminado)==null)
                Toast.makeText(this,"Producto eliminado de favoritos",Toast.LENGTH_SHORT).show();
        }
        else
        Toast.makeText(this,"Error al eliminar "+peliminado.mostrarmodelo(),Toast.LENGTH_SHORT).show();
    }

}
