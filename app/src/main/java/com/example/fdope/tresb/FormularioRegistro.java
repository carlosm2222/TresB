package com.example.fdope.tresb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fdope.tresb.DB.ConsultasUsuarios;

public class FormularioRegistro extends AppCompatActivity {
    private EditText inputNombres, inputApellidos , inputUsuario, inputPassword,inputEmail,inputPass2;

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
        setContentView(R.layout.activity_formulario_registro);
        this.inputNombres = (EditText) findViewById(R.id.nombre);
        this.inputApellidos = (EditText) findViewById(R.id.apellidos);
        this.inputUsuario = (EditText) findViewById(R.id.usuario);
        this.inputPassword = (EditText) findViewById(R.id.pass);
        this.inputEmail = (EditText) findViewById(R.id.correo);
        this.inputPass2 = (EditText) findViewById(R.id.pass2);

        inputNombres.addTextChangedListener(mTextWatcher);
        inputApellidos.addTextChangedListener(mTextWatcher);
        inputUsuario.addTextChangedListener(mTextWatcher);
        inputPassword.addTextChangedListener(mTextWatcher);
        inputEmail.addTextChangedListener(mTextWatcher);
        inputPass2.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
    }

    public void checkFieldsForEmptyValues(){
        Button b = (Button) findViewById(R.id.registrarse);
        String nombres= this.inputNombres.getText().toString();
        String apellidos=this.inputApellidos.getText().toString();
        String usuario = this.inputUsuario.getText().toString();
        String pass = this.inputPassword.getText().toString();
        String email = this.inputEmail.getText().toString();
        String pass2 = this.inputPass2.getText().toString();

        if(nombres.equals("") ||apellidos.equals("") ||usuario.equals("")|| pass.equals("") || email.equals("")){
            b.setEnabled(false);
        } else {
            if (pass.equals(pass2))
                b.setEnabled(true);
            else
                Toast.makeText(this,"Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarUsuario (View v){
        String nombres= this.inputNombres.getText().toString();
        String apellidos=this.inputApellidos.getText().toString();
        String usuario = this.inputUsuario.getText().toString();
        String pass = this.inputPassword.getText().toString();
        String email = this.inputEmail.getText().toString();

        //guardar datos en bd
        if (ConsultasUsuarios.checkUsuario(usuario,pass))
            Toast.makeText(this,"Nombre de usuario no disponible",Toast.LENGTH_SHORT).show();
        else {
            if (ConsultasUsuarios.registrar(nombres, apellidos, email, pass, usuario)) {
                Toast.makeText(this,"Grasias por registrarte",Toast.LENGTH_SHORT).show();
                nextActtivity();
            }
        }
    }

    private void nextActtivity() {
        Intent main = new Intent(this, LoginActivity.class);
        startActivity(main);
        finish();
    }
}
