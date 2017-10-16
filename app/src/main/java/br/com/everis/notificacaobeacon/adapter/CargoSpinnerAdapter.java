package br.com.everis.notificacaobeacon.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.model.PermissaoVO;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class CargoSpinnerAdapter implements SpinnerAdapter {

    private List<CargoVO> lstCargos = null;
    private Context context = null;

    public CargoSpinnerAdapter(Context context, List<CargoVO> lstCargos) {
        this.context = context;
        this.lstCargos = lstCargos;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return lstCargos.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCargos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstCargos.get(position).getIdCargo();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getLabelView(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getLabelView(position);
    }

    @NonNull
    private View getLabelView(int position) {
        TextView lblPermissao = new TextView(context);
        lblPermissao.setTextColor(Color.BLACK);
        lblPermissao.setTextSize(20f);
        lblPermissao.setText(lstCargos.get(position).getCargo());
        lblPermissao.setTag(lstCargos.get(position).getIdCargo());

        return lblPermissao;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return lstCargos.isEmpty();
    }
}
