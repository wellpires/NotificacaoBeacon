package br.com.everis.notificacaobeacon.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.util.Date;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
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
        if(vo == null){
            return;
        }

        Minutes minutos = null;
        DateTime dtTermino = null;
        try {
            DateTime dtHoje = new DateTime(new Date());
            DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(vo.getDtInicio()));
            dtTermino = new DateTime(ReuniaoUtils.stringToDateTime(vo.getDtTermino()));

            minutos = Minutes.minutesBetween(dtHoje, dtInicio);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        lblInformacoes.setText("Sua reunião é sobre " + vo.getAssunto() + ", começa em " +
                minutos.getMinutes() + " minutos e tem previsão de término a partir das " +
                ReuniaoUtils.timeToString(dtTermino.toDate()) +
                ". Por favor, dirija-se à sala " + vo.getSala() + ".");

    }

}
