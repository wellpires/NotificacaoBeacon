package br.com.everis.notificacaobeacon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class AdicionarReuniaoActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePicker dpDataInicio = null;
    private EditText txtDataInicio = null;
    private Button btnDataInicio = null;

    private TimePicker tpHoraInicio = null;
    private EditText txtHoraInicio = null;
    private Button btnHoraInicio = null;

    private DatePicker dpDataTermino = null;
    private EditText txtDataTermino = null;
    private Button btnDataTermino = null;

    private TimePicker tpHoraTermino = null;
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

        //TODO MOSTRAR ALGUM AVISO PARA SE CASO UMA REUNIÃO ESTIVER ACONTECENDO
        //TODO MUDAR OS ÍCONES

        btnDataInicio = (Button) findViewById(R.id.btnDataInicio);
        btnHoraInicio = (Button) findViewById(R.id.btnHoraInicio);
        btnDataTermino = (Button) findViewById(R.id.btnDataTermino);
        btnHoraTermino = (Button) findViewById(R.id.btnHoraTermino);

        txtDataInicio = (EditText) findViewById(R.id.txtDataInicio);
        txtHoraInicio = (EditText) findViewById(R.id.txtHoraInicio);
        txtDataTermino = (EditText) findViewById(R.id.txtDataTermino);
        txtHoraTermino = (EditText) findViewById(R.id.txtHoraTermino);

        txtAssunto = (EditText) findViewById(R.id.lblAssunto);
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

        datasource = new DBAdapter(this);

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
                    dpDataInicio = view;
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
                    tpHoraInicio = view;
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
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dpDataTermino = view;
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
                    tpHoraTermino = view;
                    txtHoraTermino.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                }
            }, horaTermino, minutoTermino, false);
            dpdHoraTermino.show();

        } else if (view.getId() == R.id.btnSalvar) {
            try {

                if ("".equals(txtAssunto.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça o Assunto da reunião");
                    return;
                } else if (dpDataInicio == null) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça a Data de Inicio da reunião");
                    return;
                } else if (tpHoraInicio == null) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça a Hora de Inicio da reunião");
                    return;
                } else if (dpDataTermino == null) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça a Data de Término da reunião");
                    return;
                } else if (tpHoraTermino == null) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça a Hora de Término da reunião");
                    return;
                } else if ("".equals(txtLocal.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça o Local da reunião");
                    return;
                } else if ("".equals(txtDescricao.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "Forneça a Descriçao da reunião");
                    return;
                }

                String horaInicio = txtDataInicio.getText().toString() + " " + txtHoraInicio.getText().toString();
                String horaTermino = txtDataTermino.getText().toString() + " " + txtHoraTermino.getText().toString();

                DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDate(horaInicio));
                DateTime dtTermino = new DateTime(ReuniaoUtils.stringToDate(horaTermino));

                if (dtInicio.isEqual(dtTermino)) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "A data e o horário de inicio e término devem ser diferentes");
                    return;
                } else if (dtInicio.isAfter(dtTermino)) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, "A data de inicio deve ser menor que a data de término");
                    return;
                }

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
