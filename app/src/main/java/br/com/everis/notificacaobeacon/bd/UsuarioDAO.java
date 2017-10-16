package br.com.everis.notificacaobeacon.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.model.UsuarioVO;

/**
 * Created by wgoncalv on 16/10/2017.
 */

public class UsuarioDAO {

    private SQLiteDatabase database = null;
    private DBHelper dbHelper = null;

    private String[] tblUsuarioColumns = {
            DBHelper.USUARIO_COLUMN_ID,
            DBHelper.USUARIO_COLUMN_NOME,
            DBHelper.USUARIO_COLUMN_EMAIL,
            DBHelper.USUARIO_COLUMN_PERMISSAO_FK,
            DBHelper.USUARIO_COLUMN_CARGO_FK
    };

    public UsuarioDAO(Context c) {
        dbHelper = new DBHelper(c);
    }

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        database.close();
    }

    public void createUsuario(UsuarioVO usuarioVO) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHelper.USUARIO_COLUMN_NOME, usuarioVO.getNomeCompleto());
        values.put(DBHelper.USUARIO_COLUMN_EMAIL, usuarioVO.getEmail());
        values.put(DBHelper.USUARIO_COLUMN_PERMISSAO_FK, usuarioVO.getPermissao().getIdPermissao());
        values.put(DBHelper.USUARIO_COLUMN_CARGO_FK, usuarioVO.getCargoVO().getIdCargo());

        database.insert(DBHelper.USUARIO_TABLE_NAME, null, values);
        close();
    }

    public List<UsuarioVO> listarUsuarios() {
        try{
            open();
            Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.USUARIO_TABLE_NAME, null);
            return cursorParaUsuarios(cursor);
        }finally {
            close();
        }
    }

    private List<UsuarioVO> cursorParaUsuarios(Cursor cursor) {
        List<UsuarioVO> lstUsuarios = new ArrayList<>();
        while (cursor.moveToNext()) {
            UsuarioVO vo = new UsuarioVO();
            vo.setIdUsuario(0L);
            vo.setNomeCompleto(cursor.getString(cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_NOME)));
            vo.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_EMAIL)));
            vo.setCargoVO(new CargoVO());
            vo.getCargoVO().setIdCargo(cursor.getLong(cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_CARGO_FK)));
            vo.setPermissao(new PermissaoVO());
            vo.getPermissao().setIdPermissao(cursor.getLong((cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_PERMISSAO_FK))));
            lstUsuarios.add(vo);
        }
        return lstUsuarios;
    }


}
