package com.example.petsapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/** Define as variaveis para a tabela Pets */
public final class PetContract {

    /** Empty Constructor */
    private PetContract(){}

    /** Cria as constantes do Content Provider */
    public static final String CONTENT_AUTHORITY = "com.example.petsapp.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";

    /** Cria as constantes MIME */
    static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
    static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

    /** Cria o nome das colunas da tabela */
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
    static boolean isValidGender(int gender){
        return gender == PetEntry.GENDER_UNKNOWN || gender == PetEntry.GENDER_MALE || gender == PetEntry.GENDER_FEMALE;
    }
    static boolean isValidPetName(String name){
        if(!TextUtils.isEmpty(name)) {
            if (name.length() > 2) {
                return name.matches("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{2,}");
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    static boolean isValidWeight(int weight){
        return weight >= 0;
    }
    static boolean isValidBreed(String breed) {
        return true;
    }
}
