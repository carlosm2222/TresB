package com.example.fdope.tresb.FactoriaProductos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 */
public class Celular implements Producto, Parcelable {

    private String nombre_categoria;
    private String marca;
    private String modelo;
    private int precio;
    private double latitud;
    private double longitud;
    private String proveedor;
    private byte[] img;
    private int largo;
    private String usuario;
    private int idEvento;


    public Celular(String usuario, String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, byte[] img, int largo, int idEvento) {
        this.nombre_categoria = nombre_categoria;
        this.precio = precio;
        this.modelo = modelo;
        this.marca = marca;
        this.proveedor = proveedor;
        this.latitud = latLng.latitude;
        this.longitud = latLng.longitude;
        this.img = img;
        this.largo = largo;
        this.usuario = usuario;
        this.idEvento=idEvento;

    }

    public Celular(LatLng latLng) {
        this.latitud = latLng.latitude;
        this.longitud = latLng.longitude;
    }

    public Celular(String usuario, String nombre_categoria, String marca, String modelo, int precio, String proveedor, LatLng latLng, byte[] img, int largo) {
        this.nombre_categoria = nombre_categoria;
        this.precio = precio;
        this.modelo = modelo;
        this.marca = marca;
        this.proveedor = proveedor;
        this.latitud = latLng.latitude;
        this.longitud = latLng.longitude;
        this.img = img;
        this.largo = largo;
        this.usuario = usuario;
        this.idEvento=0;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }





    @Override
    public byte[] mostrarImagen() {
        return img;
    }

    @Override
    public LatLng coordenadasProducto() {
        return new LatLng(latitud, longitud);
    }

    @Override
    public String mostrarMarca() {
        return marca;
    }

    @Override
    public int mostrarPrecio() {
        return  precio;
    }
    @Override
    public String mostrarCreadorPublicacion(){
        return usuario;
    }
    @Override
    public String mostrarProveedor(){
        return proveedor;
    }

    @Override
    public String mostrarmodelo(){
        return modelo;
    }
    @Override
    public int largoImg(){
        return largo;
    }

    @Override
    public String mostrarCategoria() {
        return nombre_categoria;
    }

    @Override
    public int mostrarIdEvento() {
        return idEvento;
    }

    @Override
    public void setIDEvento(int id) {
        idEvento=id;
    }

    public Bitmap getBitmap(byte[] bitmap) {

        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre_categoria);
        dest.writeString(this.marca);
        dest.writeString(this.modelo);
        dest.writeInt(this.precio);
        dest.writeDouble(this.latitud);
        dest.writeDouble(this.longitud);
        dest.writeString(this.proveedor);
        dest.writeByteArray(this.img);
        dest.writeInt(this.largo);
        dest.writeString(this.usuario);
        dest.writeInt(this.idEvento);
    }

    protected Celular(Parcel in) {
        this.nombre_categoria = in.readString();
        this.marca = in.readString();
        this.modelo = in.readString();
        this.precio = in.readInt();
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
        this.proveedor = in.readString();
        this.img = in.createByteArray();
        this.largo = in.readInt();
        this.usuario = in.readString();
        this.idEvento = in.readInt();
    }

    public static final Creator<Celular> CREATOR = new Creator<Celular>() {
        @Override
        public Celular createFromParcel(Parcel source) {
            return new Celular(source);
        }

        @Override
        public Celular[] newArray(int size) {
            return new Celular[size];
        }
    };
}