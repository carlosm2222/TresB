package com.example.fdope.tresb.Clases;

import com.example.fdope.tresb.Factoria.Producto;

import java.util.ArrayList;

/**
 * Created by SS on 06-10-2016.
 */

public class TresB {

    private String nombre;
    private ArrayList<Producto> listaSmartphone;



    public TresB() {
        this.nombre = "TRES B";
        this.listaSmartphone = new ArrayList<Producto>();
    }

    public ArrayList<Producto> getListaProductos() {
        return listaSmartphone;
    }

    public void setListaSmartphone(ArrayList<Producto> listaSmartphone) {
        this.listaSmartphone = listaSmartphone;
    }
    public void addProducto(Producto p){
        this.listaSmartphone.add(p);
    }
}
