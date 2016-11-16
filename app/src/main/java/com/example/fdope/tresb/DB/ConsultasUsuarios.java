package com.example.fdope.tresb.DB;

import android.util.Log;

import com.example.fdope.tresb.Clases.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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

    public static boolean agregarFav(String categoria,String marca,String modelo,int precio,double lat,double lng,String proveedor,String user){
        return false;
    }

    public static boolean eliminarFav(String categoria,String marca,String modelo,int precio,double lat,double lng,String proveedor,String user){
        return false;
    }
}
