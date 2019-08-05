package com.example.soportetecnico;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity {

    //XML variables
    private EditText nombres,apellidos,correo,pwd;
    private Spinner spin_sexo,spin_correo,spin_dia,spin_mes,spin_anio;
    //Variables de la base de datos
    private SQLiteDatabase db;
    //TAG
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Spinner en XML
        spin_sexo= (Spinner)findViewById(R.id.spinner_sexo);
        spin_correo= (Spinner)findViewById(R.id.spinner_correo);
        spin_dia= (Spinner)findViewById(R.id.spinner_dia);
        spin_mes= (Spinner)findViewById(R.id.spinner_mes);
        spin_anio= (Spinner)findViewById(R.id.spinner_año);
        //EditText en XML
        nombres = (EditText) findViewById(R.id.txt_nombres);
        apellidos = (EditText) findViewById(R.id.txt_apellidos);
        correo = (EditText) findViewById(R.id.txt_correo);
        pwd = (EditText) findViewById(R.id.txt_pwd);
        //Crear instancia de base de datos
        llenarSpin();
    }
    private void llenarSpin(){
        String [] sexo= {"Femenino","Masculino","Indeterminado"};
        ArrayAdapter <String> adapter1= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sexo);
        spin_sexo.setAdapter(adapter1);

        String [] correo= {"@hotmail.com","@gmail.com","@outlook.es","@outlook.com"};
        ArrayAdapter <String> adapter2= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,correo);
        spin_correo.setAdapter(adapter2);

        String [] dia= {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21",
                "22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter <String> adapter3= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dia);
        spin_dia.setAdapter(adapter3);

        String [] mes= {"Ene","Feb","Mar","Abr","Mayo","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
        ArrayAdapter <String> adapter4= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mes);
        spin_mes.setAdapter(adapter4);

        String [] anio= new String[44];
        int j=0;
        for (int i=2004;i>1960;i--){
            anio[j]=Integer.toString(i);
            j+=1;
        }
        ArrayAdapter <String> adapter5= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,anio);
        spin_anio.setAdapter(adapter5);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void añadirUsuario(View view){

        String nombres_db = nombres.getText().toString();
        String apellidos_db = apellidos.getText().toString();
        String pwd_db = pwd.getText().toString();
        String correo_db = correo.getText().toString()+spin_correo.getSelectedItem().toString();
        String fecha_db = spin_anio.getSelectedItem().toString() + "-" + spin_mes.getSelectedItem().toString() + "-" + spin_dia.getSelectedItem().toString();
        String sexo_db = spin_sexo.getSelectedItem().toString();
        if(!TextUtils.isEmpty(nombres_db)&&!TextUtils.isEmpty(apellidos_db)&&!TextUtils.isEmpty(pwd_db)&&!TextUtils.isEmpty(correo.getText())){
            db = LoginActivity.dataBaseManager.getWritableDatabase();
            db.execSQL("insert into usuarios (usuario,contrasena,nombres,apellidos,fecha_nacimiento,sexo)" +
                    "values('"+correo_db+"','"+pwd_db+"','"+nombres_db+"','"+apellidos_db+"','"+fecha_db+"','"+sexo_db+"')");
            db.close();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Ingrese datos validos", Toast.LENGTH_SHORT).show();
        }
    }
}
