package com.example.petsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {
    /** Database Version and Name */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shelter.db";
    /** Constructor method */

    public PetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String s = "CREATE TABLE " + PetContract.PetsEntry.TABLE_NAME + "("
                + PetContract.PetsEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT"*/
        //db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
