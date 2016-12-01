package com.example.fdope.tresb.DB;

import com.example.fdope.tresb.Clases.Comentario;

import com.example.fdope.tresb.Factoria.Celular;
import com.example.fdope.tresb.Factoria.Producto;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

            ResultSet resultado = base.select("SELECT *from getinfocomentario('"+idEvento+"');");
            if(resultado !=null){

                while(resultado.next()){
                    Timestamp fechaPublicacion= resultado.getTimestamp("fecha_publicacion");
                    String fecha = new SimpleDateFormat("dd/MMMM/yyyy HH:mm").format(fechaPublicacion);
                    Comentario coment = new Comentario(resultado.getString("usuariocomentarista"),resultado.getString("comentario"),resultado.getBoolean("megusta"),fecha);
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
    public static boolean publicarComentario(int idEvento,String comentario,String username, boolean flag) {
        DB base = new DB();
        Connection c = base.connect();
        try {

            ResultSet resultSet = base.select("SELECT * FROM agregarcomentario('"+idEvento+"','"+comentario+"','"+username+"','"+flag+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregarcomentario");
                    return resp;
                }
            }
            else
                return false;
        }catch (Exception e){

        }
        return false;
    }
}

