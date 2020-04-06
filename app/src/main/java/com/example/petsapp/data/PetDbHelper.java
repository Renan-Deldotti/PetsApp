package com.example.petsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {
    /** Database Version and Name */
    public static final String DATABASE_NAME = "shelter.db";
    public static final int DATABASE_VERSION = 1;
    /** Constructor method */

    public PetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE pets (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,breed TEXT,gender INTEGER NOT NULL,weight INTEGER NOT NULL DEFAULT 0);
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetContract.PetsEntry.TABLE_NAME + "("
                + PetContract.PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetsEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetContract.PetsEntry.COLUMN_PET_BREED + " TEXT, "
                + PetContract.PetsEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetContract.PetsEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0"
                + ");";
        db.execSQL(SQL_CREATE_PETS_TABLE);
        Log.e(PetDbHelper.class.getSimpleName(),"SQL da tabela criada = "+SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
