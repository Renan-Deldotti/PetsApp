package com.example.petsapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class PetProvider extends ContentProvider {

    /** Table pets schema
     * _id INTEGER PRIMARY KEY AUTOINCREMENT
     * name TEXT NOT NULL
     * breed TEXT
     * gender INTEGER NOT NULL
     * weight INTEGER NOT NULL DEFAULT 0 */

    private PetDbHelper dbHelper;
    private static final int PETS = 100;
    private static final int PET_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static  final String LOG_TAG = PetProvider.class.getSimpleName();

    static {
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS + "/#",PET_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // Cursor para receber o resultado da query
        Cursor cursor;
        // Verifica qual o codigo da Uri
        switch (uriMatcher.match(uri)){
            case PETS:
                // Se for PETS recebe todos os dados da tabela
                cursor = database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PET_ID:
                // Se for um ID de PETS recebe o pet com o determinado _ID
                // Para cada "?" no selection é necessario um selectionArgs
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
/** Insere um novo pet */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case PETS:
                String name = values.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
                if (!PetContract.isValidName(name)){
                    throw new IllegalArgumentException("Pet name required.");
                }
                Integer gender = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
                if(gender == null || !PetContract.isValidGender(gender)){
                    throw new IllegalArgumentException("Invalid pet gender.");
                }
                Integer weight = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
                if(weight != null && weight < 0){
                    throw new IllegalArgumentException("Invalid pet weight.");
                }
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                long id = database.insert(PetContract.PetEntry.TABLE_NAME,null,values);
                if(id == -1){
                    Log.e(LOG_TAG,"Fail to insert row from uri("+uri+")");
                    return null;
                }
                return ContentUris.withAppendedId(uri,id);
            default:
                throw new IllegalArgumentException("Insertion not supported ("+uri+").");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
