package br.com.everis.notificacaobeacon.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reuniao.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tblReuniao";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ASSUNTO = "assunto";
    public static final String COLUMN_HORA_INICIO = "hora_inicio";
    public static final String COLUMN_HORA_TERMINO = "hora_termino";
    public static final String COLUMN_LOCAL = "local";
    public static final String COLUMN_SALA = "sala";
    public static final String COLUMN_PARTICIPANTES = "participantes";
    public static final String COLUMN_DETALHES = "detalhes";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ASSUNTO + " TEXT NOT NULL," +
            COLUMN_HORA_INICIO + " TEXT NOT NULL, " +
            COLUMN_HORA_TERMINO + " TEXT NOT NULL, " +
            COLUMN_LOCAL + " TEXT NOT NULL, " +
            COLUMN_SALA + " TEXT NOT NULL, " +
            COLUMN_PARTICIPANTES + " TEXT, " +
            COLUMN_DETALHES + " TEXT NOT NULL )";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +
                ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
