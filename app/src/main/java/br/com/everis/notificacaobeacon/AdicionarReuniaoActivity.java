package br.com.everis.notificacaobeacon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.adapter.GooglePlacesAutocompleteAdapter;
import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class AdicionarReuniaoActivity extends AppCompatActivity implements View.OnClickListener{

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
    private Button btnMapa = null;

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

        btnMapa = (Button) findViewById(R.id.btnMapa);

        //TODO COLOCAR BOTÃO DE SALVAR NA ACTIONBAR
        //TODO COLOCAR ALGUMA SOLUÇÃO PARA O CAMPO ENDEREÇO

        btnDataInicio.setOnClickListener(this);
        btnHoraInicio.setOnClickListener(this);
        btnDataTermino.setOnClickListener(this);
        btnHoraTermino.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);
        btnMapa.setOnClickListener(this);

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

        } else if (view.getId() == R.id.btnMapa) {

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
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    LatLng posisiabsen = new LatLng(latitude, longitude); ////your lat lng
                    map.addMarker(new MarkerOptions().position(posisiabsen).title("Yout title"));
                    map.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                    map.getUiSettings().setZoomControlsEnabled(true);
                    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }
            });

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(txtPesquisar.getText().toString().trim().isEmpty()){
                        ReuniaoUtils.mostrarAvisoDialogo(AdicionarReuniaoActivity.this, "Favor pesquise um endereço!");
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
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
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

                        }
                    });

                }
            });

            dialog.show();

        }
    }

}
