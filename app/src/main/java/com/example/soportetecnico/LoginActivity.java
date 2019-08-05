package com.example.soportetecnico;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText email, pwd;
    Button btn_sign_up, btnLogin;

    private GoogleApiClient googleApiClient;

    private SignInButton signInButtonGoogle;

    public static final int SIGN_IN_CODE = 777;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progressBar;

    public static RegisterActivity dataBaseManager;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_simple_dark);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseAuth = FirebaseAuth.getInstance();

        email =(EditText)findViewById(R.id.email);
        pwd =(EditText)findViewById(R.id.pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        if(dataBaseManager ==null){
            dataBaseManager = new RegisterActivity(this,"administrador",null,1);
        }
        signInButtonGoogle = (SignInButton) findViewById(R.id.signInButtonGoogle);
        signInButtonGoogle.setSize(SignInButton.SIZE_WIDE);
        signInButtonGoogle.setColorScheme(SignInButton.COLOR_DARK);
        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });



        // el oyente se encarga de dirigir al usuario al main
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { // si no es nulo el user, entonces ya podemos ir al main
                    goMainScreen();
                }
            }
        };

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * El resultado de autenticarse con Google api,
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(task);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, R.string.not_log_in+e.toString(), Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    /**
     * Método que maneja el resultado de las credenciales.
     * Entra con autenticación de Google a Firebase
     * @param result Resultado de google sign in
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Utiliza el objeto cuenta para crear una credencial
     * Mientras Google se comunica con nuestro Firebase, se muestra un progressbar
     * @param signInAccount Objeto cuenta
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        progressBar.setVisibility(View.VISIBLE);
        signInButtonGoogle.setVisibility(View.GONE);
        // obtener una credencial
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        // entrar a firebase con esta credencial
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            /**
             * Cuando se complete la tarea, si no fue exitosa mostrar un mensaje al user
             * @param task Objeto tarea, para saber si terminó
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                signInButtonGoogle.setVisibility(View.VISIBLE);
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.not_firebase_auth, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Redirige al user al main
     */
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     *  Para detener el oyente cuando cierre sesión
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    public void login_user(View view) {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = this.email.getText().toString().trim();
        String password = pwd.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }


        //loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            int pos = email.indexOf("@");
                            String user = email.substring(0, pos);
                            Toast.makeText(LoginActivity.this, "Bienvenido: " + LoginActivity.this.email.getText(), Toast.LENGTH_LONG).show();
                            //Intent intencion = new Intent(getApplication(), WellcomeActivity.class);
                            //intencion.putExtra(WellcomeActivity.user, user);
                            //startActivity(intencion);
                            Intent i= new Intent(getApplication(), UserActivity.class );
                            startActivity(i);


                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(LoginActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "No se pudo ingresar con ese usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
        validarLoginBaseDatos(email,password);
    }

    public void validarLoginBaseDatos(String email,String pwd){
        db = dataBaseManager.getReadableDatabase();
        Cursor fila = db.rawQuery(
                "select contrasena,nombres,apellidos,sexo from usuarios where usuario='"+email+"'",null);
        if(fila.moveToFirst()) {
            if (fila.getString(0).equals(pwd)) {
                Toast.makeText(this, "dentro", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserActivity.class);
                db.close();
                //Log.e(TAG, email + "\n" + pwd + "\n" + fila.getString(1) + "\n" + fila.getString(2) + "\n" + fila.getString(3));
                intent.putExtra("Nombres", fila.getString(1));
                intent.putExtra("Apellidos", fila.getString(2));
                intent.putExtra("Sexo", fila.getString(3));
                intent.putExtra("Correo", email);
                startActivity(intent);
            }
        }else{
            Toast.makeText(this, "No existe usuario con esos datos", Toast.LENGTH_SHORT).show();
        }
    }
    public void register_user(View view){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }
}
