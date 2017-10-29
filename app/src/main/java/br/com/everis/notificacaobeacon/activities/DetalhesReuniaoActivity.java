package br.com.everis.notificacaobeacon.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.GlobalClass;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class DetalhesReuniaoActivity extends AppCompatActivity {

    private TextView lblInformacoes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_reuniao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblInformacoes = (TextView) findViewById(R.id.lblInformacoes);

        ReuniaoVO vo = ((GlobalClass) getApplicationContext()).getReuniaoVO();
        if (vo == null) {
            return;
        }

        Minutes minutos = null;
        DateTime dtTermino = null;
        DateTime dtHoje = new DateTime(new Date());
        DateTime dtInicio = new DateTime(vo.getDtInicio());
        dtTermino = new DateTime(vo.getDtTermino());

        minutos = Minutes.minutesBetween(dtHoje, dtInicio);

        HashMap<String, Object> hmValores = new HashMap<>();
        hmValores.put("assunto", vo.getAssunto());
        hmValores.put("minutos", minutos.getMinutes());
        hmValores.put("dataTermino", ReuniaoUtils.timeToString(dtTermino.toDate()));
        hmValores.put("sala", vo.getSala());

        StrSubstitutor substitutor = new StrSubstitutor(hmValores);
        lblInformacoes.setText(substitutor.replace(Constants.MENSAGEM_DETALHE_REUNIAO, hmValores));

    }

}
