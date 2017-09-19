package br.com.everis.notificacaobeacon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListAdapter;

import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.DBHelper;
import br.com.everis.notificacaobeacon.service.NotificacaoBeaconService;

public class NotificacaoMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button btnEnviar = null;
    private Button btnBeacon = null;

    private FloatingActionButton fabNovaReuniao = null;
    private FloatingActionButton fabNotificacaoReuniao = null;

    private ListAdapter reunioesAdapter = null;

    private DBAdapter datasource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO PROCURAR UMA FORMA DE IMPLEMENTAR UMA FORMA DE PERMISSÃO DO ANDROID PELO CÓDIGO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // ========== START SERVICE ==========
        Intent i = new Intent(NotificacaoMainActivity.this, NotificacaoBeaconService.class);
        startService(i);
        //===========================================

        datasource = new DBAdapter(this);
        datasource.open();
        Cursor cursor = datasource.getReuniao();

        String[] colunas = new String[]{"Assunto", "Horario de Inicio"};
        int[] row = new int[]{R.id.nome, R.id.telefone};



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
        } else if(view.getId() == R.id.fabBell){

        }
    }

}
