package com.example.fdope.tresb.Factoria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import com.example.fdope.tresb.DB.ConsultasProductos;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

/**
 * Created by SS on 12-10-2016.
 */

public class FactoriaElectronica implements ProductosFactory {



    @Override
    public Producto crearProducto(String username,String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, byte[] img, int largo,int idEvento)  {

        //se crea el producto
        Celular celular = new Celular(username,nombre_categoria,  marca,  modelo,  precio,  proveedor, latLng,img,largo,idEvento);
        return celular;

    }

    @Override
    public Producto crearProducto(String username,String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, Bitmap img)  {

        Bitmap imgRecortada = resizeImage(img,100,140);
        String imagen = BitMapToString(imgRecortada);
        byte[] b = stringToByte(imagen);
        int largo = b.length;

        //se crea el producto
        Celular celular =  new Celular(username,nombre_categoria,  marca,  modelo,  precio,  proveedor, latLng,b,largo);
        return celular;

    }

    @Override
    public Producto guardarProductoBD(Producto producto)throws SQLException{
        int id;
        id=ConsultasProductos.agregarProducto(producto.mostrarCreadorPublicacion(), producto.mostrarMarca(), producto.mostrarmodelo(),
                producto.mostrarPrecio(), producto.mostrarCategoria(), producto.coordenadasProducto().latitude, producto.coordenadasProducto().longitude, producto.mostrarProveedor(), producto.mostrarImagen(), producto.mostrarImagen().length);
        producto.setIDEvento(id);
        return producto;
    }

    public static Bitmap resizeImage(Bitmap b, int w, int h) {

        // cargamos la imagen de origen
        Bitmap BitmapOrg = b;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,
                width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return resizedBitmap;

    }


    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public Bitmap getBitmap(byte[] bitmap) {
        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0, baos);
        byte [] b=baos.toByteArray();

        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        temp.getBytes().toString();
        return temp;
    }

    public byte[] stringToByte(String str){
        byte[] data = Base64.decode(str, Base64.DEFAULT);
        return data;
    }


}
