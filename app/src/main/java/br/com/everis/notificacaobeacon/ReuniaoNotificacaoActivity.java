package br.com.everis.notificacaobeacon;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;

public class ReuniaoNotificacaoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblMensagem = null;
    private Button btnWaze = null;

    private String local = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_notificacao);
        //TODO COLOCAR O BACKBUTTON NESSA TELA
        lblMensagem = (TextView) findViewById(R.id.lblMensagem);
        btnWaze = (Button) findViewById(R.id.btnWaze);
        btnWaze.setOnClickListener(ReuniaoNotificacaoActivity.this);
        Integer tempoRestante = Integer.parseInt(String.valueOf(getIntent().getExtras().get(Constants.TEMPO_RESTANTE)));
        local = (String) getIntent().getExtras().get(Constants.LOCAL);

        String hora = Constants.HORA_SINGULAR;
        if(tempoRestante > 1){
            hora = Constants.HORA_PLURAL;
        }

        lblMensagem.setText(Constants.MENSAGEM_REUNIAO + tempoRestante + " " + hora);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnWaze) {
            String url = "https://www.waze.com/ul?q=" + local.trim().replaceAll("\\s", "%20");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }
}
