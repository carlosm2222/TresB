package com.example.fdope.tresb.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.example.fdope.tresb.FactoriaProductos.Producto;

import java.util.ArrayList;

/**
 * Created by fdope on 30-10-2016.
 */

public class Usuario implements Parcelable {
    private String nombre, apellidos, email,password,username;
    private ArrayList<Producto> listaFavoritos;
    private ArrayList<Producto> notificaciones;
    private boolean recibirNotificacion;
    private int numeroDenuncias;

    public Usuario(String nombre, String apellidos, String email, String password, String username) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.username = username;
        this.listaFavoritos = new ArrayList<Producto>();
        this.notificaciones = new ArrayList<Producto>();
        this.numeroDenuncias=3;
    }
    public Usuario(String nombre, String apellidos, String email, String password, String username,boolean recibirNotificacion, int numeroDenuncias ) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.username = username;
        this.listaFavoritos = new ArrayList<Producto>();
        this.notificaciones = new ArrayList<Producto>();
        this.recibirNotificacion = recibirNotificacion;
        this.numeroDenuncias=numeroDenuncias;
    }

    public ArrayList<Producto> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(ArrayList<Producto> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public ArrayList<Producto> getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(ArrayList<Producto> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

    public int getNumeroDenuncias() {
        return numeroDenuncias;
    }

    public void setNumeroDenuncias(int numeroDenuncias) {
        this.numeroDenuncias = numeroDenuncias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Producto buscarFav(Producto p){
        for (int i=0; i<listaFavoritos.size() ; i++){
            if (listaFavoritos.get(i).mostrarIdEvento() == p.mostrarIdEvento())
                                return listaFavoritos.get(i);
        }
        return null;
    }

    public Producto buscarFavPorID(int id){
        for (int i=0; i<listaFavoritos.size() ; i++){
            if (listaFavoritos.get(i).mostrarIdEvento() == id)
                                return listaFavoritos.get(i);
        }
        return null;
    }

    public int buscarPosFav(Producto p){
        for (int i=0; i<listaFavoritos.size() ; i++){
            if (listaFavoritos.get(i).mostrarIdEvento() == p.mostrarIdEvento())
                                return i;
        }
        return 0;
    }

    public boolean agregarFavorito(Producto p){
        if (buscarFav(p) == null){ // si no esta repetido se agrega
            if(ConsultasUsuarios.agregarFav(username,p.mostrarIdEvento())){
                listaFavoritos.add(p);
                return true;
            }
        }
        return false;
    }
    public boolean eliminarFavorito(Producto p){
        if (buscarFav(p)!=null){
            if( ConsultasUsuarios.eliminarFav(username,p.mostrarIdEvento()) ){
                int pos = buscarPosFav(p);
                if(listaFavoritos.remove(pos)!=null)
                    return true;
            }
        }
        return false;
    }
    public boolean eliminarFavPorID(int id){
        if (buscarFavPorID(id)!=null){
            if( ConsultasUsuarios.eliminarFav(username,id)){
                if(listaFavoritos.remove(buscarFavPorID(id)))
                    return true;
            }
        }
        return false;
    }

    public Producto buscarEnNotificaciones(Producto p){
        for (int i=0; i<notificaciones.size() ; i++)
            if (notificaciones.get(i).mostrarIdEvento() == (p.mostrarIdEvento()))
                return notificaciones.get(i);

        return null;
    }

    public boolean agregarNotificacion(Producto p){
        if(ConsultasUsuarios.agregarNotificacion(p.mostrarIdEvento(),username)) {
            notificaciones.add(p);
            return true;
        }
        else
            return false;
    }

    public boolean buscarNotificacionBD(Producto p){
        if (ConsultasUsuarios.consultarNotificacion(p.mostrarIdEvento(),username))
            return true;
        else
            return false;
    }

    public ArrayList<Integer> listarFavoritos(){
         ArrayList<Integer> ids = ConsultasUsuarios.obtenerFavoritos(username);
        return ids;
    }

    public boolean actYDesacNotificacion(boolean flag){

        if (ConsultasUsuarios.cambiarEstadoRecibirNotificacion(flag,username)) {
            recibirNotificacion = flag;
            return true;
        }else
            return false;

    }

    public boolean estadoRecibirNotificacion(){
        return ConsultasUsuarios.estadoRecibirNotificacion(username);
    }

    public boolean saberEstadoBloqueo(){
        if (ConsultasUsuarios.consultarUsuarioSiEstaBloqueado(username))
            return true;
        else
            return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.apellidos);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.username);
        dest.writeList(this.listaFavoritos);
        dest.writeList(this.notificaciones);
        dest.writeByte(this.recibirNotificacion ? (byte) 1 : (byte) 0);
        dest.writeInt(this.numeroDenuncias);
    }

    protected Usuario(Parcel in) {
        this.nombre = in.readString();
        this.apellidos = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.username = in.readString();
        this.listaFavoritos = new ArrayList<Producto>();
        in.readList(this.listaFavoritos, Producto.class.getClassLoader());
        this.notificaciones = new ArrayList<Producto>();
        in.readList(this.notificaciones, Producto.class.getClassLoader());
        this.recibirNotificacion = in.readByte() != 0;
        this.numeroDenuncias = in.readInt();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}

