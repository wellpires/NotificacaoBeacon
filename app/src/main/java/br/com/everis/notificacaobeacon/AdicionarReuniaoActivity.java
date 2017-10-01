package br.com.everis.notificacaobeacon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import br.com.everis.notificacaobeacon.adapter.GooglePlacesAutocompleteAdapter;
import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class AdicionarReuniaoActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView lblTituloReuniao = null;

    private EditText txtDataInicio = null;
    private EditText txtHoraInicio = null;
    private EditText txtDataTermino = null;
    private EditText txtHoraTermino = null;

    private EditText txtAssunto = null;
    private EditText txtLocal = null;
    private EditText txtSala = null;
    private EditText txtDescricao = null;
    private Button btnSalvar = null;

    private DBAdapter datasource = null;

    private Integer idReuniao = null;
    private Boolean isNovoRegistro = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_reuniao_activity);

        //TODO MOSTRAR ALGUM AVISO PARA SE CASO UMA REUNIÃO ESTIVER ACONTECENDO

        lblTituloReuniao = (TextView) findViewById(R.id.lblTituloReuniao);

        txtDataInicio = (EditText) findViewById(R.id.txtDataInicio);
        txtHoraInicio = (EditText) findViewById(R.id.txtHoraInicio);
        txtDataTermino = (EditText) findViewById(R.id.txtDataTermino);
        txtHoraTermino = (EditText) findViewById(R.id.txtHoraTermino);

        txtAssunto = (EditText) findViewById(R.id.lblAssunto);
        txtLocal = (EditText) findViewById(R.id.txtOnde);
        txtSala = (EditText) findViewById(R.id.txtSala);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);

        txtDataInicio.setOnTouchListener(this);
        txtDataInicio.setKeyListener(null);

        txtHoraInicio.setOnTouchListener(this);
        txtHoraInicio.setKeyListener(null);

        txtDataTermino.setOnTouchListener(this);
        txtDataTermino.setKeyListener(null);

        txtHoraTermino.setOnTouchListener(this);
        txtHoraTermino.setKeyListener(null);

        txtLocal.setOnTouchListener(this);
        txtLocal.setKeyListener(null);



        datasource = new DBAdapter(this);

        isNovoRegistro = getIntent().getBooleanExtra(Constants.NOVA_REUNIAO_KEY, true);

        if (isNovoRegistro) {
            lblTituloReuniao.setText(Constants.TITULO_NOVA_REUNIAO);
        } else {
            lblTituloReuniao.setText(Constants.TITULO_EDITAR_REUNIAO);
            try {
                idReuniao = Integer.valueOf(getIntent().getStringExtra(Constants.ID_REUNIAO_KEY));

                datasource.open();
                ReuniaoVO vo = datasource.getReuniao(idReuniao);
                String dataInicio[] = vo.getHoraInicio().split("\\s");
                String dataTermino[] = vo.getHoraTermino().split("\\s");

                txtAssunto.setText(vo.getAssunto());
                txtDataInicio.setText(dataInicio[0]);
                txtHoraInicio.setText(dataInicio[1]);
                txtDataTermino.setText(dataTermino[0]);
                txtHoraTermino.setText(dataTermino[1]);
                txtLocal.setText(vo.getLocal());
                txtSala.setText(vo.getSala());
                txtDescricao.setText(vo.getDetalhes());
                datasource.close();

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_button_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() ==  R.id.btnSalvar){
            try {

                if (ReuniaoUtils.isEmptyOrNull(txtAssunto.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_ASSUNTO_REUNIAO);
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtDataInicio.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_DATA_INICIO_REUNIAO);
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtHoraInicio.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_HORA_INICIO_REUNIAO);
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtDataTermino.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_DATA_TERMINO_REUNIAO);
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtHoraTermino.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_HORA_TERMINO_REUNIAO);
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtLocal.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_LOCAL_REUNIAO_REUNIAO);
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtDescricao.getText().toString())) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_DESCRICAO_REUNIAO);
                    return false;
                }

                String horaInicio = txtDataInicio.getText().toString() + " " + txtHoraInicio.getText().toString();
                String horaTermino = txtDataTermino.getText().toString() + " " + txtHoraTermino.getText().toString();

                DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(horaInicio));
                DateTime dtTermino = new DateTime(ReuniaoUtils.stringToDateTime(horaTermino));

                if (dtInicio.isEqual(dtTermino)) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_DATA_HORA_INICIO_TERMINO_DIFERENTES);
                    return false;
                } else if (dtInicio.isAfter(dtTermino)) {
                    ReuniaoUtils.mostrarAvisoDialogo(this, Constants.ERRO_DATA_INICIO_MENOR_TERMINO);
                    return false;
                }

                final ReuniaoVO r = new ReuniaoVO();
                r.setAssunto(txtAssunto.getText().toString());
                r.setHoraInicio(horaInicio);
                r.setHoraTermino(horaTermino);
                r.setLocal(txtLocal.getText().toString());
                r.setSala(txtSala.getText().toString());
                r.setDetalhes(txtDescricao.getText().toString());

                datasource.open();

                if (isNovoRegistro) {
                    datasource.createReuniao(r);
                    finalizarAcao();
                } else {
                    ReuniaoUtils.mostrarPerguntaDialogo(AdicionarReuniaoActivity.this, Constants.LABEL_VOCE_TEM_CERTEZA, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            r.setId(idReuniao);
                            datasource.updateReuniao(r);
                            finalizarAcao();
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (view.getId() == R.id.txtDataInicio) {
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

                } else if (view.getId() == R.id.txtHoraInicio) {
                    int horaInicio = 0;
                    int minutoInicio = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtHoraInicio.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToTime(txtHoraInicio.getText().toString()));
                    }

                    horaInicio = c.get(Calendar.HOUR_OF_DAY);
                    minutoInicio = c.get(Calendar.MINUTE);

                    TimePickerDialog dpdhoraInicio = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtHoraInicio.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                        }
                    }, horaInicio, minutoInicio, false);
                    dpdhoraInicio.show();

                } else if (view.getId() == R.id.txtDataTermino) {
                    int anoTermino = 0;
                    int mesTermino = 0;
                    int diaTermino = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtDataTermino.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToDate(txtDataTermino.getText().toString()));
                    }

                    anoTermino = c.get(Calendar.YEAR);
                    mesTermino = c.get(Calendar.MONTH);
                    diaTermino = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dataTermino = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDataTermino.setText(ReuniaoUtils.zeroAEsquerda(dayOfMonth) + "/" + ReuniaoUtils.zeroAEsquerda((monthOfYear + 1)) + "/" + year);
                        }
                    }, anoTermino, mesTermino, diaTermino);
                    dataTermino.show();
                } else if (view.getId() == R.id.txtHoraTermino) {

                    int horaTermino = 0;
                    int minutoTermino = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtHoraTermino.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToTime(txtHoraTermino.getText().toString()));
                    }

                    horaTermino = c.get(Calendar.HOUR_OF_DAY);
                    minutoTermino = c.get(Calendar.MINUTE);

                    TimePickerDialog dpdHoraTermino = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtHoraTermino.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                        }
                    }, horaTermino, minutoTermino, false);
                    dpdHoraTermino.show();

                } else if (view.getId() == R.id.txtOnde) {

                    final Dialog dialog = new Dialog(AdicionarReuniaoActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    dialog.setContentView(R.layout.custom_google_maps);

                    final MapView mMapView = (MapView) dialog.findViewById(R.id.mvMapa);
                    MapsInitializer.initialize(AdicionarReuniaoActivity.this);

                    Button btnOK = (Button) dialog.findViewById(R.id.btnOKGoogle);
                    Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
                    Button btnPesquisar = (Button) dialog.findViewById(R.id.btnPesquisar);

                    final AutoCompleteTextView txtPesquisar = (AutoCompleteTextView) dialog.findViewById(R.id.txtPesquisar);
                    txtPesquisar.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_google_autocomplete));

                    mMapView.onCreate(dialog.onSaveInstanceState());
                    mMapView.onResume();// needed to get the map to display immediately

                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap map) {

                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();

                            LatLng posisiabsen = new LatLng(latitude, longitude); ////your lat lng
                            map.addMarker(new MarkerOptions().position(posisiabsen).title(Constants.VOCE_ESTA_AQUI));
                            map.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                            map.getUiSettings().setZoomControlsEnabled(true);
                            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        }
                    });

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (txtPesquisar.getText().toString().trim().isEmpty()) {
                                ReuniaoUtils.mostrarAvisoDialogo(AdicionarReuniaoActivity.this, Constants.ERRO_PESQUISA_ENDERECO);
                                return;
                            }
                            txtLocal.setText(txtPesquisar.getText().toString());
                            dialog.dismiss();
                        }
                    });

                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnPesquisar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(final GoogleMap map) {

                                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }

                                    String googleString = txtPesquisar.getText().toString();

                                    Geocoder geocoder = new Geocoder(AdicionarReuniaoActivity.this);
                                    List<Address> lstEnderecos = null;

                                    try {
                                        lstEnderecos = geocoder.getFromLocationName(googleString, 1);

                                        if (lstEnderecos.size() > 0) {

                                            Double lat = (double) (lstEnderecos.get(0).getLatitude());
                                            Double lon = (double) (lstEnderecos.get(0).getLongitude());

                                            final LatLng user = new LatLng(lat, lon);

                                            LatLng posisiabsen = new LatLng(lat, lon); ////your lat lng
                                            map.addMarker(new MarkerOptions().position(posisiabsen).title("Yout title"));
                                            map.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                                            map.getUiSettings().setZoomControlsEnabled(true);
                                            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ReuniaoUtils.esconderTeclado(AdicionarReuniaoActivity.this);

                                }
                            });

                        }
                    });

                    dialog.show();

                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ReuniaoUtils.esconderTeclado(AdicionarReuniaoActivity.this);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void finalizarAcao() {
        datasource.close();

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
    }

}
