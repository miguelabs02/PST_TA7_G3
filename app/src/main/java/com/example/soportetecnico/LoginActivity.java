package com.example.soportetecnico;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    EditText email, pwd;
    Button btn_sign_up, btnLogin;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_simple_dark);

        email =(EditText)findViewById(R.id.email);
        pwd =(EditText)findViewById(R.id.pwd);

        btnLogin = (Button) findViewById(R.id.btn_login);


        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //prueba
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


    }

    public void registrar_user(View view) {
        Intent i = new Intent(this, SignupActivity.class );
        startActivity(i);
    }



}
