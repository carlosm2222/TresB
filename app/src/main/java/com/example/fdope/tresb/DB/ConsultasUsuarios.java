package com.example.fdope.tresb.DB;

import com.example.fdope.tresb.Clases.Usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by SS on 01-11-2016.
 */

public class ConsultasUsuarios {


    public  static boolean registrar(String nombre, String apellidos, String email, String password, String username) {
        DB db=new DB();
        Connection c =db.connect();

        try {/*
            CallableStatement oCall;
            oCall = c.prepareCall("{ ? = call agregarcliente(?,?,?,?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(2,username);
            oCall.setString(3,password);
            oCall.setString(4,nombre);
            oCall.setString(5,apellidos);
            oCall.setString(6,email);
            ResultSet resultSet= oCall.executeQuery();*/
            ResultSet resultSet = db.execute("SELECT * FROM agregarcliente('"+username+"','"+password+"','"+nombre+"','"+apellidos+"','"+email+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregarcliente");
                    return resp;
                }
            }
            else
                return false;


        }catch (Exception e){

        }
        return false;
    }

    public  static boolean registrar(String nombre, String apellidos, String email, String password, String username,String idFacebook) {
        DB db=new DB();
        Connection c =db.connect();

        try {
            ResultSet resultSet = db.execute("SELECT * FROM agregarclientefacebook('"+username+"','"+password+"','"+nombre+"','"+apellidos+"','"+email+"','"+idFacebook+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregarclientefacebook");
                    return resp;
                }
            }
            else
                return false;


        }catch (Exception e){

        }
        return false;
    }

    public static boolean checkUsuario(String username,String password) {
        DB db=new DB();
        Connection c =db.connect();

        try {

            ResultSet resultSet = db.execute("SELECT * FROM validarlogin('"+username+"','"+password+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("validarlogin");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean checkUsuarioFacebook(String id) {
        DB db=new DB();
        Connection c =db.connect();

        try {

            ResultSet resultSet = db.execute("SELECT * FROM buscarusuariofacebook('"+id+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("buscarusuariofacebook");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean verificarNombreUsuario(String username) {
        DB db=new DB();
        Connection c =db.connect();

        try {

            ResultSet resultSet = db.execute("SELECT * FROM verificarusuario('"+username+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("verificarusuario");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static Usuario obtenerUsuario(String username){
        DB db=new DB();
        Connection c =db.connect();

        try {

            ResultSet resultSet = db.execute("SELECT * FROM obtener_usuario('"+username+"');");
            if (resultSet!=null){
                while(resultSet.next()){
                    String user = resultSet.getString("usuario");
                    String password = resultSet.getString("contrasena");
                    String nombre = resultSet.getString("nombre");
                    String apellidos = resultSet.getString("apellido");
                    String email = resultSet.getString("correo");
                    boolean noti = resultSet.getBoolean("recibirnotificacion");
                    int denuncias = resultSet.getInt("denunciascliente");
                    Usuario usuario = new Usuario(nombre,apellidos,email,password,username,noti,denuncias);
                    return usuario;
                }
            }
        }catch (Exception e){

        }
        return null;
    }

    public static Usuario obtenerUsuarioFacebook(String id){
        DB db=new DB();
        Connection c =db.connect();

        try {

            ResultSet resultSet = db.execute("SELECT * FROM obtener_usuariofacebook('"+id+"');");
            if (resultSet!=null){
                while(resultSet.next()){
                    String user = resultSet.getString("usuario");
                    String password = resultSet.getString("contrasena");
                    String nombre = resultSet.getString("nombre");
                    String apellidos = resultSet.getString("apellido");
                    String email = resultSet.getString("correo");
                    boolean noti = resultSet.getBoolean("recibirnotificacion");
                    int denuncias = resultSet.getInt("denunciascliente");
                    Usuario usuario = new Usuario(nombre,apellidos,email,password,user,noti,denuncias);
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
            ResultSet resultSet = db.execute(" SELECT * FROM agregarfavorito("+ idEvento +",'"+ user +"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregarfavorito");
                    return resp;
                }
            }
                else
                    return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean eliminarFav(String user,int idEvento){
        DB db=new DB();
        Connection c =db.connect();

        try {

            ResultSet resultSet = db.execute(" SELECT * FROM quitarfavorito("+ idEvento +",'"+ user +"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("quitarfavorito");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static ArrayList<Integer> obtenerFavoritos(String username){
        DB db=new DB();
        Connection c =db.connect();
        ArrayList<Integer> favoritos = new ArrayList<Integer>();
        try {

            ResultSet resultSet = db.execute("SELECT id_evento FROM getfavoritos('"+username+"');");
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

    public static boolean agregarNotificacionFav(int idEvento, String user,String tipo){
        DB db=new DB();
        Connection c =db.connect();
        try {
            ResultSet resultSet = db.execute("SELECT * FROM agregarnotificacionfav("+idEvento+",'"+user+"','"+tipo+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregarnotificacionfav");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean agregarNotificacionAdv(String user,String tipo,boolean estado){
        DB db=new DB();
        Connection c =db.connect();
        try {
            ResultSet resultSet = db.execute("SELECT * FROM agregarnotificacionadv('"+user+"','"+tipo+"','"+estado+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregarnotificacionadv");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean eliminarNotificacion(int idEvento, String user){
        DB db=new DB();
        Connection c =db.connect();
        try {/*
            CallableStatement oCall = c.prepareCall("{ ? = call quitarnotificacion(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,user);
            oCall.setInt(2,idEvento);
            ResultSet resultSet = oCall.executeQuery();
*/
            ResultSet resultSet = db.execute("SELECT * FROM quitarnotificacion("+idEvento+",'"+user+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("quitarnotificacion");
                    return resp;
                }
            }
            else
                return false;
        }catch (Exception e){

        }
        return false;
    }

    public static boolean consultarNotificacionFav(int idEvento, String user,String tipo){
        DB db=new DB();
        Connection c =db.connect();
        try {
            ResultSet resultSet = db.execute("SELECT * FROM saberestadonotificacionfav("+idEvento+",'"+user+"','"+tipo+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("saberestadonotificacionfav");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean consultarNotificacionAdv(String user,String tipo){
        DB db=new DB();
        Connection c =db.connect();
        try {
            ResultSet resultSet = db.execute("SELECT * FROM saberestadonotificacionadv('"+user+"','"+tipo+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("saberestadonotificacionadv");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }


    public static boolean cambiarEstadoRecibirNotificacion(boolean estado,String username){
        DB db=new DB();
        Connection c =db.connect();
        try {/*
            CallableStatement oCall = c.prepareCall("{ ? = call cambiarestadorecibirnotificacion(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,username);
            oCall.setBoolean(2,estado);
            ResultSet resultSet =oCall.executeQuery();*/
            ResultSet resultSet = db.execute("SELECT * FROM cambiarestadorecibirnotificacion("+estado+",'"+username+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("cambiarestadorecibirnotificacion");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static boolean estadoRecibirNotificacion(String username){
        DB db=new DB();
        Connection c =db.connect();
        try {/*
            CallableStatement oCall = c.prepareCall("{ ? = call estadorecibirnotificacion(?,?) }");
            oCall.registerOutParameter(1, Types.BOOLEAN);
            oCall.setString(3,username);
            oCall.setBoolean(2,estado);
            ResultSet resultSet =oCall.executeQuery();*/
            ResultSet resultSet = db.execute("SELECT * FROM estadorecibirnotificacion('"+username+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("estadorecibirnotificacion");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        return false;
    }

    public static int buscarDenunciasUsuario(String username){
        DB db=new DB();
        Connection c =db.connect();
    try {
        ResultSet resultSet = db.execute("SELECT * FROM buscarnumerodenunciasusuario('"+username+"');");
        if (resultSet != null)
        {
            while(resultSet.next()){
                int resp = resultSet.getInt("buscarnumerodenunciasusuario");
                return resp;
            }
        }
        else
            return 3;

    }catch (Exception e){

    }
    return 3;

    }

    public static boolean consultarUsuarioSiEstaBloqueado(String username){
        DB db=new DB();
        Connection c =db.connect();
        //false = no esta bloqueado. true = bloquado

        try {

            ResultSet resultSet = db.execute("SELECT * FROM consultarusuariobloqueado('"+username+"');");

            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("consultarusuariobloqueado");
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
