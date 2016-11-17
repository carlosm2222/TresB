package com.example.fdope.tresb.Clases;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fdope on 16-11-2016.
 */

public class Comentario {
    private String usuario;
    private String comentario;

    public Comentario(String usuario,String comentario){
        this.usuario=usuario;
        this.comentario=comentario;
    }



    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }



}
