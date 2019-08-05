package com.example.soportetecnico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class RegisterActivity extends SQLiteOpenHelper {

    public RegisterActivity(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuarios(usuario text primary key, contraseña text," +
                "nombres text, apellidos text, fecha_nacimiento text, producto text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Se utiliza el formato de fecha iso8601Format yyyy-MM-dd HH:mm:ss
     * @param context
     * @param fecha
     * @return
     */

}
