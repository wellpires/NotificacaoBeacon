package br.com.everis.notificacaobeacon.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public class DBAdapter {

    private SQLiteDatabase database = null;
    private DBHelper dbHelper = null;
    private String[] allColumns = {
            dbHelper.COLUMN_ID,
            dbHelper.COLUMN_ASSUNTO,
            dbHelper.COLUMN_HORA_INICIO,
            dbHelper.COLUMN_HORA_TERMINO,
            dbHelper.COLUMN_LOCAL,
            dbHelper.COLUMN_SALA,
            dbHelper.COLUMN_PARTICIPANTES,
            dbHelper.COLUMN_DETALHES
    };

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ReuniaoVO createReuniao(ReuniaoVO reuniao) throws ParseException {
        open();
        ContentValues values = new ContentValues();

        values.put(dbHelper.COLUMN_ASSUNTO, reuniao.getAssunto());
        values.put(dbHelper.COLUMN_HORA_INICIO, reuniao.getDtInicio());
        values.put(dbHelper.COLUMN_HORA_TERMINO, reuniao.getDtTermino());
        values.put(dbHelper.COLUMN_LOCAL, reuniao.getEndereco());
        values.put(dbHelper.COLUMN_SALA, reuniao.getSala());
        values.put(dbHelper.COLUMN_DETALHES, reuniao.getPauta());

        long insertId = database.insert(DBHelper.TABLE_NAME, null, values);
        Cursor c = database.query(DBHelper.TABLE_NAME, allColumns, DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        c.moveToFirst();
        close();
        return cursorParaReuniao(c);

    }

    public void deleteReuniao(Integer idReuniao) {
        database.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_ID + " = " + idReuniao, null);
    }

    public void updateReuniao(ReuniaoVO reuniao) {
        ContentValues values = new ContentValues();

        values.put(dbHelper.COLUMN_ASSUNTO, reuniao.getAssunto());
        values.put(dbHelper.COLUMN_HORA_INICIO, reuniao.getDtInicio());
        values.put(dbHelper.COLUMN_HORA_TERMINO, reuniao.getDtTermino());
        values.put(dbHelper.COLUMN_LOCAL, reuniao.getEndereco());
        values.put(dbHelper.COLUMN_SALA, reuniao.getSala());
        values.put(dbHelper.COLUMN_DETALHES, reuniao.getPauta());

        database.update(DBHelper.TABLE_NAME, values, dbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(reuniao.getIdReuniao())});
    }

    public void deleteReuniao() {
        open();
        database.delete(DBHelper.TABLE_NAME, null, null);
        close();
    }

    private ReuniaoVO cursorParaReuniao(Cursor c) throws ParseException {
        int index = 0;
        ReuniaoVO r = new ReuniaoVO();
        r.setIdReuniao(c.getInt(index++));
        r.setAssunto(c.getString(index++));
        r.setDtInicio(c.getString(index++));
        r.setDtTermino(c.getString(index++));
        r.setEndereco(c.getString(index++));
        r.setSala(c.getString(index++));
        r.setPauta(c.getString(index++));

        return r;

    }

    private List<ReuniaoVO> cursorParaReunioes(Cursor cursor) {
        List<ReuniaoVO> lstReunioes = new ArrayList<>();
        while (cursor.moveToNext()) {
            ReuniaoVO vo = new ReuniaoVO();
            vo.setIdReuniao(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))));
            vo.setAssunto(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ASSUNTO)));
            vo.setDtInicio(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_HORA_INICIO)));
            vo.setDtTermino(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_HORA_TERMINO)));
            vo.setEndereco(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LOCAL)));
            vo.setSala(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SALA)));
            vo.setPauta(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DETALHES)));
            lstReunioes.add(vo);
        }
        return lstReunioes;
    }

    public List<ReuniaoVO> getReunioes() {
        open();
        try {
            Cursor c = database.rawQuery("SELECT " +
                    DBHelper.COLUMN_ID + ", " +
                    DBHelper.COLUMN_ASSUNTO + ", " +
                    DBHelper.COLUMN_HORA_INICIO + ", " +
                    DBHelper.COLUMN_HORA_TERMINO + ", " +
                    DBHelper.COLUMN_LOCAL + ", " +
                    DBHelper.COLUMN_SALA + ", " +
                    DBHelper.COLUMN_PARTICIPANTES + ", " +
                    DBHelper.COLUMN_DETALHES +
                    " FROM " + DBHelper.TABLE_NAME, null);
            return cursorParaReunioes(c);
        } finally {
            close();
        }

    }

    public ReuniaoVO getReunioes(int idReuniao) throws ParseException {
        Cursor c = database.query(DBHelper.TABLE_NAME, allColumns, DBHelper.COLUMN_ID + " = " + idReuniao, null, null, null, null);
        c.moveToFirst();
        return cursorParaReuniao(c);
    }

}
