package com.example.petsapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.petsapp.data.PetContract;

import java.util.Objects;

public class EditorActivity extends AppCompatActivity {
    /** Edita ou cria um novo Pet */
    private EditText nameEditText, breedEditText, weightEditText;
    private Spinner genderSpinner;
    /** Sexo -> 0 = indefinido, 1 = masculino, 2 = feminino */
    private int gender = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        nameEditText = findViewById(R.id.edit_pet_name);
        breedEditText = findViewById(R.id.edit_pet_breed);
        weightEditText = findViewById(R.id.edit_pet_weight);
        genderSpinner = findViewById(R.id.spinner_gender);
        //setupSpinner();
        TextView catStyle = findViewById(R.id.randomIdGender);
        catStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setTitle("Chose a gender").setItems(R.array.array_gender_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditorActivity.this,"Index: "+which,Toast.LENGTH_LONG).show();
                        switch (which){
                            case 1:
                                gender = 1;
                                Toast.makeText(EditorActivity.this,"Index: "+which+" (M)",Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                gender = 2;
                                Toast.makeText(EditorActivity.this,"Index: "+which+" (F)",Toast.LENGTH_LONG).show();
                                break;
                            default:
                                gender = 0;
                                Toast.makeText(EditorActivity.this,"Index: "+which+" (IND)",Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    /** Configura o dropdown do spinner */
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
        return true;
    }

    /** Designa acao ao botao clicado */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                addNewPet();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewPet() {
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
        }else{
            nameEditText.setError("Name field required");
        }
    }
}
