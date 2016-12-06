package com.example.fdope.tresb;

import com.example.fdope.tresb.FactoriaProductos.Producto;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by SS on 25-11-2016.
 */

public interface EnviarInfoDesdeListsAdapter {
    public void productoEliminado(Producto delete);
    public void recibirCoordenadas(LatLng latLng);
}
