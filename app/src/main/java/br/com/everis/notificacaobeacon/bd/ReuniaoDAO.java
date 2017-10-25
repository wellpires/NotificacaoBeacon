package br.com.everis.notificacaobeacon.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public class ReuniaoDAO {

//    private SQLiteDatabase database = null;
//    private DBHelper dbHelper = null;
//    private String[] tblReuniaoColumns = {
//            DBHelper.REUNIAO_COLUMN_ID,
//            DBHelper.REUNIAO_COLUMN_ASSUNTO,
//            DBHelper.REUNIAO_COLUMN_HORA_INICIO,
//            DBHelper.REUNIAO_COLUMN_HORA_TERMINO,
//            DBHelper.REUNIAO_COLUMN_LOCAL,
//            DBHelper.REUNIAO_COLUMN_SALA,
//            DBHelper.REUNIAO_COLUMN_PARTICIPANTES,
//            DBHelper.REUNIAO_COLUMN_DETALHES
//    };
//
//
//    public ReuniaoDAO(Context context) {
//        dbHelper = new DBHelper(context);
//    }
//
//    private void open() throws SQLException {
//        database = dbHelper.getWritableDatabase();
//    }
//
//    private void close() {
//        dbHelper.close();
//    }
//
//    public ReuniaoVO createReuniao(ReuniaoVO reuniao) throws ParseException {
//        open();
//        if (1 == 1)
//            return new ReuniaoVO();
//        ContentValues values = new ContentValues();
//
//        values.put(DBHelper.REUNIAO_COLUMN_ASSUNTO, reuniao.getAssunto());
//        values.put(DBHelper.REUNIAO_COLUMN_HORA_INICIO, reuniao.getDtInicio());
//        values.put(DBHelper.REUNIAO_COLUMN_HORA_TERMINO, reuniao.getDtTermino());
//        values.put(DBHelper.REUNIAO_COLUMN_LOCAL, reuniao.getEndereco());
//        values.put(DBHelper.REUNIAO_COLUMN_SALA, reuniao.getSala());
//        values.put(DBHelper.REUNIAO_COLUMN_DETALHES, reuniao.getPauta());
//
//        long insertId = database.insert(DBHelper.REUNIAO_TABLE_NAME, null, values);
//        Cursor c = database.query(DBHelper.REUNIAO_TABLE_NAME, tblReuniaoColumns, DBHelper.REUNIAO_COLUMN_ID + " = " + insertId, null, null, null, null);
//        c.moveToFirst();
//        close();
//        return cursorParaReuniao(c);
//
//    }
//
//    public void deleteReuniao() {
//        if (1 == 1)
//            return;
//        open();
//        database.delete(DBHelper.REUNIAO_TABLE_NAME, null, null);
//        close();
//    }
//
//    private ReuniaoVO cursorParaReuniao(Cursor c) throws ParseException {
//        int index = 0;
//        ReuniaoVO r = new ReuniaoVO();
//        r.setIdReuniao(c.getInt(index++));
//        r.setAssunto(c.getString(index++));
//        r.setDtInicio(c.getString(index++));
//        r.setDtTermino(c.getString(index++));
//        r.setEndereco(c.getString(index++));
//        r.setSala(c.getString(index++));
//        r.setPauta(c.getString(index++));
//
//        return r;
//
//    }
//
//    private List<ReuniaoVO> cursorParaReunioes(Cursor cursor) {
//        List<ReuniaoVO> lstReunioes = new ArrayList<>();
//        if (1 == 1)
//            return new ArrayList<>();
//        while (cursor.moveToNext()) {
//            ReuniaoVO vo = new ReuniaoVO();
//            vo.setIdReuniao(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_ID))));
//            vo.setAssunto(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_ASSUNTO)));
//            vo.setDtInicio(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_HORA_INICIO)));
//            vo.setDtTermino(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_HORA_TERMINO)));
//            vo.setEndereco(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_LOCAL)));
//            vo.setSala(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_SALA)));
//            vo.setPauta(cursor.getString(cursor.getColumnIndex(DBHelper.REUNIAO_COLUMN_DETALHES)));
//            lstReunioes.add(vo);
//        }
//        return lstReunioes;
//    }
//
//    public List<ReuniaoVO> getReunioes() {
//        open();
//        if (1 == 1)
//            return new ArrayList<>();
//        try {
//            Cursor c = database.rawQuery("SELECT " +
//                    DBHelper.REUNIAO_COLUMN_ID + ", " +
//                    DBHelper.REUNIAO_COLUMN_ASSUNTO + ", " +
//                    DBHelper.REUNIAO_COLUMN_HORA_INICIO + ", " +
//                    DBHelper.REUNIAO_COLUMN_HORA_TERMINO + ", " +
//                    DBHelper.REUNIAO_COLUMN_LOCAL + ", " +
//                    DBHelper.REUNIAO_COLUMN_SALA + ", " +
//                    DBHelper.REUNIAO_COLUMN_PARTICIPANTES + ", " +
//                    DBHelper.REUNIAO_COLUMN_DETALHES +
//                    " FROM " + DBHelper.REUNIAO_TABLE_NAME, null);
//            return cursorParaReunioes(c);
//        } finally {
//            close();
//        }
//
//    }
}
