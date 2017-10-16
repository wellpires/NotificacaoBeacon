package br.com.everis.notificacaobeacon.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.everis.notificacaobeacon.model.ArquivoVO;

/**
 * Created by wgoncalv on 16/10/2017.
 */

public class AnexosAdapter extends BaseAdapter {

    private final List<ArquivoVO> lstArquivos;
    private Activity activity = null;

    public AnexosAdapter(Activity activity) {
        this.lstArquivos = new ArrayList<>();
        this.activity = activity;
    }

    public void addItem(ArquivoVO arquivoVO){
        lstArquivos.add(arquivoVO);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lstArquivos.size();
    }

    @Override
    public Object getItem(int position) {
        return lstArquivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstArquivos.get(position).getIdArquivo();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView lblParticipante = new TextView(activity);
        lblParticipante.setText(lstArquivos.get(position).getArquivo());
        lblParticipante.setTextColor(Color.BLACK);
        lblParticipante.setTextSize(20f);

        return lblParticipante;
    }

}
