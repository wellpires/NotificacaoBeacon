package br.com.everis.notificacaobeacon.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.ReunioesAdapter;
import br.com.everis.notificacaobeacon.bd.DBAdapter;

public class ReunioesActivity extends AppCompatActivity {

    private ListView lvReunioes = null;

    private DBAdapter dbAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reunioes);

        lvReunioes = (ListView) findViewById(R.id.lvTodasReunioes);

        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.getReunioes();

        ReunioesAdapter reuniaoAdapter = new ReunioesAdapter(dbAdapter.getReunioes(), ReunioesActivity.this);
        lvReunioes.setAdapter(reuniaoAdapter);

    }
}
