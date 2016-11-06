package com.example.fdope.tresb.Factoria;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by SS on 12-10-2016.
 */

public interface Producto {


   // public Boolean guardarDatosBD() throws IOException, SQLException;
    public String mostrarMarca();
    public String mostrarmodelo();
    public int mostrarPrecio();
    public LatLng coordenadasProducto();
    public byte[] mostrarImagen();
    public int largoImg();
    public String mostrarCreadorPublicacion();
    public String mostrarProveedor();

}
