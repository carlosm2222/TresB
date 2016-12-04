package com.example.fdope.tresb.Clases;

import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.example.fdope.tresb.FactoriaProductos.Producto;

import java.util.ArrayList;

/**
 * Created by SS on 06-10-2016.
 */

public class TresB {

    private String nombre;
    private ArrayList<Producto> listaSmartphone;



    public TresB() {
        this.nombre = "TRES B";
        this.listaSmartphone = null;
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

    public void obtenerProductos(){
        listaSmartphone=ConsultasProductos.listarProductos();
    }

    public int buscarYNotificarDenunciasUsuario(String username){

        int contDenuncias = ConsultasUsuarios.buscarDenunciasUsuario(username);
        return contDenuncias;
    }


}
