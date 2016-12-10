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
    private String descripcion;
    private ArrayList<Producto> listaProductos;



    public TresB() {
        this.nombre = "TRES B";
        this.listaProductos = null;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    public void addProducto(Producto p){
        this.listaProductos.add(p);
    }

    public void obtenerProductos(){
        listaProductos =ConsultasProductos.listarProductos();
    }

    public int buscarYNotificarDenunciasUsuario(String username){

        int contDenuncias = ConsultasUsuarios.buscarDenunciasUsuario(username);
        return contDenuncias;
    }

    public ArrayList<Producto> obtenerFavs(ArrayList<Integer> idsEventos){

        ArrayList<Producto> favs = new ArrayList<Producto>();
        if (idsEventos != null) {
            for (int i = 0; i < listaProductos.size(); i++)
                for (int j = 0; j < idsEventos.size(); j++)
                    if (listaProductos.get(i).mostrarIdEvento() == idsEventos.get(j)) {
                        favs.add(listaProductos.get(i));
                    }
        }
          return favs;
    }

    public ArrayList<Producto> buscarProductoPorMarcaModelo(Producto p,String username){
        ArrayList<Producto> lista = new ArrayList<Producto>();

        //busco coincidencias
        for (int i = 0; i< listaProductos.size(); i++){
            if (p.mostrarMarca().equals(listaProductos.get(i).mostrarMarca()))
                if ( (p.mostrarmodelo().equals(listaProductos.get(i).mostrarmodelo())) && ( p.mostrarIdEvento() != listaProductos.get(i).mostrarIdEvento()) && ( !p.mostrarCreadorPublicacion().equals(username)) )
                    lista.add(listaProductos.get(i));
        }
        return lista;
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


    public Producto buscarEnNotificaciones(Producto p,ArrayList<Producto> notificaciones){
        for (int i=0; i<notificaciones.size() ; i++)
            if (notificaciones.get(i).mostrarIdEvento() == (p.mostrarIdEvento()))
                return notificaciones.get(i);

        return null;
    }


    public boolean buscarNotificacionFavBD(Producto p,String username){
        String tipo_not ="Favoritos";
        if (ConsultasUsuarios.consultarNotificacionFav(p.mostrarIdEvento(),username,tipo_not))
            return true;
        else
            return false;
    }

}
