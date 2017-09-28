package br.com.everis.notificacaobeacon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.adapter.ReuniaoAdapter;
import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.service.NotificacaoBeaconService;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.GlobalClass;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;
import br.com.everis.notificacaobeacon.utils.UpdateGUI;

public class NotificacaoMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnEnviar = null;
    private Button btnBeacon = null;

    private FloatingActionButton fabNovaReuniao = null;
    private FloatingActionButton fabNotificacaoReuniao = null;

    private ListView lvReunioes = null;

    private DBAdapter datasource = null;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabNovaReuniao = (FloatingActionButton) findViewById(R.id.fab);
        fabNovaReuniao.setOnClickListener(this);

        fabNotificacaoReuniao = (FloatingActionButton) findViewById(R.id.fabBell);
        fabNotificacaoReuniao.setOnClickListener(this);

        lvReunioes = (ListView) findViewById(R.id.lvReunioes);
        lvReunioes.setOnItemClickListener(this);

        // ========== SININHO ==========
        UpdateGUI updateBell = new UpdateGUI(this, 1);
        updateBell.run();
        // ========== START SERVICE ==========
        Intent i = new Intent(NotificacaoMainActivity.this, NotificacaoBeaconService.class);
        startService(i);
        //===========================================

        listarReunioes();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notificacao_main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {
            Intent i = new Intent(getApplicationContext(), AdicionarReuniaoActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.fabBell) {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            if (globalVariable.getReuniaoAcontecendo()) {
                Intent i = new Intent(getApplicationContext(), DetalhesReuniaoActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, Constants.SEM_REUNIAO_ATUAL, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PopupMenu popupMenu = new PopupMenu(NotificacaoMainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    public void showMenu(final View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_item) {
                    Toast.makeText(NotificacaoMainActivity.this, "EDITANDO", Toast.LENGTH_LONG).show();
                } else if (item.getItemId() == R.id.delete_item) {
                    ReuniaoUtils.mostrarPerguntaDialogo(NotificacaoMainActivity.this, "Você tem certeza?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int id = Integer.parseInt(view.getTag().toString());
                            datasource = new DBAdapter(getApplicationContext());
                            datasource.open();
                            datasource.deleteReuniao(id);
                            datasource.close();
                            listarReunioes();
                        }
                    });
                }
                return true;
            }
        });
        menu.inflate(R.menu.item_menu);
        menu.show();
    }

    private void listarReunioes() {
        try {
            datasource = new DBAdapter(getApplicationContext());
            datasource.open();
            Cursor cursor = datasource.getReunioes();
            List<ReuniaoVO> reunioes = ReuniaoUtils.cursorToList(cursor);
            List<ReuniaoVO> reunioesFiltradas = new ArrayList<>();
            for (ReuniaoVO vo : reunioes) {
                DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDate(vo.getHoraInicio()));
                DateTime dtHoje = new DateTime(new Date());
                if (dtInicio.isAfter(dtHoje) || dtInicio.isAfterNow()) {
                    reunioesFiltradas.add(vo);
                }
            }

            ReuniaoAdapter adapter = new ReuniaoAdapter(reunioesFiltradas, this);

            lvReunioes.setAdapter(adapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
