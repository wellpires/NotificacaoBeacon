package br.com.everis.notificacaobeacon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.CargoSpinnerAdapter;
import br.com.everis.notificacaobeacon.adapter.PermissaoSpinnerAdapter;
import br.com.everis.notificacaobeacon.bd.UsuarioDAO;
import br.com.everis.notificacaobeacon.listener.CargoPresenterListener;
import br.com.everis.notificacaobeacon.listener.PermissaoPresenterListener;
import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.model.UsuarioVO;
import br.com.everis.notificacaobeacon.service.ICargoService;
import br.com.everis.notificacaobeacon.service.IPermissaoService;
import br.com.everis.notificacaobeacon.service.impl.CargoServiceImpl;
import br.com.everis.notificacaobeacon.service.impl.PermissaoServiceImpl;
import br.com.everis.notificacaobeacon.utils.Constants;

public class ParticipanteFragment extends Fragment implements PermissaoPresenterListener, CargoPresenterListener {

    private Spinner cbPermissao = null;
    private Spinner cbCargo = null;
    private EditText txtNomeCompleto = null;
    private EditText txtEmail = null;
    private Button btnAdicionar = null;
    private Button btnCancelar = null;
    private ListView lvParticipantes = null;

    private  ProgressDialog barraProgresso = null;

    private ICargoService cargoService = null;
    private IPermissaoService permissaoService = null;
    private UsuarioDAO usuarioDAO = null;

    public ParticipanteFragment() {
    }

    public static ParticipanteFragment newInstance(Menu menu, ActionBar supportActionBar) {
        ParticipanteFragment fragment = new ParticipanteFragment();
        supportActionBar.setTitle("Participantes");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_participante, container, false);
        lvParticipantes = (ListView) v.findViewById(R.id.lvParticipantes);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        barraProgresso = new ProgressDialog(getActivity());
        barraProgresso.setMessage("Aguarde!");
        barraProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barraProgresso.setCancelable(false);
        barraProgresso.show();

        permissaoService = new PermissaoServiceImpl(getContext(), this);
        permissaoService.listarPermissoes();

        cargoService = new CargoServiceImpl(getContext(), this);
        cargoService.listarCargos();

        final Dialog dlgAdcParticipantes = new Dialog(getContext());
        dlgAdcParticipantes.setContentView(R.layout.custom_adicionar_participantes);
        txtNomeCompleto = (EditText) dlgAdcParticipantes.findViewById(R.id.txtNomeCompleto);
        txtEmail = (EditText) dlgAdcParticipantes.findViewById(R.id.txtEmail);
        cbPermissao = (Spinner) dlgAdcParticipantes.findViewById(R.id.cbPermissao);
        cbCargo = (Spinner) dlgAdcParticipantes.findViewById(R.id.cbCargo);
        btnAdicionar = (Button) dlgAdcParticipantes.findViewById(R.id.btnAddPartipante);
        btnCancelar = (Button) dlgAdcParticipantes.findViewById(R.id.btnCancelarParticipante);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioDAO = new UsuarioDAO(getContext());
                UsuarioVO vo = new UsuarioVO();
                vo.setNomeCompleto(txtNomeCompleto.getText().toString());
                vo.setEmail(txtEmail.getText().toString());
                vo.setCargoVO(new CargoVO());
                vo.getCargoVO().setIdCargo((Long) cbCargo.getSelectedView().getTag());
                vo.setPermissao(new PermissaoVO());
                vo.getPermissao().setIdPermissao((Long) cbPermissao.getSelectedView().getTag());


            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAdcParticipantes.dismiss();
            }
        });
        dlgAdcParticipantes.show();

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void permissaoReady(List<PermissaoVO> lstPermissao) {
        PermissaoSpinnerAdapter adapter = new PermissaoSpinnerAdapter(this.getContext(), lstPermissao);
        cbPermissao.setAdapter(adapter);
        if(cbCargo.getAdapter() != null){
            barraProgresso.cancel();
        }
    }

    @Override
    public void cargosReady(List<CargoVO> lstCargos) {
        CargoSpinnerAdapter adapter = new CargoSpinnerAdapter(this.getContext(), lstCargos);
        cbCargo.setAdapter(adapter);
        if(cbPermissao.getAdapter() != null){
            barraProgresso.cancel();
        }

    }
}
