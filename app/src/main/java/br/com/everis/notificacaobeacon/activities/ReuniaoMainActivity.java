package br.com.everis.notificacaobeacon.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.ReunioesHojeAdapter;
import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.service.IReuniaoService;
import br.com.everis.notificacaobeacon.service.NotificacaoBeaconService;
import br.com.everis.notificacaobeacon.service.impl.ReuniaoServiceImpl;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.GlobalClass;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;
import br.com.everis.notificacaobeacon.utils.UpdateGUI;

public class ReuniaoMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ReuniaoPresenterListener {

    private TextView lblEmptyListMain = null;

    private FloatingActionButton fabNovaReuniao = null;
    private FloatingActionButton fabNotificacaoReuniao = null;

    private ListView lvReunioes = null;

    private ProgressDialog barraProgresso = null;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private IReuniaoService reuniaoService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        bluetooth.enable();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lblEmptyListMain = (TextView) findViewById(R.id.lblEmptyListMain);

        fabNovaReuniao = (FloatingActionButton) findViewById(R.id.fab);
        fabNovaReuniao.setOnClickListener(this);

        fabNotificacaoReuniao = (FloatingActionButton) findViewById(R.id.fabBell);
        fabNotificacaoReuniao.setOnClickListener(this);

        lvReunioes = (ListView) findViewById(R.id.lvReunioes);

        lblEmptyListMain.setText(Constants.LABEL_NENHUMA_REUNIAO);
        lvReunioes.setEmptyView(lblEmptyListMain);
        // ========== SININHO ==========
        UpdateGUI updateBell = new UpdateGUI(this, 1);
        updateBell.run();
        // ========== START SERVICE ==========
        Intent iService = new Intent(ReuniaoMainActivity.this, NotificacaoBeaconService.class);
        startService(iService);
        //===========================================

        barraProgresso = new ProgressDialog(this);
        barraProgresso.setMessage("Aguarde!");
        barraProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barraProgresso.setIndeterminate(true);
        barraProgresso.show();

        try {
            buscarReunioes();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reunioes) {
            Intent i = new Intent(ReuniaoMainActivity.this, ReunioesActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(final View view) {

        if (view.getId() == R.id.fab) {
            Intent i = new Intent(getApplicationContext(), AdicionarEditarReuniaoActivity.class);
            i.putExtra(Constants.NOVA_REUNIAO_KEY, Constants.FLAG_NOVA_REUNIAO);
            startActivity(i);
        } else if (view.getId() == R.id.fabBell) {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            if (globalVariable.getReuniaoAcontecera()) {
                Intent i = new Intent(getApplicationContext(), DetalhesReuniaoActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, Constants.SEM_REUNIAO_ATUAL, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void excluirRegistro(final View view) {
        ReuniaoUtils.mostrarPerguntaDialogo(ReuniaoMainActivity.this, Constants.LABEL_VOCE_TEM_CERTEZA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                barraProgresso.setMessage("Aguarde!");
                barraProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                barraProgresso.setIndeterminate(true);
                barraProgresso.show();
                int id = Integer.parseInt(view.getTag().toString());
                ReuniaoVO r = new ReuniaoVO();
                r.setIdReuniao(id);
                try {
                    reuniaoService.removerReuniao(r);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO QUANDO NÃO TIVER REUNIÕES NA LISTA, NÃO É PARA APARECER O PUSH DE 'BEM VINDO Á EVERIS' -> TESTAR
    //TODO ADICIONAR PARTICIPANTES PARA ENVIAR EMAILS

    private void listarReunioes(List<ReuniaoVO> lstReunioes) {
        try {
            ReunioesHojeAdapter adapter = new ReunioesHojeAdapter(lstReunioes, this);
            lvReunioes.setAdapter(adapter);
            ReuniaoUtils.popularBancoLocal(getApplicationContext(), lstReunioes);
            if (lvReunioes.getAdapter().getCount() <= 0) {
                GlobalClass gc = (GlobalClass) getApplicationContext();
                gc.setReuniaoAcontecera(false);
                ReuniaoUtils.cancelarNotificacao(this, new int[]{Constants.ID_BEM_VINDO_REUNIAO, Constants.ID_NOTIFICACAO_REUNIAO, Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO});
            }
            barraProgresso.cancel();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void buscarReunioes() throws Exception {
        ReuniaoVO r = new ReuniaoVO();
        r.setDtInicio(ReuniaoUtils.dateTimeToString(new Date()));
        reuniaoService = new ReuniaoServiceImpl(this, this);
        reuniaoService.buscarReunioes(r);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void reunioesReady(List<ReuniaoVO> lstReunioes) {
        listarReunioes(lstReunioes);
    }

    @Override
    public void reuniaoReady(ReuniaoVO reuniaoVO) {

    }

    @Override
    public void reuniaoReady() {
        try {
            buscarReunioes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reuniaoFailed(RestException exception) {
        barraProgresso.dismiss();
        ReuniaoUtils.mostrarAvisoDialogo(this, exception.getMessage());
    }
}
