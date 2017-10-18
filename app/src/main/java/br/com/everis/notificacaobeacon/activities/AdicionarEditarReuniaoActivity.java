package br.com.everis.notificacaobeacon.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.bd.UsuarioDAO;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;

public class AdicionarEditarReuniaoActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,View.OnClickListener, ReuniaoPresenterListener{

    private Menu menu = null;

    private UsuarioDAO usuarioDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar_reuniao);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ReuniaoFragment.newInstance(getSupportActionBar()));
        transaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_reuniao:
                selectedFragment = ReuniaoFragment.newInstance(getSupportActionBar());
                break;
            case R.id.navigation_participantes:
                selectedFragment = ParticipanteFragment.newInstance(getSupportActionBar());
                break;
            case R.id.navigation_anexos:
                selectedFragment = AnexoFragment.newInstance(getSupportActionBar());
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_button_actionbar, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
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
    public void onClick(View v) {

    }
}
