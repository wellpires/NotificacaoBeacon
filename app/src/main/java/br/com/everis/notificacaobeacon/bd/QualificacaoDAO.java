package br.com.everis.notificacaobeacon.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.everis.notificacaobeacon.model.QualificacaoVO;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class QualificacaoDAO {

    private SQLiteDatabase database = null;
    private DBHelper dbHelper = null;

    private String[] tblQualificacaoColumns = {
            dbHelper.QUALIFICACAO_COLUMN_ID,
            dbHelper.QUALIFICACAO_COLUMN_ID_USUARIO
    };

    public QualificacaoDAO(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public void createQualificacao(QualificacaoVO qualificacao, Long idUsuario){
        open();
        ContentValues values = new ContentValues();
        values.put(dbHelper.QUALIFICACAO_COLUMN_ID, qualificacao.getIdQualificacao());
        values.put(dbHelper.QUALIFICACAO_COLUMN_ID_USUARIO, idUsuario);

        database.insert(DBHelper.QUALIFICACAO_TABLE_NAME, null, values);
        close();
    }

    public void deleteQualificacao(QualificacaoVO qualificacao){
        open();
        database.delete(DBHelper.QUALIFICACAO_TABLE_NAME, DBHelper.QUALIFICACAO_COLUMN_ID + " = " + qualificacao.getIdQualificacao(), null);
        close();
    }

    public List<QualificacaoVO> listarQualificacoes(){
//        database.rawQuery("SELECT " + DBHelper.QUALIFICACAO_COLUMN_ID);
        return null;
    }

}
