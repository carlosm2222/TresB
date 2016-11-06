package com.example.fdope.tresb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.DB.ConsultasLogin;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class LoginActivity extends AppCompatActivity {
    public static final int  reques_usuario = 3;
    private ConsultasLogin consultasLogin;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private EditText user, pass;
    private Usuario usuario;
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
    //Facebook login button
    private FacebookCallback<LoginResult> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                registrarUsuarioFacebookYEntrar(newProfile);;
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions("user_friends", "email");
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
             AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                registrarUsuarioFacebookYEntrar(profile);

                Toast.makeText(getApplicationContext(), "Cargando mapa...", Toast.LENGTH_SHORT).show();
                /*GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        usuario = getFacebookData(object);
                        nextActivity(usuario);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();*/
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
            }
        };

        loginButton.registerCallback(callbackManager, callback);

        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.password);
        user.addTextChangedListener(mTextWatcher);     //Si los campos están vacios el botón iniciar sesion estará desactivado.
        pass.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();


    }

    public void registrarUsuarioFacebookYEntrar(Profile profile){
        //GUARDAR PERFIL FACEBOOK EN BD E INGRESO
        if(consultasLogin.checkUsuario(profile.getName(),profile.getId())){ // SI YA ESTA EN LA BD SOLO ENTRA
            nextActivity(consultasLogin.obtenerUsuario(profile.getName()));
        }
        else { // SE GUARDA EL USUARIO QUE ENTRA CON FACEBOOK EN LA BD
            consultasLogin.registrar(profile.getFirstName(),profile.getLastName(),"",profile.getId(),profile.getName());
            nextActivity(consultasLogin.obtenerUsuario(profile.getName()));
        }
    }

    private void nextActivity(Usuario u) {
        Intent main = new Intent(this, MapsActivity.class);
        main.putExtra("UsuarioIn",u);
        startActivity(main);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if (profile!=null)      //Si hay un perfil de fb activo se envian los datos del perfil
            registrarUsuarioFacebookYEntrar(profile);
        else if (usuario!=null) //si hay un perfil de usuario activo se envian datos del usuario
            nextActivity(usuario);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }


    public void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.login);

        String user = this.user.getText().toString();   //Método que valida si los campos del login están vacios o no
        String pass = this.pass.getText().toString();

        if (user.equals("") || pass.equals("")) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
        }
    }

    public void cargarFormularioDeRegistro(View v) {
        Intent main = new Intent(this, FormularioRegistro.class);
        startActivity(main);
    }

    public void iniciarSesion(View v) {
        Usuario u= validarCredenciales(user.getText().toString(),pass.getText().toString());
        if (u!=null) {//Validar datos en BD
            //setear en usuario nombre y apellido
            usuario=u;
            nextActivity(u);
        } else
            Toast.makeText(getApplicationContext(), "Credenciales no coinciden.", Toast.LENGTH_SHORT).show();

    }


    private Usuario validarCredenciales(String u, String pass) {

        if (consulta(u,pass)){
            if (obtenerUsuario(u) != null) {
                Toast.makeText(this,"Cargando mapa",Toast.LENGTH_SHORT).show();
                return obtenerUsuario(u);
            }
            else {
                return  null;
            }
        }
        else
            return null;
    }

    private boolean consulta(String u, String pass) {
        //consulta bd
        if(consultasLogin.checkUsuario(u,pass)) {
            return true;
        }
        else {
            return false;
        }
    }

    private Usuario obtenerUsuario(String u) {
        consultasLogin.obtenerUsuario(u);
            return consultasLogin.obtenerUsuario(u);
    }






   /* private void nextActivity() {   //Falta enviar datos de usuario a la siguiente actividad
        Intent main = new Intent(this, MapsActivity.class);
        startActivity(main);
        finish();
    }*/

    private Usuario getFacebookData(JSONObject object) {


        Usuario u = new Usuario();
        String id = null;
        try {
            id = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (object.has("first_name"))
            try {
                usuario.setNombre(object.getString("first_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        if (object.has("last_name"))
            try {
                usuario.setApellidos(object.getString("last_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        if (object.has("email"))
            try {
                usuario.setEmail(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    return u;
    }
}






