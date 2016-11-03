package com.example.fdope.tresb.DB;

import com.example.fdope.tresb.Factoria.Celular;
import com.example.fdope.tresb.Factoria.Producto;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageReader;

import com.example.fdope.tresb.Factoria.Celular;
import com.example.fdope.tresb.Factoria.Producto;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.maps.model.LatLng;


import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConsultasProductos {

    public ConsultasProductos() {
    }

    public  Boolean guardarProducto(String marca, String modelo, int precio , String tipo, double lat , double lng, String proveedor, byte[] img, int largo){
        DB db= new DB();


        String comando = ("INSERT INTO producto (marca,modelo,precio,nombre_categoria,lat,lng,proveedor,img,largo) " +
                "VALUES ('" + marca + "','" + modelo + "','" + precio + "','" + tipo + "','"+lat+"','"+lng+"','"+proveedor+"','"+ img+"','"+ largo+"');");

        if (db.execute(comando) != null)
            return true;
        else
            return false;

    }
/*
    public boolean agregarProducto(String marca, String modelo, int precio , String tipo, double lat , double lng, String proveedor, byte[] img, int largo)throws SQLException {
        DB db = new DB();
        Connection c = db.connect();

        try {

            //byte[] b = new byte[largo];
            //  b=img.getBytes();

            PreparedStatement pstmt = c.prepareStatement("INSERT INTO producto (marca,modelo,precio,nombre_categoria,lat,lng,proveedor,img,largo) VALUES (?,?,?,?,?,?,?,?,?);");
            pstmt.setString(1,marca);
            pstmt.setString(2,modelo);
            pstmt.setInt(3,precio);
            pstmt.setString(4,tipo);
            pstmt.setDouble(5,lat);
            pstmt.setDouble(6,lng);
            pstmt.setBytes(7,img);
            // pstmt.setString(7,img);

            pstmt.setInt(8,largo);
            return pstmt.execute();


        }catch (Exception e){

        }

        String comando = ("INSERT INTO producto (marca,modelo,precio,nombre_categoria,lat,lng,proveedor,img,largo) " +
                "VALUES ('" + marca + "','" + modelo + "','" + precio + "','" + tipo + "','"+lat+"','"+lng+"','"+proveedor+"','"+ img+"','"+ largo+"');");

        if (db.execute(comando) != null)
            return true;
        else
            return false;

    }

*/

    public ArrayList<Producto> listarProductos()throws SQLException {
        DB base = new DB();

        ArrayList<Producto> listap = new ArrayList<Producto>();
        try{

            ResultSet resultado = base.select("SELECT * FROM producto;");
            if(resultado !=null){

                while(resultado.next()){

                    String tipo = resultado.getString("nombre_categoria");
                    String marc = resultado.getString("marca");
                    String mode = resultado.getString("modelo");
                    int precio = resultado.getInt("precio");
                    double lat = resultado.getDouble("lat");
                    double lng = resultado.getDouble("lng");
                    String prov = resultado.getString("proveedor");
                    LatLng latLng = new LatLng(lat,lng);
                    int largo = resultado.getInt("largo");
                    byte img[]=new byte[largo];
                    img = resultado.getBytes("img");

                    if(img != null){
                        Celular producto = new Celular("asd",tipo,marc,mode,precio,prov,latLng,img,largo);
                        listap.add(producto);
                    }
                    else
                        return null;


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


    public static Boolean agregarProducto(String user,String marca, String modelo, int precio , String tipo, double lat , double lng, String proveedor, byte[] img, int largo){
        DB db=new DB();
        Connection c =db.connect();

        try {
            CallableStatement oCall=c.prepareCall("{ ? = call agregarevento(?,?,?,?,?,?,?,?,?,?)}");
            oCall.registerOutParameter(1,Types.BOOLEAN);
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
            oCall.execute();

            return oCall.getBoolean(1);

        }catch (Exception e){

        }
        return false;
    }





























}


