package com.example.petsapp.data;

import android.provider.BaseColumns;

public final class PetContract {
    private PetContract(){}
    public static final class PetsEntry implements BaseColumns{
        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BREED = "breed";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_WEIGHT = "weight";

        /** CONSTANTES DE DEFINICAO */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
