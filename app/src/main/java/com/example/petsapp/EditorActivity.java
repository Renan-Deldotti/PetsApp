package com.example.petsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.petsapp.data.PetContract;

import java.util.Objects;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /** Edita ou cria um novo Pet */
    private EditText nameEditText, breedEditText, weightEditText;
    private Spinner genderSpinner;
    /** Sexo -> 0 = indefinido, 1 = masculino, 2 = feminino */
    private int gender = PetContract.PetEntry.GENDER_UNKNOWN;
    private Uri petUri;
    private int loaderId = 1;
    private boolean isNewPet = true;
    private boolean hasChangedPetInfo = false;
    private String actualPetName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        nameEditText = findViewById(R.id.edit_pet_name);
        breedEditText = findViewById(R.id.edit_pet_breed);
        weightEditText = findViewById(R.id.edit_pet_weight);
        genderSpinner = findViewById(R.id.spinner_gender);

        nameEditText.setOnTouchListener(touchListener);
        breedEditText.setOnTouchListener(touchListener);
        weightEditText.setOnTouchListener(touchListener);
        genderSpinner.setOnTouchListener(touchListener);

        Intent intent = getIntent();
        petUri = intent.getData();

        if (petUri != null){
            setTitle("Edit pet");
            getLoaderManager().initLoader(loaderId,null,this);
            isNewPet = false;
            actualPetName = "Jhon";
        }else{
            setTitle("Add pet");
        }
        setupSpinner();

        final Button buttonTeste = findViewById(R.id.button_teste);
        /** Teste */
        buttonTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setTitle("Chose a gender").setItems(R.array.array_gender_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 1:
                                buttonTeste.setText("Opt 1");
                                Toast.makeText(EditorActivity.this,"Selected: Male",Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                buttonTeste.setText("Opt 2");
                                Toast.makeText(EditorActivity.this,"Selected: Female",Toast.LENGTH_LONG).show();
                                break;
                            default:
                                buttonTeste.setText("Opt 0");
                                Toast.makeText(EditorActivity.this,"Selected: Unknown",Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!hasChangedPetInfo) {
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hasChangedPetInfo = false;
                onBackPressed();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hasChangedPetInfo = true;
            return false;
        }
    };

    /** Configura o dropdown do spinner (REMOVED SPINNER)*/
    private void setupSpinner() {
        // Cria o array para o spinner atraves do resource Arrays
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.array_gender_options,android.R.layout.simple_spinner_item);
        // Seta 1 linha por item no adapter
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Passa o adapter para o spinner
        genderSpinner.setAdapter(genderSpinnerAdapter);
        // Pega o item selecionado
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selected)){
                    if (selected.equals(getString(R.string.gender_male))){
                        gender = PetContract.PetEntry.GENDER_MALE;
                    }else if (selected.equals(getString(R.string.gender_female))){
                        gender = PetContract.PetEntry.GENDER_FEMALE;
                    }
                }else {
                    gender = PetContract.PetEntry.GENDER_UNKNOWN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = PetContract.PetEntry.GENDER_UNKNOWN;
            }
        });
    }

    /** Infla o menu do menu_editor */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        if (isNewPet){
            MenuItem delete = menu.findItem(R.id.action_delete);
            delete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isNewPet){
            MenuItem delete = menu.findItem(R.id.action_delete);
            delete.setVisible(false);
        }
        return true;
    }

    /** Designa acao ao botao clicado */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                savePet();
                return true;
            case R.id.action_delete:
                deleteThisPet();
                return true;
            case android.R.id.home:
                if (!hasChangedPetInfo){
                    // Vai para a Activity pai
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteThisPet() {
        if (isNewPet)
            return;
        int idToDelete = -1;
        if (petUri.getLastPathSegment() != null) {
            idToDelete = Integer.parseInt(petUri.getLastPathSegment());
        }
        if(idToDelete != -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this pet?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int deletedRows = getContentResolver().delete(petUri,null,null);
                    if(deletedRows == 1) {
                        Toast.makeText(EditorActivity.this, actualPetName + " deleted", Toast.LENGTH_LONG).show();
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }else {
                        Toast.makeText(EditorActivity.this,"Something wen wrong",Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton(android.R.string.no,null);
            builder.show();
            return;
        }
        Toast.makeText(EditorActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
    }

    private void savePet() {
        String petName = "" + nameEditText.getText().toString().trim();
        String petBreed = "" + breedEditText.getText().toString().trim();
        String pw = weightEditText.getText().toString().trim();
        int petWeight;
        try {
            petWeight = Integer.parseInt(pw);
        } catch (NumberFormatException e) {
            petWeight = 0;
        }
        if (!TextUtils.isEmpty(petName)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, petName);
            contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, petBreed);
            contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, gender);
            contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, petWeight);

            if(!isNewPet){
                int rowsUpdated = getContentResolver().update(petUri,contentValues,null,null);
                if(rowsUpdated == 1) {
                    Toast.makeText(this, "Pet updated :)", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(this,"Error try again :(",Toast.LENGTH_LONG).show();
                }
            }else{
                Uri newUri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);
                if (newUri != null) {
                    int returnedId = Integer.parseInt(Objects.requireNonNull(newUri.getLastPathSegment()));
                    switch (returnedId){
                        case -2:
                            Toast.makeText(this, "Invalid name.", Toast.LENGTH_LONG).show();
                            nameEditText.setError("Invalid name");
                            break;
                        case -3:
                            Toast.makeText(this, getString(R.string.editor_insert_pet_failed)+"(invalid gender).", Toast.LENGTH_LONG).show();
                            break;
                        case -4:
                            Toast.makeText(this, getString(R.string.editor_insert_pet_failed)+"(invalid weight).", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(this, getString(R.string.editor_insert_pet_successful), Toast.LENGTH_LONG).show();
                            finish();
                            break;
                    }
                }else {
                    Toast.makeText(this,getString(R.string.editor_insert_pet_failed),Toast.LENGTH_LONG).show();
                }
            }
        }else{
            nameEditText.setError("Name field required");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {PetContract.PetEntry._ID,PetContract.PetEntry.COLUMN_PET_NAME, PetContract.PetEntry.COLUMN_PET_BREED, PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.COLUMN_PET_WEIGHT};
        return new CursorLoader(this, petUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Sempre usar .moveToFirst()
        // Default index do cursor é -1
        if(data.moveToFirst()){
            // Adiciona o nome ao EditText de name
            int nameColumnIndex = data.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            String name = data.getString(nameColumnIndex);
            nameEditText.setText(name);
            actualPetName = name;
            // Adiciona os outros campos
            breedEditText.setText(data.getString(data.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED)));
            weightEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT))).trim());
            gender = data.getInt(data.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER));
            if(gender < 0 || gender >2 ){
                genderSpinner.setSelection(0);
            }else {
                genderSpinner.setSelection(gender);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText("");
        breedEditText.setText("");
        weightEditText.setText("");
        genderSpinner.setSelection(PetContract.PetEntry.GENDER_UNKNOWN);
    }
}
