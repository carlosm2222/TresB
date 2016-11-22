package com.example.fdope.tresb.Clases;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fdope on 04-11-2016.
 */

public class Filtro implements Parcelable {
    public String marca,tipo,precioMin,precioMax;

    public Filtro(String marca, String tipo, String precioMax, String precioMin) {
        setMarca(marca);
        setTipo(tipo);
        setPrecioMax(precioMax);
        setPrecioMin(precioMin);
    }

    protected Filtro(Parcel in) {
        marca = in.readString();
        tipo = in.readString();
        precioMin = in.readString();
        precioMax = in.readString();
    }

    public static final Creator<Filtro> CREATOR = new Creator<Filtro>() {
        @Override
        public Filtro createFromParcel(Parcel in) {
            return new Filtro(in);
        }

        @Override
        public Filtro[] newArray(int size) {
            return new Filtro[size];
        }
    };

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPrecioMin(String precioMin) {
        this.precioMin = precioMin;
    }

    public void setPrecioMax(String precioMax) {
        this.precioMax = precioMax;
    }

    public String getMarca() {

        return marca;
    }

    public String getPrecioMin() {
        return precioMin;
    }

    public String getPrecioMax() {
        return precioMax;
    }

    public String getTipo() {

        return tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(marca);
        dest.writeString(tipo);
        dest.writeString(precioMin);
        dest.writeString(precioMax);
    }
}
