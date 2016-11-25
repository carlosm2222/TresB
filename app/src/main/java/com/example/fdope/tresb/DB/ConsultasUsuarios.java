package com.example.fdope.tresb.DB;

import android.util.Log;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.Factoria.Celular;
import com.example.fdope.tresb.Factoria.Producto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by SS on 01-11-2016.
 */

public class ConsultasUsuarios {


    public  static Boolean registrar(String nombre, String apellidos, String email, String password, String username) {
        DB db=new DB();
        Connection c =db.connect();

        try {
            CallableStatement oCall;
            oCall = c.prepareCall("{ ? = call agregarcliente(?,?,?,?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(2,username);
            oCall.setString(3,password);
            oCall.setString(4,nombre);
            oCall.setString(5,apellidos);
            oCall.setString(6,email);
            oCall.execute();

            boolean resp = oCall.getBoolean(1);
            return resp;
        }catch (Exception e){

        }
        return false;
    }

    public static Boolean checkUsuario(String username,String password) {
        DB db=new DB();
        Connection c =db.connect();

        try {

            CallableStatement oCall = c.prepareCall("{ ? = call validarlogin(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(2,username);
            oCall.setString(3,password);
            oCall.execute();

            boolean resp = oCall.getBoolean(1);
            return resp;

        }catch (Exception e){

        }
        return false;
    }

    public static Usuario obtenerUsuario(String username){
        DB db=new DB();
        Connection c =db.connect();

        try {
            //CallableStatement oCall = c.prepareCall("{ call obtener_usuario(?) }");
            //oCall.setString(1,username);
            //ResultSet resultSet = oCall.executeQuery();
            ResultSet resultSet = db.execute("SELECT * FROM obtener_usuario('"+username+"');");
            if (resultSet!=null){
                while(resultSet.next()){
                    String user = resultSet.getString("usuario");
                    String password = resultSet.getString("contrasena");
                    String nombre = resultSet.getString("nombre");
                    String apellidos = resultSet.getString("apellido");
                    String email = resultSet.getString("correo");
                    Usuario usuario = new Usuario(nombre,apellidos,email,password,username);
                    return usuario;
                }
            }
        }catch (Exception e){

        }
        return null;
    }

    public static boolean agregarFav(String user,int idEvento){
        DB db=new DB();
        Connection c =db.connect();

        try {

            CallableStatement oCall = c.prepareCall("{ ? = call agregarfavorito(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,user);
            oCall.setInt(2,idEvento);
            oCall.execute();

            Boolean resp = oCall.getBoolean(1);
            return resp;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean eliminarFav(String user,int idEvento){
        DB db=new DB();
        Connection c =db.connect();

        try {

            CallableStatement oCall = c.prepareCall("{ ? = call quitarfavorito(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,user);
            oCall.setInt(2,idEvento);
            oCall.execute();

            boolean resp = oCall.getBoolean(1);
            return resp;

        }catch (Exception e){

        }
        return false;
    }

    public static ArrayList<Integer> obtenerFavoritos(String username){
        DB db=new DB();
        Connection c =db.connect();
        ArrayList<Integer> favoritos = new ArrayList<Integer>();
        try {

            ResultSet resultSet = db.execute("SELECT id_evento FROM getinfofavorito('"+username+"');");
            if (resultSet!=null){
                while(resultSet.next()){
                    int idEvento = resultSet.getInt("id_evento");
                    favoritos.add(idEvento);
                }
            }
            return favoritos;

        }catch (Exception e){

        }
        return favoritos;
    }

    public static boolean agregarNotificacion(int idEvento, String user){
        DB db=new DB();
        Connection c =db.connect();
        try {
            CallableStatement oCall = c.prepareCall("{ ? = call agregarnotificacion(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,user);
            oCall.setInt(2,idEvento);
            oCall.execute();

            return   oCall.getBoolean(1);

        }catch (Exception e){

        }
        return false;
    }

    public static boolean eliminarNotificacion(int idEvento, String user){
        DB db=new DB();
        Connection c =db.connect();
        try {
            CallableStatement oCall = c.prepareCall("{ ? = call quitarnotificacion(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,user);
            oCall.setInt(2,idEvento);
            oCall.execute();

            boolean resp = oCall.getBoolean(1);
            return resp;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean consultarNotificacion(int idEvento, String user){
        DB db=new DB();
        Connection c =db.connect();
        try {
            CallableStatement oCall = c.prepareCall("{ ? = call saberestadonotificacion(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,user);
            oCall.setInt(2,idEvento);
            oCall.execute();

            boolean resp = oCall.getBoolean(1);
            return resp;

        }catch (Exception e){

        }
        return false;
    }

}
