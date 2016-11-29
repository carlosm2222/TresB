package com.example.fdope.tresb.Clases;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.example.fdope.tresb.Factoria.Producto;
import com.example.fdope.tresb.MapsActivity;

import java.util.ArrayList;

/**
 * Created by fdope on 30-10-2016.
 */

public class Usuario implements Parcelable {
    private String nombre, apellidos, email,password,username;
    private ArrayList<Producto> listaFavoritos;
    private ArrayList<Producto> notificaciones;
    private double rating;

    public Usuario(String nombre, String apellidos, String email, String password, String username) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.username = username;
        this.listaFavoritos = new ArrayList<Producto>();
        this.notificaciones = new ArrayList<Producto>();
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        dest.writeDouble(this.rating);
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
        this.rating = in.readDouble();
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

    public Producto buscarFav(Producto p){
        for (int i=0; i<listaFavoritos.size() ; i++){
            if (listaFavoritos.get(i).mostrarCategoria().equals(p.mostrarCategoria()))
                if (listaFavoritos.get(i).coordenadasProducto().latitude == p.coordenadasProducto().latitude && listaFavoritos.get(i).coordenadasProducto().longitude == p.coordenadasProducto().longitude)
                    if (listaFavoritos.get(i).mostrarmodelo().equals(p.mostrarmodelo()))
                        if (listaFavoritos.get(i).mostrarMarca().equals(p.mostrarMarca()))
                            if (listaFavoritos.get(i).mostrarPrecio() == p.mostrarPrecio())
                                return listaFavoritos.get(i);
        }
        return null;
    }

    public int buscarPosFav(Producto p){
        for (int i=0; i<listaFavoritos.size() ; i++){
            if (listaFavoritos.get(i).mostrarCategoria().equals(p.mostrarCategoria()))
                if (listaFavoritos.get(i).coordenadasProducto().latitude == p.coordenadasProducto().latitude && listaFavoritos.get(i).coordenadasProducto().longitude == p.coordenadasProducto().longitude)
                    if (listaFavoritos.get(i).mostrarmodelo().equals(p.mostrarmodelo()))
                        if (listaFavoritos.get(i).mostrarMarca().equals(p.mostrarMarca()))
                            if (listaFavoritos.get(i).mostrarPrecio() == p.mostrarPrecio())
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
            if(ConsultasUsuarios.eliminarFav(username,p.mostrarIdEvento())){
                int pos = buscarPosFav(p);
                if(listaFavoritos.remove(pos)!= null)
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

}

