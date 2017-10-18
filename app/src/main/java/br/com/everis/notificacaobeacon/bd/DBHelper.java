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
    private static final int DATABASE_VERSION = 3;

    public static final String REUNIAO_TABLE_NAME = "tblReuniao";
    public static final String REUNIAO_COLUMN_ID = "id";
    public static final String REUNIAO_COLUMN_ASSUNTO = "assunto";
    public static final String REUNIAO_COLUMN_HORA_INICIO = "hora_inicio";
    public static final String REUNIAO_COLUMN_HORA_TERMINO = "hora_termino";
    public static final String REUNIAO_COLUMN_LOCAL = "local";
    public static final String REUNIAO_COLUMN_SALA = "sala";
    public static final String REUNIAO_COLUMN_PARTICIPANTES = "participantes";
    public static final String REUNIAO_COLUMN_DETALHES = "detalhes";

    public static final String USUARIO_TABLE_NAME = "tblUsuarios";
    public static final String USUARIO_COLUMN_ID = "id";
    public static final String USUARIO_COLUMN_NOME = "nomeCompleto";
    public static final String USUARIO_COLUMN_EMAIL = "email";
    public static final String USUARIO_COLUMN_PERMISSAO_FK = "id_permissao_FK";
    public static final String USUARIO_COLUMN_CARGO_FK = "id_cargo_FK";

    public static final String QUALIFICACAO_TABLE_NAME = "tblUsuariosHasQualificacoes";
    public static final String QUALIFICACAO_COLUMN_ID = "idQualificacao";
    public static final String QUALIFICACAO_COLUMN_ID_USUARIO = "idUsuario";

    private static final String CREATE_REUNIAO_TABLE =
            "CREATE TABLE " + REUNIAO_TABLE_NAME + "(" +
                    REUNIAO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    REUNIAO_COLUMN_ASSUNTO + " TEXT NOT NULL," +
                    REUNIAO_COLUMN_HORA_INICIO + " TEXT NOT NULL, " +
                    REUNIAO_COLUMN_HORA_TERMINO + " TEXT NOT NULL, " +
                    REUNIAO_COLUMN_LOCAL + " TEXT NOT NULL, " +
                    REUNIAO_COLUMN_SALA + " TEXT NOT NULL, " +
                    REUNIAO_COLUMN_PARTICIPANTES + " TEXT, " +
                    REUNIAO_COLUMN_DETALHES + " TEXT NOT NULL );";

    private static final String CREATE_USUARIO_TABLE =
            "CREATE TABLE " + USUARIO_TABLE_NAME + "(" +
                    USUARIO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    USUARIO_COLUMN_NOME + " TEXT NOT NULL," +
                    USUARIO_COLUMN_EMAIL + " TEXT NOT NULL," +
                    USUARIO_COLUMN_PERMISSAO_FK + " TEXT NOT NULL," +
                    USUARIO_COLUMN_CARGO_FK + " TEXT );";

    private static final String CREATE_QUALIFICACAO_TABLE =
            "CREATE TABLE " + QUALIFICACAO_TABLE_NAME + "(" +
                    QUALIFICACAO_COLUMN_ID + " INTEGER PRIMARY KEY," +
                    QUALIFICACAO_COLUMN_ID_USUARIO + " INTEGER PRIMARY KEY );";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
//            db.execSQL(CREATE_REUNIAO_TABLE);
//            db.execSQL(CREATE_USUARIO_TABLE);
//            db.execSQL(CREATE_QUALIFICACAO_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +
//                ", which will destroy all old data");
//        db.execSQL("DROP TABLE IF EXISTS " + REUNIAO_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + USUARIO_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + QUALIFICACAO_TABLE_NAME);
//        onCreate(db);
    }
}
