package com.example.petsapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditorActivity extends AppCompatActivity {
    //Edita ou cria um novo Pet
    private EditText nameEditText, breedEditText, weightEditText;
    private Spinner genderSpinner;
    // Sexo -> 0 = indefinido, 1 = masculino, 2 = feminino
    private int gender = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }
}
