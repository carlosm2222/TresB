package com.example.fdope.tresb;

import android.*;
import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fdope.tresb.Clases.Usuario;
import com.example.fdope.tresb.DB.ConsultasUsuarios;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements InfoPostDialog{
    private Profile profile;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private EditText user, pass;
    private Usuario usuario;
    static  final int GPS_PERMISSION_CODE = 23;
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
        if(!isGPSAccessAllowed())
            requestGPSPermission();

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
               // registrarUsuarioFacebookYEntrar(newProfile);
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
                profile = Profile.getCurrentProfile();
                if (profile!=null) {
                    //registrarUsuarioFacebookYEntrar(profile);

                }
                else
                    Toast.makeText(getApplicationContext(), "NOSE PUDO OBTENER PERFIL DE FACEBOOK", Toast.LENGTH_SHORT).show();
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

        if( ConsultasUsuarios.checkUsuarioFacebook(profile.getId())){ // SI YA ESTA EN LA BD SOLO ENTRA
            nextActivity(ConsultasUsuarios.obtenerUsuarioFacebook(profile.getId()));
        }
        else { // SE GUARDA EL USUARIO QUE ENTRA CON FACEBOOK EN LA BD
            mostrarVentanaObtenerUsuarioFacebook();
        }
    }

    private void nextActivity(Usuario u) {
        if (u!=null){
            if (! u.saberEstadoBloqueo()){
                Intent main = new Intent(this, InicioActivity.class);
                main.putExtra("UsuarioIn",u);
                startActivity(main);
                finish();
            }
            else
                Toast.makeText(this," Tu cuenta esta bloqueada ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        profile = Profile.getCurrentProfile();
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

        } else
            Toast.makeText(getApplicationContext(), "Credenciales no coinciden.", Toast.LENGTH_SHORT).show();

    }


    private Usuario validarCredenciales(String u, String pass) {

        if (consulta(u,pass)){
            if (obtenerUsuario(u) != null) {
                Toast.makeText(this,"Cargando mapa ...",Toast.LENGTH_SHORT).show();
                return obtenerUsuario(u);
            }
            else
                return  null;
        }
        else
            return null;
    }

    private boolean consulta(String u, String pass) {
        //consulta bd
        if ( ConsultasUsuarios.checkUsuario(u,pass) )
            return true;
        else
            return  false;

    }

    private Usuario obtenerUsuario(String u) {
            return ConsultasUsuarios.obtenerUsuario(u);
    }

    public void mostrarVentanaObtenerUsuarioFacebook(){
        FragmentManager fm = getFragmentManager();
        VentanaUsuarioFacebook dialogFragment = new VentanaUsuarioFacebook();
        dialogFragment.show(fm, "Sample Fragment");
    }

    @Override
    public void postIngresarUsuarioFacebook(String username) {

            if (!username.equals("")){
                if (!ConsultasUsuarios.verificarNombreUsuario(username)){
                    if(ConsultasUsuarios.registrar(profile.getFirstName(), profile.getLastName(), "email", "pass", username,profile.getId())){
                        Toast.makeText(getApplicationContext(), "Grasias por registrarte", Toast.LENGTH_SHORT).show();
                        nextActivity(ConsultasUsuarios.obtenerUsuario(username));
                    }
                }
                else {
                    Toast.makeText(this, "Nombre no disponible", Toast.LENGTH_SHORT).show();
                }
            }

    }

    @Override
    public void salir(boolean salir) {
        if (salir){
            accessTokenTracker.stopTracking();
            profileTracker.stopTracking();
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    public void onFinishDialogFavorito(boolean flag) {

    }

    @Override
    public void onFinishDialogComparar(boolean flag) {

    }
    //We are calling this method to check the permission status
    private boolean isGPSAccessAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestGPSPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},GPS_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(getApplicationContext(), "Permiso denegado. No podrá acceder a Tres B.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}






