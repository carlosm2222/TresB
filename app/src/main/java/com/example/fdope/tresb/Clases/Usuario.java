package com.example.fdope.tresb.Clases;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fdope on 30-10-2016.
 */

public class Usuario implements Parcelable {
    private String nombre, apellidos, email,password,username;

    public Usuario(String nombre, String apellidos, String email, String password, String username) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public  Usuario (){
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
    }

    protected Usuario(Parcel in) {
        this.nombre = in.readString();
        this.apellidos = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.username = in.readString();
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

