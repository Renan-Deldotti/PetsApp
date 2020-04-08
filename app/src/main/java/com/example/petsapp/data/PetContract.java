package com.example.petsapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/** Define as variaveis para a tabela Pets */
public final class PetContract {

    /** Empty Constructor */
    private PetContract(){}

    /** Cria as variaveis do Content Provider */
    public static final String CONTENT_AUTHORITY = "com.example.petsapp.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";

    public static final class PetEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

        /** Define o nome da tabela e das colunas */
        public static final String TABLE_NAME = "pets";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        /** CONSTANTES DE DEFINICAO */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
