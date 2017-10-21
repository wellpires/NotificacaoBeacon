package br.com.everis.notificacaobeacon.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.GooglePlacesAutocompleteAdapter;
import br.com.everis.notificacaobeacon.bd.DAOHelper;
import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class ReuniaoFragment extends Fragment implements View.OnTouchListener, ReuniaoPresenterListener {

    private EditText txtAssunto = null;
    private EditText txtDtInicio = null;
    private EditText txtHrInicio = null;
    private EditText txtDtTermino = null;
    private EditText txtHrTermino = null;
    private EditText txtEndereco = null;
    private EditText txtSala = null;
    private EditText txtDescricao = null;

    private DAOHelper<ReuniaoVO> daoHelper = null;

    private ReuniaoVO voExistente = null;

    private Context context = null;
    private View view = null;

    public ReuniaoFragment() {
    }

    public static ReuniaoFragment newInstance(ActionBar supportActionBar) {
        ReuniaoFragment fragment = new ReuniaoFragment();
        supportActionBar.setTitle(Constants.TITULO_REUNIAO);
        return fragment;
    }

    private void buscarRegistroAdicionado() throws ParseException {
        ReuniaoVO reuniaoVO = daoHelper.find(ReuniaoVO.class);
        if (reuniaoVO == null) {
            voExistente = null;
            return;
        }
        voExistente = reuniaoVO;

        DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(voExistente.getDtInicio()));
        DateTime dtTermino = new DateTime(ReuniaoUtils.stringToDateTime(voExistente.getDtTermino()));

        txtAssunto.setText(voExistente.getAssunto());
        txtDtInicio.setText(ReuniaoUtils.dateToString(dtInicio.toDate()));
        txtHrInicio.setText(ReuniaoUtils.timeToString(dtInicio.toDate()));
        txtDtTermino.setText(ReuniaoUtils.dateToString(dtTermino.toDate()));
        txtHrTermino.setText(ReuniaoUtils.timeToString(dtTermino.toDate()));
        txtEndereco.setText(voExistente.getEndereco());
        txtSala.setText(voExistente.getSala());
        txtDescricao.setText(voExistente.getPauta());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_reuniao, container, false);
            context = view.getContext();
            txtAssunto = (EditText) view.findViewById(R.id.txtAssunto);
            txtDtInicio = (EditText) view.findViewById(R.id.txtDataInicio);
            txtHrInicio = (EditText) view.findViewById(R.id.txtHoraInicio);
            txtDtTermino = (EditText) view.findViewById(R.id.txtDataTermino);
            txtHrTermino = (EditText) view.findViewById(R.id.txtHoraTermino);
            txtEndereco = (EditText) view.findViewById(R.id.txtOnde);
            txtSala = (EditText) view.findViewById(R.id.txtSala);
            txtDescricao = (EditText) view.findViewById(R.id.txtDescricao);

            txtDtInicio.setOnTouchListener(this);
            txtDtInicio.setKeyListener(null);

            txtHrInicio.setOnTouchListener(this);
            txtHrInicio.setKeyListener(null);

            txtDtTermino.setOnTouchListener(this);
            txtDtTermino.setKeyListener(null);

            txtHrTermino.setOnTouchListener(this);
            txtHrTermino.setKeyListener(null);

            txtEndereco.setOnTouchListener(this);
            txtEndereco.setKeyListener(null);

            daoHelper = new DAOHelper<>();
            buscarRegistroAdicionado();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.btnSalvar);
        item.setTitle(Constants.LABEL_PROXIMO);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btnSalvar){
            try {
                if (ReuniaoUtils.isEmptyOrNull(txtAssunto.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_ASSUNTO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtDtInicio.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_DATA_INICIO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtHrInicio.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_HORA_INICIO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtDtTermino.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_DATA_TERMINO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtHrTermino.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_HORA_TERMINO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtEndereco.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_LOCAL_REUNIAO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ReuniaoUtils.isEmptyOrNull(txtDescricao.getText().toString())) {
                    Toast.makeText(context, Constants.ERRO_DESCRICAO_REUNIAO, Toast.LENGTH_SHORT).show();
                    return false;
                }

                String horaInicio = txtDtInicio.getText().toString() + " " + txtHrInicio.getText().toString();
                String horaTermino = txtDtTermino.getText().toString() + " " + txtHrTermino.getText().toString();

                DateTime dtInicio = null;
                dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(horaInicio));
                DateTime dtTermino = new DateTime(ReuniaoUtils.stringToDateTime(horaTermino));


                if (dtInicio.isEqual(dtTermino)) {
                    Toast.makeText(context, Constants.ERRO_DATA_HORA_INICIO_TERMINO_DIFERENTES, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (dtInicio.isAfter(dtTermino)) {
                    Toast.makeText(context, Constants.ERRO_DATA_INICIO_MENOR_TERMINO, Toast.LENGTH_SHORT).show();
                    return false;
                }

                ReuniaoVO vo = new ReuniaoVO();
                if (voExistente != null) {
                    vo = voExistente;
                    daoHelper.open();
                }
                vo.setIdReuniao(Integer.parseInt(daoHelper.getNextId(ReuniaoVO.class).toString()));
                vo.setAssunto(txtAssunto.getText().toString());
                vo.setDtInicio(txtDtInicio.getText().toString() + " " + txtHrInicio.getText().toString());
                vo.setDtTermino(txtDtTermino.getText().toString() + " " + txtHrTermino.getText().toString());
                vo.setEndereco(txtEndereco.getText().toString());
                vo.setSala(txtSala.getText().toString());
                vo.setPauta(txtDescricao.getText().toString());
                daoHelper.insert(vo);

                daoHelper.close();

                buscarRegistroAdicionado();

                AdicionarEditarReuniaoActivity adicionarEditarReuniaoActivity = (AdicionarEditarReuniaoActivity) getActivity();
                BottomNavigationView bnvNavegacao = (BottomNavigationView) adicionarEditarReuniaoActivity.findViewById(R.id.navigation);
                bnvNavegacao.setSelectedItemId(R.id.navigation_participantes);
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
                    if (!ReuniaoUtils.isEmptyOrNull(txtDtInicio.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToDate(txtDtInicio.getText().toString()));
                    }

                    anoInicio = c.get(Calendar.YEAR);
                    mesInicio = c.get(Calendar.MONTH);
                    diaInicio = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dataInicio = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDtInicio.setText(ReuniaoUtils.zeroAEsquerda(dayOfMonth) + "/" + ReuniaoUtils.zeroAEsquerda((monthOfYear + 1)) + "/" + year);
                        }
                    }, anoInicio, mesInicio, diaInicio);
                    dataInicio.show();

                } else if (view.getId() == R.id.txtHoraInicio) {
                    int horaInicio = 0;
                    int minutoInicio = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtHrInicio.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToTime(txtHrInicio.getText().toString()));
                    }

                    horaInicio = c.get(Calendar.HOUR_OF_DAY);
                    minutoInicio = c.get(Calendar.MINUTE);

                    TimePickerDialog dpdhoraInicio = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtHrInicio.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                        }
                    }, horaInicio, minutoInicio, false);
                    dpdhoraInicio.show();

                } else if (view.getId() == R.id.txtDataTermino) {
                    int anoTermino = 0;
                    int mesTermino = 0;
                    int diaTermino = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtDtTermino.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToDate(txtDtTermino.getText().toString()));
                    }

                    anoTermino = c.get(Calendar.YEAR);
                    mesTermino = c.get(Calendar.MONTH);
                    diaTermino = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dataTermino = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDtTermino.setText(ReuniaoUtils.zeroAEsquerda(dayOfMonth) + "/" + ReuniaoUtils.zeroAEsquerda((monthOfYear + 1)) + "/" + year);
                        }
                    }, anoTermino, mesTermino, diaTermino);
                    dataTermino.show();
                } else if (view.getId() == R.id.txtHoraTermino) {

                    int horaTermino = 0;
                    int minutoTermino = 0;

                    Calendar c = Calendar.getInstance();
                    if (!ReuniaoUtils.isEmptyOrNull(txtHrTermino.getText().toString())) {
                        c.setTime(ReuniaoUtils.stringToTime(txtHrTermino.getText().toString()));
                    }

                    horaTermino = c.get(Calendar.HOUR_OF_DAY);
                    minutoTermino = c.get(Calendar.MINUTE);

                    TimePickerDialog dpdHoraTermino = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtHrTermino.setText(ReuniaoUtils.zeroAEsquerda(hourOfDay) + ":" + ReuniaoUtils.zeroAEsquerda(minute));
                        }
                    }, horaTermino, minutoTermino, false);
                    dpdHoraTermino.show();

                } else if (view.getId() == R.id.txtOnde) {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    dialog.setContentView(R.layout.custom_google_maps);

                    final MapView mMapView = (MapView) dialog.findViewById(R.id.mvMapa);
                    MapsInitializer.initialize(context);

                    Button btnOK = (Button) dialog.findViewById(R.id.btnOKGoogle);
                    Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
                    Button btnPesquisar = (Button) dialog.findViewById(R.id.btnPesquisar);

                    final AutoCompleteTextView txtPesquisar = (AutoCompleteTextView) dialog.findViewById(R.id.txtPesquisar);
                    txtPesquisar.setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.list_item_google_autocomplete));

                    mMapView.onCreate(dialog.onSaveInstanceState());
                    mMapView.onResume();// needed to get the map to display immediately

                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap map) {

                            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location == null) {
                                location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            }

                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();

                            LatLng posisiabsen = new LatLng(latitude, longitude);
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
                                Toast.makeText(context, Constants.ERRO_PESQUISA_ENDERECO, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            txtEndereco.setText(txtPesquisar.getText().toString());
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
                                    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }

                                    String googleString = txtPesquisar.getText().toString();

                                    Geocoder geocoder = new Geocoder(context);
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

                                    ReuniaoUtils.esconderTeclado(getActivity());
                                }
                            });
                        }
                    });

                    dialog.show();

                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ReuniaoUtils.esconderTeclado(getActivity());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void reunioesReady(List<ReuniaoVO> lstReunioes) {

    }

    @Override
    public void reuniaoReady(ReuniaoVO reuniaoVO) {

    }

    @Override
    public void reuniaoReady() {

    }

    @Override
    public void reuniaoFailed(RestException exception) {
    }
}
