package br.com.everis.notificacaobeacon.utils;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import br.com.everis.notificacaobeacon.R;

/**
 * Created by wgoncalv on 28/09/2017.
 */

public class UpdateGUI extends TimerTask {
    private Activity context = null;
    private Timer timer = null;

    public UpdateGUI(Activity context, int seconds) {
        this.context = context;
        timer = new Timer();
        timer.schedule(this,
                seconds * 1000,  // initial delay
                seconds * 1000); // subsequent rate
    }

    @Override
    public void run() {
        if(context == null || context.isFinishing()) {
            this.cancel();
            return;
        }

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //ATUALIZANDO O ÍCONE DO SININHO
                atualizarIconeNotificacao();

            }
        });
    }

    private void atualizarIconeNotificacao(){

        FloatingActionButton fabNovaReuniao = (FloatingActionButton) context.findViewById(R.id.fab);
        FloatingActionButton fabNotificacaoReuniao = (FloatingActionButton) context.findViewById(R.id.fabBell);

        boolean notificacaoAtiva = ReuniaoUtils.isNotificacaoAtiva(context.getApplicationContext(), Constants.ID_BEM_VINDO_REUNIAO);

        try {
            final GlobalClass globalVariable = (GlobalClass) context.getApplicationContext();
            if (globalVariable.getReuniaoAcontecendo() && notificacaoAtiva) {
                fabNotificacaoReuniao.setImageResource(R.mipmap.bell_star);
            } else {
                fabNotificacaoReuniao.setImageResource(R.mipmap.bell);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
