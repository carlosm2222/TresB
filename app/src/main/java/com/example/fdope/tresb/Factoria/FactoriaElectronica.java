package com.example.fdope.tresb.Factoria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.fdope.tresb.Clases.SaveImage;
import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.FormularioActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by SS on 12-10-2016.
 */

public class FactoriaElectronica implements ProductosFactory {
    ConsultasProductos consultasProductos;


    @Override
    public Celular crearProducto(String username,String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, byte[] img, int largo) {

        //se crea el producto
        Celular celular = new Celular(username,nombre_categoria,  marca,  modelo,  precio,  proveedor, latLng,img,largo);

        //se guarda el producto
        if (consultasProductos.agregarProducto(username,marca,modelo,precio,nombre_categoria,celular.getLatitud(),celular.getLongitud(),proveedor,img,largo))
            return celular;
        else
            return null;
    }

    @Override
    public Celular crearProducto(String username,String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, Bitmap img) {
        byte[] b = getByteArray(img);
        int largo = b.length;

        //se crea el producto
        Celular celular =  new Celular(username,nombre_categoria,  marca,  modelo,  precio,  proveedor, latLng,b,largo);
        //se guarda
        if (consultasProductos.agregarProducto(username,marca,modelo,precio,nombre_categoria,celular.getLatitud(),celular.getLongitud(),proveedor,getByteArray(img),celular.getLargo()))
            return celular;
        else
            return null;
    }



    public FileInputStream convertirImg(){
        File f=null;
        FileInputStream s=null ;
        try {

            f = new File("/sdstore/prueba.png");
            s = new FileInputStream(f);
            return  s;

        }catch (Exception e){

        }
        return s;


    }

    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


    public Bitmap getBitmap(byte[] bitmap) {

        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
    }

    public InputStream ByteArrToInputStream (byte[] by){


        byte[] content = by;
        int size = content.length;
        InputStream is = null;
        byte[] b = new byte[size];
        try {
            is = new ByteArrayInputStream(content);
            is.read(b);
            System.out.println("Data Recovered: "+new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(is != null) is.close();
                return is;
            } catch (Exception ex){

            }
        }
        return is;
    }



    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();

        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        temp.getBytes().toString();
        return temp;
    }


    public byte[] stringToByte(String str){
        byte[] data = Base64.decode(str, Base64.DEFAULT);
        return data;
    }

    public FileOutputStream prueba (Bitmap bmp){

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sdstore/prueba.png");
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;

    }

}
