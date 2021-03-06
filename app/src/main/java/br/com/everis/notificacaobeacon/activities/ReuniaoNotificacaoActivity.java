package br.com.everis.notificacaobeacon.activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.utils.Constants;

public class ReuniaoNotificacaoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblMensagem = null;
    private Button btnWaze = null;

    private Location  local = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_notificacao);
        lblMensagem = (TextView) findViewById(R.id.lblMensagem);
        btnWaze = (Button) findViewById(R.id.btnWaze);
        btnWaze.setOnClickListener(ReuniaoNotificacaoActivity.this);
        local = (Location) getIntent().getExtras().get(Constants.LOCAL_KEY);
        String mensagem = (String) getIntent().getExtras().get(Constants.MENSAGEM_KEY);

        lblMensagem.setText(mensagem);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnWaze) {

            HashMap<String, Double> hmValores = new HashMap<>();
            hmValores.put("latitude",local.getLatitude());
            hmValores.put("longitude", local.getLongitude());
            StrSubstitutor deepLink = new StrSubstitutor();

            String url = deepLink.replace(Constants.WAZE_DEEP_LINK, hmValores);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }
}
