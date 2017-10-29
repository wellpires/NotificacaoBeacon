package br.com.everis.notificacaobeacon.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

/**
 * Created by wgoncalv on 02/10/2017.
 */

public class ReunioesAdapter extends BaseAdapter {

    private final List<ReuniaoVO> lstReuniaoVO;
    private final Activity activity;

    public ReunioesAdapter(List<ReuniaoVO> lstReuniaoVO, Activity activity) {
        this.lstReuniaoVO = lstReuniaoVO;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return lstReuniaoVO.size();
    }

    @Override
    public Object getItem(int position) {
        return lstReuniaoVO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstReuniaoVO.get(position).getIdReuniao();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        try {
            view = activity.getLayoutInflater().inflate(R.layout.reuniao_item_list, parent, false);
            ReuniaoVO vo = lstReuniaoVO.get(position);

            TextView lblAssunto = (TextView) view.findViewById(R.id.txtAssunto);
            TextView lblHorario = (TextView) view.findViewById(R.id.lblHorario);

            lblAssunto.setText(vo.getAssunto());
            lblHorario.setText(ReuniaoUtils.formatDate(Constants.DATETIME_CUSTOM_PATTERN, vo.getDtInicio()));

        }catch (Exception e){

        }
        return view;
    }
}
