package br.com.everis.notificacaobeacon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;

import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class AdicionarReuniaoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtDataInicio = null;
    private Button btnDataInicio = null;

    private EditText txtHoraInicio = null;
    private Button btnHoraInicio = null;

    private EditText txtDataTermino = null;
    private Button btnDataTermino = null;

    private EditText txtHoraTermino = null;
    private Button btnHoraTermino = null;

    private EditText txtAssunto = null;
    private EditText txtLocal = null;
    private EditText txtSala = null;
    private EditText txtDescricao = null;
    private Button btnSalvar = null;

    private int anoInicio = 0;
    private int mesInicio = 0;
    private int diaInicio = 0;
    private int horaInicio = 0;
    private int minutoInicio = 0;

    private int anoTermino = 0;
    private int mesTermino = 0;
    private int diaTermino = 0;
    private int horaTermino = 0;
    private int minutoTermino = 0;

    private DBAdapter datasource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_reuniao_activity);

        //TODO CRIAR UMA LISTA COM TODAS AS REUNIÕES

        btnDataInicio = (Button) findViewById(R.id.btnDataInicio);
        btnHoraInicio = (Button) findViewById(R.id.btnHoraInicio);
        btnDataTermino = (Button) findViewById(R.id.btnDataTermino);
        btnHoraTermino = (Button) findViewById(R.id.btnHoraTermino);

        txtDataInicio = (EditText) findViewById(R.id.txtDataInicio);
        txtHoraInicio = (EditText) findViewById(R.id.txtHoraInicio);
        txtDataTermino = (EditText) findViewById(R.id.txtDataTermino);
        txtHoraTermino = (EditText) findViewById(R.id.txtHoraTermino);

        txtAssunto = (EditText) findViewById(R.id.txtAssunto);
        txtLocal = (EditText) findViewById(R.id.txtOnde);
        txtSala = (EditText) findViewById(R.id.txtSala);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        //TODO COLOCAR ALGUMA SOLUÇÃO PARA O CAMPO ENDEREÇO

        btnDataInicio.setOnClickListener(this);
        btnHoraInicio.setOnClickListener(this);
        btnDataTermino.setOnClickListener(this);
        btnHoraTermino.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);

        datasource = new DBAdapter(getApplicationContext());

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnDataInicio) {

            final Calendar c = Calendar.getInstance();
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
        } else if (view.getId() == R.id.btnHoraInicio) {

            final Calendar c = Calendar.getInstance();
            horaInicio = c.get(Calendar.HOUR_OF_DAY);
            minutoInicio = c.get(Calendar.MINUTE);

            TimePickerDialog dpdhoraInicio = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtHoraInicio.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                }
            }, horaInicio, minutoInicio, false);
            dpdhoraInicio.show();

        } else if (view.getId() == R.id.btnDataTermino) {

            final Calendar c = Calendar.getInstance();
            anoTermino = c.get(Calendar.YEAR);
            mesTermino = c.get(Calendar.MONTH);
            diaTermino = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dataTermino = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    txtDataTermino.setText(ReuniaoUtils.zeroAEsquerda(dayOfMonth) + "/" + ReuniaoUtils.zeroAEsquerda((monthOfYear + 1)) + "/" + year);
                }
            }, anoTermino, mesTermino, diaTermino);
            dataTermino.show();
        } else if (view.getId() == R.id.btnHoraTermino) {

            final Calendar c = Calendar.getInstance();
            horaTermino = c.get(Calendar.HOUR_OF_DAY);
            minutoTermino = c.get(Calendar.MINUTE);

            TimePickerDialog dpdHoraTermino = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtHoraTermino.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                }
            }, horaTermino, minutoTermino, false);
            dpdHoraTermino.show();

        } else if (view.getId() == R.id.btnSalvar) {
            try {

                String horaInicio = txtDataInicio.getText().toString() + " " + txtHoraInicio.getText().toString();
                String horaTermino = txtDataTermino.getText().toString() + " " + txtHoraTermino.getText().toString();

                ReuniaoVO r = new ReuniaoVO();
                r.setAssunto(txtAssunto.getText().toString());
                r.setHoraInicio(horaInicio);
                r.setHoraTermino(horaTermino);
                r.setLocal(txtLocal.getText().toString());
                r.setSala(txtSala.getText().toString());
                r.setDetalhes(txtDescricao.getText().toString());

                datasource.open();
                ReuniaoVO reuniao = datasource.createReuniao(r);
                datasource.close();

                Log.d("TESTE_BANCO", reuniao.toString());

                txtAssunto.getText().clear();
                txtDataInicio.getText().clear();
                txtHoraInicio.getText().clear();
                txtDataTermino.getText().clear();
                txtHoraTermino.getText().clear();
                txtLocal.getText().clear();
                txtSala.getText().clear();
                txtDescricao.getText().clear();

                Intent i = new Intent(getApplicationContext(), NotificacaoMainActivity.class);
                startActivity(i);

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
