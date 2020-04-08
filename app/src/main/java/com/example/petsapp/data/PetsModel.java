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
    public String getRowCount(){
        /** Table pets schema
         * _id INTEGER PRIMARY KEY AUTOINCREMENT
         * name TEXT NOT NULL
         * breed TEXT
         * gender INTEGER NOT NULL
         * weight INTEGER NOT NULL DEFAULT 0 */
        StringBuilder rowCount = new StringBuilder();
        // Configura o camando para ler os dados
        database = dbHelper.getReadableDatabase();
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT
            };
        // Executa o comando para receber todos os dados da tabela
        Cursor cursor = database.query(PetContract.PetEntry.TABLE_NAME,projection,null,null,null,null,null);
        int i =0;
        try {
            // Conta o numero de linhas da tabela
            rowCount = new StringBuilder("Number of rows: " + cursor.getCount() + "\nPet table data:\n");
            rowCount.append(PetContract.PetEntry._ID + " - " + PetContract.PetEntry.COLUMN_PET_NAME + " - "
                    + PetContract.PetEntry.COLUMN_PET_BREED + " - " + PetContract.PetEntry.COLUMN_PET_GENDER + " - "
                    + PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n\n\n");
            int petIdCol = cursor.getColumnIndex(PetContract.PetEntry._ID);
            int petNameCol = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            int petBreedCol = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
            int petGenderCol = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
            int petWeightCol = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            while (cursor.moveToNext()){
                rowCount.append(cursor.getInt(petIdCol)).append("\t")
                        .append(cursor.getString(petNameCol)).append("\t")
                        .append(cursor.getString(petBreedCol)).append("\t")
                        .append(cursor.getInt(petGenderCol)).append("\t")
                        .append(cursor.getInt(petWeightCol)).append("\n");
            }
        }catch (Exception e){
            // Log de erro caso algo aconteca
            Log.e(PetsModel.class.getSimpleName(),"Something went wrong.");
        }finally {
            Log.e(PetsModel.class.getSimpleName(),""+rowCount);
            // Fecha a requisicao e libera recursos
            cursor.close();
        }
        return rowCount.toString();
    }
}
