package com.example.petsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.petsapp.data.PetContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT
        };
        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI,projection,null,null,null);
        ListView listView = findViewById(R.id.listView);

        View emptyView = findViewById(R.id.empty_pet_list);
        listView.setEmptyView(emptyView);

        PetCursorAdapter adapter = new PetCursorAdapter(this,cursor);
        listView.setAdapter(adapter);
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
            ContentValues contentValues = new ContentValues();
            contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
            contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
            contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
            contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);
            Uri newUri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);
            if (newUri != null){
                int petId = Integer.parseInt(Objects.requireNonNull(newUri.getLastPathSegment()).trim());
                Snackbar.make(findViewById(R.id.activity_main),"Fake pet added (id: "+petId+")",BaseTransientBottomBar.LENGTH_LONG).setAction("Desfazer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Dados apagados",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }else{
                Snackbar.make(findViewById(R.id.activity_main),"Erro ao inserir dados ficticios.",BaseTransientBottomBar.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_delete_all_entries) {
            int i = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI,null,null);
            Toast.makeText(this, "Deletado "+i+" animais.", Toast.LENGTH_SHORT).show();
        }
        displayDatabaseInfo();
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {PetContract.PetEntry._ID, PetContract.PetEntry.COLUMN_PET_NAME, PetContract.PetEntry.COLUMN_PET_BREED};
        Cursor cursor = getContentResolver().query(PetContract.BASE_CONTENT_URI,projection,null,null,null);
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
