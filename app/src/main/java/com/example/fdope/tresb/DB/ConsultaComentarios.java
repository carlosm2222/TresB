package com.example.fdope.tresb.DB;

import com.example.fdope.tresb.Clases.Comentario;

import com.example.fdope.tresb.Factoria.Celular;
import com.example.fdope.tresb.Factoria.Producto;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by fdope on 16-11-2016.
 */

public class ConsultaComentarios {
    public static ArrayList<Comentario> listarComentarios(int idEvento){
        DB base = new DB();
        Connection c= base.connect();

        ArrayList<Comentario> listap = new ArrayList<Comentario>();
        try{
            //CallableStatement oCall = c.prepareCall("{call getINFO()}");
            //ResultSet resultado = oCall.executeQuery();

            ResultSet resultado = base.select("SELECT comentario, usuario WHERE id_evento="+idEvento+";");
            if(resultado !=null){

                while(resultado.next()){
                    Comentario coment = new Comentario(resultado.getString("usuario"),resultado.getString("comentario"));
                    listap.add(coment);

                }
                return listap;
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listap;

    }
}
