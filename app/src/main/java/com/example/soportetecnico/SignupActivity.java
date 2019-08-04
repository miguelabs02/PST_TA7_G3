package com.example.soportetecnico;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignupActivity extends AppCompatActivity {

    private Spinner spinner1,spinner2,spinner3,spinner4,spinner5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spinner1= (Spinner)findViewById(R.id.spinner1);
        spinner2= (Spinner)findViewById(R.id.spinner2);
        spinner3= (Spinner)findViewById(R.id.spinner3);
        spinner4= (Spinner)findViewById(R.id.spinner4);
        spinner5= (Spinner)findViewById(R.id.spinner5);

        String [] sexo= {"Femenino","Masculino","Indeterminado"};
        ArrayAdapter <String> adapter1= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sexo);
        spinner1.setAdapter(adapter1);

        String [] correo= {"@hotmail.com","@gmail.com","@outlook.es","@outlook.com"};
        ArrayAdapter <String> adapter2= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,correo);
        spinner2.setAdapter(adapter2);

        String [] dia= {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21",
        "22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter <String> adapter3= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dia);
        spinner3.setAdapter(adapter3);

        String [] mes= {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre",
        "Diciembre"};
        ArrayAdapter <String> adapter4= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mes);
        spinner4.setAdapter(adapter4);

        String [] anio= new String[44];
        int j=0;
        for (int i=2004;i>1960;i--){
              anio[j]=Integer.toString(i);
              j+=1;
          }
        ArrayAdapter <String> adapter5= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,anio);
        spinner5.setAdapter(adapter5);
    }
}
