package com.example.fdope.tresb.FactoriaProductos;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by SS on 12-10-2016.
 */

public interface Producto {

    public String mostrarMarca();
    public String mostrarmodelo();
    public int mostrarPrecio();
    public LatLng coordenadasProducto();
    public byte[] mostrarImagen();
    public String mostrarCreadorPublicacion();
    public String mostrarProveedor();
    public String mostrarCategoria();
    public int mostrarIdEvento();
    public void setIDEvento(int id);

}
