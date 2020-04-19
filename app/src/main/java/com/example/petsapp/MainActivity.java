package com.example.petsapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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

import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private static final int PET_LOADER_ID = 0;
    PetCursorAdapter petCursorAdapter;

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
        // Configura o listview para dados vazios
        listView = findViewById(R.id.listView);
        View emptyView = findViewById(R.id.empty_pet_list);
        listView.setEmptyView(emptyView);

        // Adiciona o adapter
        petCursorAdapter = new PetCursorAdapter(this,null);
        listView.setAdapter(petCursorAdapter);

        //Adiciona o Loader
        getLoaderManager().initLoader(PET_LOADER_ID,null,this);

        // Coloca ação de long click
        registerForContextMenu(listView);
    }
    /** Cria o menu de presisonamento longo */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.dropdown_listview,menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                // Passa o id na Uri para a Intent EditorActivity
                Uri uri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,adapterContextMenuInfo.id);
                intent.setData(uri);
                startActivity(intent);
                return true;
            case R.id.delete:
                String petName = petCursorAdapter.getPetName(adapterContextMenuInfo.position);
                int i = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, PetContract.PetEntry._ID + "=?",new String[]{String.valueOf(adapterContextMenuInfo.id)});
                if (i == 1)
                    Toast.makeText(this,"Pet "+petName+" deleted",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /** Cria os icones da navbar */
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
                final int petId = Integer.parseInt(Objects.requireNonNull(newUri.getLastPathSegment()).trim());
                Snackbar.make(findViewById(R.id.activity_main),"Fake pet added (id: "+petId+")",BaseTransientBottomBar.LENGTH_LONG).setAction("Desfazer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] toDelete = {String.valueOf(petId)};
                        int i = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, PetContract.PetEntry._ID + "=?",toDelete);
                        Toast.makeText(getApplicationContext(),"Dados apagados: "+i,Toast.LENGTH_LONG).show();
                    }
                }).show();
            }else{
                Snackbar.make(findViewById(R.id.activity_main),"Erro ao inserir dados ficticios.",BaseTransientBottomBar.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_delete_all_entries) {
            int i = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI,null,null);
            Toast.makeText(this, "Deletado "+i+" animais.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    /** Cria o loader */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {PetContract.PetEntry._ID, PetContract.PetEntry.COLUMN_PET_NAME, PetContract.PetEntry.COLUMN_PET_BREED};
        return new CursorLoader(this,PetContract.PetEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        petCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        petCursorAdapter.swapCursor(null);
    }
}
