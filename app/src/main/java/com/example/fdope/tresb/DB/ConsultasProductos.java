package com.example.fdope.tresb.DB;

import com.example.fdope.tresb.FactoriaProductos.FactoriaElectronica;
import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.example.fdope.tresb.Clases.Filtro;
import com.example.fdope.tresb.FactoriaProductos.ProductosFactory;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;


import java.sql.CallableStatement;

public class ConsultasProductos {

    public static ArrayList<Producto> listarProductos(){
        DB base = new DB();
        Connection c= base.connect();

        ArrayList<Producto> listap = new ArrayList<Producto>();
        try{
            //CallableStatement oCall = c.prepareCall("{call getINFO()}");
            //ResultSet resultado = oCall.executeQuery();

            ResultSet resultado = base.select("SELECT * FROM getINFO();");
            if(resultado !=null){

                while(resultado.next()){

                    String user = resultado.getString("usuario");
                    double lat = resultado.getDouble("latitud");
                    double lng = resultado.getDouble("longitud");
                    LatLng latLng = new LatLng(lat,lng);
                    int largo = resultado.getInt("largo");
                   byte[] img = resultado.getBytes("archivo");
                    String tipo = resultado.getString("nombre_categoria");
                    String marc = resultado.getString("marca");
                    int precio = resultado.getInt("precio");
                    String mode = resultado.getString("modelo");
                    String prov = resultado.getString("proveedor");
                    int idevento= resultado.getInt("id_evento");
                    if ( (tipo.equals("Smartphone")) && idevento!=0){
                        ProductosFactory pf = new FactoriaElectronica();
                        Producto producto = pf.crearProducto(user,tipo,marc,mode,precio,prov,latLng,img,largo,idevento);
                        listap.add(producto);
                    }

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static int agregarProducto(String user,String marca, String modelo, int precio , String tipo, double lat , double lng, String proveedor, byte[] img, int largo) {
        DB db=new DB();
        Connection c =db.connect();

        try {
            CallableStatement oCall=c.prepareCall("{ ? = call agregarevento(?,?,?,?,?,?,?,?,?,?)}");
            oCall.registerOutParameter(1,Types.INTEGER);
            oCall.setString(2,user);
            oCall.setDouble(3,lat);
            oCall.setDouble(4,lng);
            oCall.setString(5,tipo);
            oCall.setString(6,marca);
            oCall.setInt(7,precio);
            oCall.setString(8,modelo);
            oCall.setString(9,proveedor);
            oCall.setBytes(10,img);
            oCall.setInt(11,largo);
            ResultSet resultSet = oCall.executeQuery();

            if (resultSet != null)
            {
                while(resultSet.next()){
                    int resp = resultSet.getInt("agregarevento");
                    return resp;
                }
            }
            else
                return 0;

        }catch (Exception e){

        }
        db.desconectarBd();
        return 0;
    }

    public static ArrayList<Producto> listarRecientes(){
        DB base = new DB();
        Connection c= base.connect();

        ArrayList<Producto> listap = new ArrayList<Producto>();
        try{

            ResultSet resultado = base.select("SELECT * FROM ultimaspublicaciones();");
            if(resultado !=null){

                while(resultado.next()){

                    String user = resultado.getString("usuario");
                    double lat = resultado.getDouble("latitud");
                    double lng = resultado.getDouble("longitud");
                    LatLng latLng = new LatLng(lat,lng);
                    int largo = resultado.getInt("largo");
                    byte[] img = resultado.getBytes("archivo");
                    String tipo = resultado.getString("nombre_categoria");
                    String marc = resultado.getString("marca");
                    int precio = resultado.getInt("precio");
                    String mode = resultado.getString("modelo");
                    String prov = resultado.getString("proveedor");
                    int idevento= resultado.getInt("id_evento");
                    if ( (tipo.equals("Smartphone")) && idevento!=0){
                        ProductosFactory pf = new FactoriaElectronica();
                        Producto producto = pf.crearProducto(user,tipo,marc,mode,precio,prov,latLng,img,largo,idevento);
                        listap.add(producto);
                    }

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


    public static ArrayList<Producto> listarFiltrados(Filtro filtro) {
        DB db=new DB();
        Connection c =db.connect();

        try {
            ResultSet resultado = db.execute("SELECT * FROM getfiltroproducto('"+filtro.getMarca()+"','"+Integer.parseInt(filtro.getPrecioMin())+"','"+Integer.parseInt(filtro.getPrecioMax())+"');");
            if(resultado !=null){
                ArrayList<Producto> lista = new ArrayList<Producto>();
                while(resultado.next()) {
                    String user = resultado.getString("usuario");
                    double lat = resultado.getDouble("latitud");
                    double lng = resultado.getDouble("longitud");
                    LatLng latLng = new LatLng(lat,lng);
                    int largo = resultado.getInt("largo");
                    byte[] img = resultado.getBytes("archivo");
                    String tipo = resultado.getString("nombre_categoria");
                    String marc = resultado.getString("marca");
                    int precio = resultado.getInt("precio");
                    String mode = resultado.getString("modelo");
                    String prov = resultado.getString("proveedor");
                    int idevento= resultado.getInt("id_evento");
                    if ( (tipo.equals("Smartphone")) && idevento!=0){

                        ProductosFactory pf = new FactoriaElectronica();
                        Producto producto = pf.crearProducto(user,tipo,marc,mode,precio,prov,latLng,img,largo,idevento);
                        lista.add(producto);
                    }
                }
                return lista;
            }

        }catch (Exception e){

        }
        db.desconectarBd();

       return new ArrayList<Producto>();
    }

    public static boolean agregarDenuncia (int idEvento, String motivo){
        DB db= new DB();
        Connection c = db.connect();

        try {
            ResultSet resultSet = db.execute("SELECT * FROM agregardenuncia("+idEvento+",'"+motivo+"');");
            if (resultSet != null)
            {
                while(resultSet.next()){
                    boolean resp = resultSet.getBoolean("agregardenuncia");
                    return resp;
                }
            }
            else
                return false;

        }catch (Exception e){

        }
        db.desconectarBd();
        return false;
    }
}


