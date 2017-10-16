package br.com.everis.notificacaobeacon.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.model.UsuarioVO;

/**
 * Created by wgoncalv on 16/10/2017.
 */

public class UsuarioAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<UsuarioVO> lstUsuarios;

    public UsuarioAdapter(Activity activity, List<UsuarioVO> lstUsuarios) {
        this.activity = activity;
        this.lstUsuarios = lstUsuarios;
    }

    @Override
    public int getCount() {
        return lstUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return lstUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstUsuarios.get(position).getIdUsuario();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView lblParticipante = new TextView(activity);
        lblParticipante.setText(lstUsuarios.get(position).getNomeCompleto());
        lblParticipante.setTextColor(Color.BLACK);
        lblParticipante.setTextSize(20f);

        return lblParticipante;
    }
}
