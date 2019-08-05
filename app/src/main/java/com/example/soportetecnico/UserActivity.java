package com.example.soportetecnico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
    }

    public void cerrarSesion(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
