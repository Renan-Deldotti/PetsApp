package com.example.petsapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.petsapp.data.PetContract;

class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    /** Cria uma nova view */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    /** Adiciona dados a view */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView = view.findViewById(R.id.summary);

        int nameIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);

        String name = cursor.getString(nameIndex);
        // Reduzido
        String breed = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED));
        if (TextUtils.isEmpty(breed))
            breed = "Unknown breed";

        nameTextView.setText(name);
        summaryTextView.setText(breed);
    }

    public String getPetName(int position){
        Cursor positionCursor = (Cursor) getItem(position);
        int cIndex = positionCursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        return positionCursor.getString(cIndex);
    }
}
