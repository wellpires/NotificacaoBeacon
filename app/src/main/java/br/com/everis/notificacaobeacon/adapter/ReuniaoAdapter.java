package br.com.everis.notificacaobeacon.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

/**
 * Created by wgoncalv on 20/09/2017.
 */

public class ReuniaoAdapter extends BaseAdapter {

    private final List<ReuniaoVO> lstReunioes;
    private final Activity act;

    public ReuniaoAdapter(List<ReuniaoVO> lstReunioes, Activity act) {
        this.lstReunioes = lstReunioes;
        this.act = act;
    }

    @Override
    public int getCount() {
        return lstReunioes.size();
    }

    @Override
    public Object getItem(int i) {
        return lstReunioes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstReunioes.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = null;
        try {
            view = act.getLayoutInflater().inflate(R.layout.reuniao_list_item, parent, false);
            ReuniaoVO vo = lstReunioes.get(i);

            TextView lblAssunto = (TextView) view.findViewById(R.id.lblAssunto);
            TextView lblHorario = (TextView) view.findViewById(R.id.lblHorario);

            Date horaInicio = ReuniaoUtils.stringToDate(vo.getHoraInicio());
            Date horaTermino = ReuniaoUtils.stringToDate(vo.getHoraTermino());

            lblAssunto.setText(vo.getAssunto());
            lblHorario.setText(ReuniaoUtils.timeToString(horaInicio) + " - " + ReuniaoUtils.timeToString(horaTermino));

        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
