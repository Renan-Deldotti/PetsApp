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
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_NAME, "Toto");
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_BREED, "Terrier");
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_GENDER, PetContract.PetsEntry.GENDER_MALE);
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_WEIGHT, 7);
        long rowId = database.insert(PetContract.PetsEntry.TABLE_NAME, null, contentValues);
        return rowId;
    }
    public void readFromPetsdb(){
        // action
    }
    public String getRowCount(){
        String rowCount = "";
        // Configura o camando para ler os dados
        database = dbHelper.getReadableDatabase();
        // Executa o comando para receber todos os dados da tabela
        Cursor cursor = database.rawQuery("SELECT * FROM "+ PetContract.PetsEntry.TABLE_NAME,null);
        try {
            // Conta o numero de linhas da tabela
            rowCount = "" + cursor.getCount();
        }catch (Exception e){
            // Log de erro caso algo aconteca
            Log.e(PetsModel.class.getSimpleName(),"Something went wrong.");
        }finally {
            // Fecha a requisicao e libera recursos
            cursor.close();
        }
        return rowCount;
    }
}
