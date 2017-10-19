package br.com.everis.notificacaobeacon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.CargoSpinnerAdapter;
import br.com.everis.notificacaobeacon.adapter.PermissaoSpinnerAdapter;
import br.com.everis.notificacaobeacon.adapter.UsuarioAdapter;
import br.com.everis.notificacaobeacon.bd.DAOHelper;
import br.com.everis.notificacaobeacon.listener.CargoPresenterListener;
import br.com.everis.notificacaobeacon.listener.PermissaoPresenterListener;
import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.model.UsuarioVO;
import br.com.everis.notificacaobeacon.service.ICargoService;
import br.com.everis.notificacaobeacon.service.IPermissaoService;
import br.com.everis.notificacaobeacon.service.impl.CargoServiceImpl;
import br.com.everis.notificacaobeacon.service.impl.PermissaoServiceImpl;
import br.com.everis.notificacaobeacon.utils.Constants;

public class ParticipanteFragment extends Fragment implements PermissaoPresenterListener, CargoPresenterListener, View.OnClickListener {

    private Spinner cbPermissao = null;
    private Spinner cbCargo = null;
    private EditText txtNomeCompleto = null;
    private EditText txtEmail = null;
    private Button btnAdicionar = null;
    private Button btnCancelar = null;
    private ListView lvParticipantes = null;
    private FloatingActionButton fabAddParticipante = null;

    private View v;
    private Context context;
    private ProgressDialog barraProgresso = null;

    private ICargoService cargoService = null;
    private IPermissaoService permissaoService = null;

    private UsuarioAdapter usuarioAdapter = null;
    private DAOHelper<UsuarioVO> usuarioDAO = null;

    public ParticipanteFragment() {
    }

    public static ParticipanteFragment newInstance(ActionBar supportActionBar) {
        ParticipanteFragment fragment = new ParticipanteFragment();
        supportActionBar.setTitle(Constants.TITULO_PARTICIPANTE);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_participante, container, false);
        context = v.getContext();

        usuarioAdapter = new UsuarioAdapter(getActivity());
        usuarioDAO = new DAOHelper<>();
        lvParticipantes = (ListView) v.findViewById(R.id.lvParticipantes);
        fabAddParticipante = (FloatingActionButton) v.findViewById(R.id.fabAddParticipante);
        fabAddParticipante.setOnClickListener(this);
        lvParticipantes.setAdapter(usuarioAdapter);

        List<UsuarioVO> usuarios = usuarioDAO.findAll(UsuarioVO.class);
        usuarioAdapter.addAll(usuarios);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.btnSalvar);
        item.setTitle(Constants.LABEL_PROXIMO);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.btnSalvar) {
            if(usuarioAdapter.getCount() == 0){
                Toast.makeText(context, Constants.ERRO_FALTA_PARTICIPANTE, Toast.LENGTH_LONG).show();
                return false;
            }
            usuarioDAO.deleteAll(UsuarioVO.class);

            for (int i = 0; i < usuarioAdapter.getCount(); i++) {
                UsuarioVO vo = (UsuarioVO) usuarioAdapter.getItem(i);
                vo.setIdUsuario(usuarioDAO.getNextId(UsuarioVO.class));
                usuarioDAO.insert(vo);
            }

            AdicionarEditarReuniaoActivity adicionarEditarReuniaoActivity = (AdicionarEditarReuniaoActivity) getActivity();
            BottomNavigationView bnvNavegacao = (BottomNavigationView) adicionarEditarReuniaoActivity.findViewById(R.id.navigation);
            bnvNavegacao.setSelectedItemId(R.id.navigation_anexos);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void permissaoReady(List<PermissaoVO> lstPermissao) {
        lstPermissao.add(0, new PermissaoVO(0L, "Selecione uma permissão"));
        PermissaoSpinnerAdapter adapter = new PermissaoSpinnerAdapter(this.getContext(), lstPermissao);
        cbPermissao.setAdapter(adapter);
        if (cbCargo.getAdapter() != null) {
            barraProgresso.cancel();
        }
    }

    @Override
    public void cargosReady(List<CargoVO> lstCargos) {
        lstCargos.add(0, new CargoVO(0L, "Selecione um cargo"));
        CargoSpinnerAdapter adapter = new CargoSpinnerAdapter(this.getContext(), lstCargos);
        cbCargo.setAdapter(adapter);
        if (cbPermissao.getAdapter() != null) {
            barraProgresso.cancel();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabAddParticipante) {
            v.setEnabled(false);
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
                    UsuarioVO vo = new UsuarioVO();
                    vo.setIdUsuario(0L);
                    vo.setNomeCompleto(txtNomeCompleto.getText().toString());
                    vo.setEmail(txtEmail.getText().toString());
                    vo.setCargoFK((Long) cbCargo.getSelectedView().getTag());
                    vo.setPermissaoFK((Long) cbPermissao.getSelectedView().getTag());
                    usuarioAdapter.addItem(vo);

                    dlgAdcParticipantes.dismiss();
                }
            });
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlgAdcParticipantes.dismiss();
                }
            });
            dlgAdcParticipantes.show();

            barraProgresso = new ProgressDialog(context);
            barraProgresso.setMessage("Aguarde!");
            barraProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barraProgresso.setCancelable(false);
            barraProgresso.show();

            permissaoService = new PermissaoServiceImpl(getContext(), this);
            permissaoService.listarPermissoes();

            cargoService = new CargoServiceImpl(getContext(), this);
            cargoService.listarCargos();
            v.setEnabled(true);
        }
    }
}
