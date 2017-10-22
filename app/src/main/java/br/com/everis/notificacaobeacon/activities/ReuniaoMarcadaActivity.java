package br.com.everis.notificacaobeacon.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.everis.notificacaobeacon.R;

public class ReuniaoMarcadaActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVoltarSucesso = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_marcada);

        btnVoltarSucesso = (Button) findViewById(R.id.btnVoltarSucesso);
        btnVoltarSucesso.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnVoltarSucesso) {
            Intent i = new Intent(this, ReuniaoMainActivity.class);
            startActivity(i);
        }
    }
}
