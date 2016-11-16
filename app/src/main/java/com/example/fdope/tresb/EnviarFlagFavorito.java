package com.example.fdope.tresb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fdope.tresb.Factoria.Producto;

import java.util.List;

/**
 * Created by SS on 13-11-2016.
 */

public interface EnviarFlagFavorito {
    void onFinishDialog(boolean flag);

}