package com.jmgarzo.ratescar.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jmgarzo on 28/08/17.
 */

public class RatesCarContract {

    public static final String CONTENT_AUTHORITY ="com.jmgarzo.ratescar";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String sqlCreateCombustibles = "CREATE TABLE Combustibles (id_combustible INTEGER PRIMARY KEY, tipo TEXT, subtipo TEXT)";
    public static final class OperatorEntry implements BaseColumns {
        public static final String TABLE_NAME = "combustibles";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String _ID = "_id";
        public static final String TYPE = "type";
        public static final String SUBTYPE = "subtype";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
}
