package br.com.everis.notificacaobeacon.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.ReunioesAdapter;
import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class ReunioesActivity extends AppCompatActivity implements View.OnTouchListener, TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView txtEmptyList = null;
    private EditText txtDataInicio = null;
    private ListView lvReunioes = null;
    private Button btnLimparFiltro = null;

    private DBAdapter dbAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reunioes);

        txtEmptyList = (TextView) findViewById(R.id.txtEmptyList);
        txtDataInicio = (EditText) findViewById(R.id.txtDtInicio);
        lvReunioes = (ListView) findViewById(R.id.lvTodasReunioes);
        btnLimparFiltro = (Button) findViewById(R.id.btnLimparFiltro);

        txtDataInicio.setOnTouchListener(this);
        txtDataInicio.setKeyListener(null);
        txtDataInicio.addTextChangedListener(this);

        lvReunioes.setOnItemClickListener(this);

        btnLimparFiltro.setOnClickListener(this);

        txtEmptyList.setText(Constants.LABEL_NENHUMA_REUNIAO);
        lvReunioes.setEmptyView(txtEmptyList);

        dbAdapter = new DBAdapter(getApplicationContext());
        criarAdapter(dbAdapter.getReunioes());

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v.getId() == R.id.txtDtInicio) {
                    int anoInicio = 0;
                    int mesInicio = 0;
                    int diaInicio = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtDataInicio.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToDate(txtDataInicio.getText().toString()));
                    }

                    anoInicio = c.get(Calendar.YEAR);
                    mesInicio = c.get(Calendar.MONTH);
                    diaInicio = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dataInicio = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDataInicio.setText(ReuniaoUtils.zeroAEsquerda(dayOfMonth) + "/" + ReuniaoUtils.zeroAEsquerda((monthOfYear + 1)) + "/" + year);
                        }
                    }, anoInicio, mesInicio, diaInicio);
                    dataInicio.show();
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            filtrarLista(s.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void filtrarLista(String filtro) throws ParseException {
        DateTime inicio = new DateTime();
        inicio = new DateTime(ReuniaoUtils.stringToDate(filtro));
        List<ReuniaoVO> lstReunioes = dbAdapter.getReunioes();
        List<ReuniaoVO> lstReunioesFiltradas = new ArrayList<>();

        for (ReuniaoVO item : lstReunioes) {
            DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(item.getDtInicio()));
            if (dtInicio.withTimeAtStartOfDay().equals(inicio)) {
                lstReunioesFiltradas.add(item);
            }
        }
        criarAdapter(lstReunioesFiltradas);
    }

    private void criarAdapter(List<ReuniaoVO> reunioes){
        ReunioesAdapter reuniaoAdapter = new ReunioesAdapter(reunioes, ReunioesActivity.this);
        lvReunioes.setAdapter(reuniaoAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLimparFiltro){
            txtDataInicio.getText().clear();
            criarAdapter(dbAdapter.getReunioes());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), AdicionarReuniaoActivity.class);
        i.putExtra(Constants.NOVA_REUNIAO_KEY, Constants.FLAG_DETALHES_REUNIAO);
        i.putExtra(Constants.ID_REUNIAO_KEY, String.valueOf(id));
        startActivity(i);
    }
}
