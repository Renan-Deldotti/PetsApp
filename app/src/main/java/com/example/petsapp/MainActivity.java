package com.example.petsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.example.petsapp.data.PetContract;
import com.example.petsapp.data.PetDbHelper;
import com.example.petsapp.data.PetProvider;
import com.example.petsapp.data.PetsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PetsModel petsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        petsModel = new PetsModel(this);
        //displayDatabaseInfo(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo(null);
    }

    private void displayDatabaseInfo(@Nullable Long rowId) {
        StringBuilder rowCount = new StringBuilder();
        TextView displayView = findViewById(R.id.text_view_pet);
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT
        };
        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI,projection,null,null,null);
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
            Log.e(this.getClass().getSimpleName(),"Something went wrong.");
        }finally {
            Log.e(this.getClass().getSimpleName(),""+rowCount);
            // Fecha a requisicao e libera recursos
            cursor.close();
        }
        /*String rowCount = petsModel.getRowCount();
        if (rowId != null){
            if (rowId == -1){
                Snackbar.make(findViewById(R.id.activity_main),"Erro ao inserir dados ficticios.",BaseTransientBottomBar.LENGTH_LONG).show();
            }else {
                Snackbar.make(findViewById(R.id.activity_main),"Dados inserido id:"+rowId,BaseTransientBottomBar.LENGTH_LONG).setAction("Desfazer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Dados apagados",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        }*/
        displayView.setText(rowCount.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu principal; e adiciona os itens na action bar (se exisitir).
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_insert_dummy_data) {
            long rowId = petsModel.insertDummyData();
            displayDatabaseInfo(rowId);
            return true;
        } else if (id == R.id.action_delete_all_entries) {
            Toast.makeText(this, "Deletando todos os animais...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
