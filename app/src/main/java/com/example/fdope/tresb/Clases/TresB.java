package com.example.fdope.tresb.Clases;

import com.example.fdope.tresb.DB.ConsultasProductos;
import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.example.fdope.tresb.InfoPostDialog;

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

    public ArrayList<Producto> obtenerFavs(ArrayList<Integer> idsEventos){

        ArrayList<Producto> favs = new ArrayList<Producto>();
        if (idsEventos != null) {
            for (int i = 0; i < listaSmartphone.size(); i++)
                for (int j = 0; j < idsEventos.size(); j++)
                    if (listaSmartphone.get(i).mostrarIdEvento() == idsEventos.get(j)) {
                        favs.add(listaSmartphone.get(i));
                    }
        }
          return favs;
    }

    public ArrayList<Producto> buscarProductoPorMarcaModelo(Producto p,String username){
        ArrayList<Producto> lista = new ArrayList<Producto>();

        //busco coincidencias
        for (int i = 0; i< listaSmartphone.size(); i++){
            if (p.mostrarMarca().equals(listaSmartphone.get(i).mostrarMarca()))
                if ( (p.mostrarmodelo().equals(listaSmartphone.get(i).mostrarmodelo())) && ( p.mostrarIdEvento() != listaSmartphone.get(i).mostrarIdEvento())  )
                    lista.add(listaSmartphone.get(i));
        }

        return lista;
        //&& ( !p.mostrarCreadorPublicacion().equals(username))
    }

    public boolean saberEstadoBloqueoDeUsuario(String username){

        if (ConsultasUsuarios.consultarUsuarioSiEstaBloqueado(username))
            return true;
        else
            return false;
    }

    public boolean agregarNotificacionAdv(String username){
        String tipo_not ="Advertencia";
        if(ConsultasUsuarios.agregarNotificacionAdv(username,tipo_not,true)) {
            return true;
        }
        else
            return false;
    }
    public boolean buscarNotificacionAdvBD(String username ){
        String tipo_not ="Advertencia";
        if (ConsultasUsuarios.consultarNotificacionAdv(username,tipo_not))
            return true; // esta pero hay que enviarla
        else
            return false;
    }
}
