package com.example.fdope.tresb.Clases;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fdope on 16-11-2016.
 */

public class Comentario {
    private String usuario;
    private String comentario;
    private boolean like;
    public Comentario(String usuario,String comentario, boolean like){
        this.usuario=usuario;
        this.comentario=comentario;
        this.like=like;
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

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
