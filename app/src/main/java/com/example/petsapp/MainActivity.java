package com.example.petsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.example.petsapp.data.PetContract;
import com.example.petsapp.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PetDbHelper dbHelper;

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
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        dbHelper = new PetDbHelper(this);
        displayDatabaseInfo(null);
    }

    private void displayDatabaseInfo(@Nullable Long l) {
        // Create and/or open a database to read from it
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        TextView displayView = (TextView) findViewById(R.id.text_view_pet);
        String rowCount ="";

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + PetContract.PetsEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            rowCount = "" + cursor.getCount();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        if (l != null){
            if (l == -1){
                Snackbar.make(findViewById(R.id.activity_main),"Erro ao inserir dados ficticios.",BaseTransientBottomBar.LENGTH_LONG).show();
            }else {
                Snackbar.make(findViewById(R.id.activity_main),"Dados inserido id:"+l,BaseTransientBottomBar.LENGTH_LONG).setAction("Desfazer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Dados apagados",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        }
        displayView.setText("Number of rows in pets database table: " + rowCount);
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
            insertPetDummyData();
            return true;
        } else if (id == R.id.action_delete_all_entries) {
            Toast.makeText(this, "Deletando todos os animais...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertPetDummyData() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_NAME, "Toto");
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_BREED, "Terrier");
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_GENDER, PetContract.PetsEntry.GENDER_MALE);
        contentValues.put(PetContract.PetsEntry.COLUMN_PET_WEIGHT, 7);
        long l = database.insert(PetContract.PetsEntry.TABLE_NAME, null, contentValues);
        displayDatabaseInfo(l);
    }
}
