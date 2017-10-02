package br.com.everis.notificacaobeacon.service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.activities.DetalhesReuniaoActivity;
import br.com.everis.notificacaobeacon.activities.NotificacaoMainActivity;
import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.activities.ReuniaoNotificacaoActivity;
import br.com.everis.notificacaobeacon.bd.DBAdapter;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.GlobalClass;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class NotificacaoBeaconService extends Service implements BootstrapNotifier {

    protected static final String TAG = "NotificacaoActivity";
    private BeaconManager beaconManager = null;
    private Region region = null;
    private RegionBootstrap regionBootstrap = null;

    private DBAdapter datasource = null;

    private Thread threadNotificacao = null;

    private boolean pararWhileTrue = false;

    public NotificacaoBeaconService() {
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        //======BEACON=======
        try {
            region = new Region("pocEverisBeacon", Identifier.parse(Constants.UUID_BEACON), Identifier.parse(Constants.MAJOR_BEACON), Identifier.parse(Constants.MINOR_BEACON));
            beaconManager = BeaconManager.getInstanceForApplication(this);
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(Constants.LAYOUT_BEACON));
            beaconManager.setForegroundScanPeriod(1900);
            beaconManager.setForegroundBetweenScanPeriod(100);
            beaconManager.setBackgroundScanPeriod(1900);
            beaconManager.setBackgroundBetweenScanPeriod(100);
            beaconManager.updateScanPeriods();

            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //====================

        regionBootstrap = new RegionBootstrap(this, region);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //========================
                    try {
                        Thread.sleep(5000);

                        datasource = new DBAdapter(getApplicationContext());
                        datasource.open();
                        List<ReuniaoVO> lstReunioes = datasource.getReunioes();
                        datasource.close();

                        List<ReuniaoVO> lstReunioesHoje = new ArrayList<>();
                        for (ReuniaoVO vo : lstReunioes) {

                            DateTime dtHoje = new DateTime(new Date());
                            DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(vo.getHoraInicio()));
                            Minutes m = Minutes.minutesBetween(dtHoje, dtInicio);

                            if(dtHoje.withTimeAtStartOfDay().isEqual(dtInicio.withTimeAtStartOfDay())){
                                if (m.getMinutes() <= 60 && m.getMinutes() > 0) {
                                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                    globalVariable.setReuniaoVO(vo);
                                    globalVariable.setReuniaoAcontecendo(true);
                                    notificacaoReuniao(vo, m.getMinutes());
                                    break;
                                    //TODO MELHORAMENTO: FAZER COM QUE APAREÇA NOTIFICAÇÕES PARA SE CASO TIVER VÁRIAS REUNIÕES POR DIA.
                                } else if (m.getMinutes() == 0) {
                                    //TODO REUNIÃO ACONTECENDO
                                } else if(m.getMinutes() > 60){
                                    GlobalClass gc = (GlobalClass) getApplicationContext();
                                    gc.setReuniaoAcontecendo(false);
                                    ReuniaoUtils.cancelarNotificacao(NotificacaoBeaconService.this, Constants.ID_NOTIFICACAO_REUNIAO);
                                }
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return Service.START_NOT_STICKY;
        //TODO COLOCAR UMA REGRA QUE LIMITE UMA NOIFICAÇÃO
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "Got a didEnterRegion call");
        Log.i(TAG, "Beacon: " + region.getBluetoothAddress());
        pararWhileTrue = false;

        threadNotificacao = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (pararWhileTrue) {
                        //ESTE IF É USADO POIS NÃO ESTAVA CONSEGUINDO PARAR A THREAD DE OUTRA FORMA
                        return;
                    }
                    try {
                        Thread.sleep(5000);
                        GlobalClass gc = (GlobalClass) getApplicationContext();
                        if (gc.getReuniaoVO() != null && (gc.getReuniaoAcontecendo() != null && gc.getReuniaoAcontecendo())) {
                            if (!ReuniaoUtils.isNotificacaoAtiva(getApplicationContext(), Constants.ID_BEM_VINDO_REUNIAO)) {
                                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                globalVariable.setReuniaoAcontecendo(true);
                                Intent intent = new Intent(getApplicationContext(), NotificacaoMainActivity.class);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                stackBuilder.addParentStack(NotificacaoMainActivity.class);
                                stackBuilder.addNextIntent(intent);
                                Intent intentEmailView = new Intent(getApplicationContext(), DetalhesReuniaoActivity.class);
                                stackBuilder.addNextIntent(intentEmailView);
                                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                                ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.keditbookmarks, Constants.BEM_VINDO, Constants.CONTINUACAO_BEM_VINDO, pendingIntent, Constants.ID_BEM_VINDO_REUNIAO);

                            }

                        } else if(ReuniaoUtils.isNotificacaoAtiva(getApplicationContext(), Constants.ID_BEM_VINDO_REUNIAO)){
                            ReuniaoUtils.cancelarNotificacao(NotificacaoBeaconService.this, Constants.ID_BEM_VINDO_REUNIAO);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        threadNotificacao.start();

        Log.d(TAG, "Thread ID: " + threadNotificacao.getId());
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "Got a didExitRegion call");
        Log.d(TAG, "Thread ID: " + threadNotificacao.getId());
        pararWhileTrue = true;
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setReuniaoAcontecendo(false);
        if (threadNotificacao != null) {
            threadNotificacao.interrupt();
        }
        ReuniaoUtils.cancelarNotificacao(this, Constants.ID_BEM_VINDO_REUNIAO);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    private void notificacaoReuniao(ReuniaoVO vo, int qtdeMinutos) {

        int horas = qtdeMinutos / 60;
        int minutos = qtdeMinutos % 60;
        String mensagem = Constants.MENSAGEM_REUNIAO;

        if (horas == 0) {
            mensagem += ReuniaoUtils.zeroAEsquerda(minutos) + ReuniaoUtils.pluralString(minutos, " minuto");
        } else if (horas > 0) {
            mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, " hora");
        }


        Intent intent = new Intent(getApplicationContext(), ReuniaoNotificacaoActivity.class);
        intent.putExtra(Constants.TEMPO_RESTANTE_KEY, qtdeMinutos);
        intent.putExtra(Constants.MENSAGEM_KEY, mensagem);
        intent.putExtra(Constants.LOCAL_KEY, vo.getLocal());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(NotificacaoMainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.stock_new_meeting, Constants.REUNIAO, mensagem, resultPendingIntent, Constants.ID_NOTIFICACAO_REUNIAO);

    }

}
