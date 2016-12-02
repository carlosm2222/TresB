package com.example.fdope.tresb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Comentario;
import com.example.fdope.tresb.Clases.ComentarioAdapter;
import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.DB.ConsultaComentarios;

import java.util.ArrayList;

/**
 * Created by fdope on 17-11-2016.
 */

public class ListViewComentarios extends AppCompatActivity {
    public ListView lista;
    private ArrayList<Comentario> list;
    private EditText inputComentario;
    private boolean flag,pressed=false;
    private ImageButton like, dislike;
    private int mg, nomg;
   // private Button enviar;
    private String username;
    private int idEvento;
    private TextView mgs,nomgs;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_comentarios);
        lista = (ListView) findViewById(R.id.listaProd);
        inputComentario = (EditText)findViewById(R.id.comentario) ;
        inputComentario.addTextChangedListener(mTextWatcher);
        like = (ImageButton)findViewById(R.id.botonlike);
        dislike= (ImageButton)findViewById(R.id.botondislike);
        checkFieldsForEmptyValues();
        Bundle bundle = getIntent().getExtras();
        idEvento = bundle.getInt("idEvento");
        username = bundle.getString("username");
        list= ConsultaComentarios.listarComentarios(idEvento);
        contarrMg();
        mgs = (TextView)findViewById(R.id.totalmg);
        mgs.setText("Comentarios postivos: "+mg);
        nomgs = (TextView)findViewById(R.id.totalnomg);
        nomgs.setText("Comentarios negativos: "+nomg);
        if (!list.isEmpty()){
             ComentarioAdapter listadapter = new ComentarioAdapter(ListViewComentarios.this,R.layout.lista_comentarios,list);
             lista.setAdapter(listadapter);
        } else
            Toast.makeText(this, "No hay comentarios para esta publicación.", Toast.LENGTH_SHORT).show();

    }

    private void contarrMg() {
            for (int i=0;i<list.size();i++)
                if (list.get(i).isLike())
                    mg+=1;
                else
                    nomg+=1;
    }


    public void checkFieldsForEmptyValues() {
        Button enviar = (Button)findViewById(R.id.enviarcomentario);
        String input = this.inputComentario.getText().toString();

        if (input.equals("")&&(pressed==false)) {
            enviar.setEnabled(false);
        } else if (input.equals("")&&(pressed==true)){
            enviar.setEnabled(false);
        }else if (!input.equals("")&&(pressed==false))
            enviar.setEnabled(false);
        else
            enviar.setEnabled(true);
    }

    public void accionBotonLike (View v){
        flag=true;
        pressed=true;
        //Toast.makeText(this,"Boton like",Toast.LENGTH_SHORT).show();
        like.setImageResource(R.mipmap.ic_like_pressed);
        dislike.setImageResource(R.mipmap.ic_dislike);
        inputComentario.setHint("Dinos por qué te gusta esta oferta...");
        checkFieldsForEmptyValues();
    }

    public void accionBotonDislike (View v){
        flag=false;
        pressed=true;
        //Toast.makeText(this,"Boton dislike",Toast.LENGTH_SHORT).show();
        like.setImageResource(R.mipmap.ic_like);
        dislike.setImageResource(R.mipmap.ic_dislike_pressed);
        inputComentario.setHint("Dinos por qué NO te gusta esta oferta...");
        checkFieldsForEmptyValues();
    }

    public void enviarComentario (View v){
        String input = this.inputComentario.getText().toString();
        if(ConsultaComentarios.publicarComentario(idEvento,input,username,flag)){
            Toast.makeText(this,"Comentario publicado.",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }

        else
            Toast.makeText(this,"No se pudo publicar su comentario",Toast.LENGTH_SHORT).show();
    }
}
