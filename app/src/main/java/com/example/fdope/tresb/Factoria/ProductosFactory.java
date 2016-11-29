package com.example.fdope.tresb.Factoria;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.sql.SQLException;

/**
 * Created by SS on 12-10-2016.
 */

public interface ProductosFactory {

    public Producto crearProducto(String username,String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, byte[] img, int largo)  ;
    public Producto crearProducto(String username,String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, Bitmap img)  ;
    public Producto guardarProductoBD(Producto producto) throws SQLException;
}
