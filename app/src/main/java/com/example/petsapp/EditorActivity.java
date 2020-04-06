package com.example.petsapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.petsapp.data.PetContract;
import com.google.android.material.snackbar.Snackbar;

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
        setupSpinner();
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
                        gender = PetContract.PetsEntry.GENDER_MALE;
                    }else if (selected.equals(getString(R.string.gender_female))){
                        gender = PetContract.PetsEntry.GENDER_FEMALE;
                    }
                }else {
                    gender = PetContract.PetsEntry.GENDER_UNKNOWN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = PetContract.PetsEntry.GENDER_UNKNOWN;
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
                Snackbar.make(findViewById(R.id.activity_editor), "Data saved.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(EditorActivity.this,"Desfeito",Toast.LENGTH_LONG).show();
                            }
                        }).show();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
