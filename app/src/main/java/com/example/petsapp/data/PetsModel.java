package com.example.petsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PetsModel {
    private PetDbHelper dbHelper;
    private SQLiteDatabase database;
    /** Constructor to access the database trhrough PetDbHelper*/
    // To access our database, we instantiate our subclass of SQLiteOpenHelper
    // and pass the context, which is the current activity.
    public PetsModel(Context context){
        dbHelper = new PetDbHelper(context);
    }
    public long insertDummyData(){
        // Configura o comando para escrever na tablea
        database = dbHelper.getWritableDatabase();
        // Cria os valores das colunas
        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);
        return database.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues);
    }
    public void readFromPetsdb(){
        // action
    }
    public long insertNewPet(ContentValues contentValues){
        database = dbHelper.getWritableDatabase();
        return database.insert(PetContract.PetEntry.TABLE_NAME,null,contentValues);
    }
}
