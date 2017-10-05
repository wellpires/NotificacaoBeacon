package br.com.everis.notificacaobeacon.service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
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
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.activities.DetalhesReuniaoActivity;
import br.com.everis.notificacaobeacon.activities.ReuniaoMainActivity;
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

                            DateTime dtAgora = new DateTime(new Date());
                            DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDateTime(vo.getHoraInicio()));
                            DateTime dtTermino = new DateTime(ReuniaoUtils.stringToDateTime(vo.getHoraTermino()));
                            Minutes mInicio = Minutes.minutesBetween(dtAgora, dtInicio);
                            Minutes mTermino = Minutes.minutesBetween(dtAgora, dtTermino);

                            if(dtAgora.withTimeAtStartOfDay().isEqual(dtInicio.withTimeAtStartOfDay()) &&
                                    (dtAgora.isBefore(dtInicio) || dtAgora.isBefore(dtTermino))){
                                Duration duration = new Duration(dtAgora, dtInicio);
                                if (dtAgora.isBefore(dtInicio) && ((duration.getStandardMinutes() % 60) == 0 && (duration.getStandardMinutes() / 60) == 1)) {
//                                if (mInicio.getMinutes() <= 60 && mInicio.getMinutes() > 0) {
                                    //REUNIÃO IRÁ COMEÇAR

                                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                    globalVariable.setReuniaoVO(vo);
                                    globalVariable.setReuniaoAcontecera(true);
                                    globalVariable.setReuniaoAcontecendo(false);
                                    notificacaoReuniao(vo, duration.getStandardMinutes());
                                    ReuniaoUtils.cancelarNotificacao(getApplicationContext(), Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO);
                                    break;
                                    //TODO MELHORAMENTO: FAZER COM QUE APAREÇA NOTIFICAÇÕES PARA SE CASO TIVER VÁRIAS REUNIÕES POR DIA.
                                } else if (mInicio.getMinutes() <= 0 && mTermino.getMinutes() > 0) {
                                    //TODO REUNIÃO ACONTECENDO
                                    ReuniaoUtils.cancelarNotificacao(getApplicationContext(), new int[]{Constants.ID_NOTIFICACAO_REUNIAO, Constants.ID_BEM_VINDO_REUNIAO});
                                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                    globalVariable.setReuniaoAcontecera(true);
                                    globalVariable.setReuniaoAcontecendo(true);

                                    String mensagem = Constants.REUNIAO_TERMINARA;
                                    int horas = mTermino.getMinutes() / 60;
                                    int minutos = mTermino.getMinutes() % 60;

                                    if (horas == 0) {
                                        mensagem += ReuniaoUtils.zeroAEsquerda(minutos) + ReuniaoUtils.pluralString(minutos, Constants.MINUTOS_LABEL);
                                    } else if (horas > 0) {
                                        mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, Constants.HORAS_LABEL);
                                    }

                                    ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.ic_meet_table, Constants.REUNIAO_ACONTECENDO, mensagem, null, Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO, Constants.NOTIFICACAO_FIXA);
                                } else if(mInicio.getMinutes() > 60){
                                    GlobalClass gc = (GlobalClass) getApplicationContext();
                                    gc.setReuniaoAcontecera(false);
                                    ReuniaoUtils.cancelarNotificacao(NotificacaoBeaconService.this, new int[]{Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO, Constants.ID_NOTIFICACAO_REUNIAO});
                                } else if(mTermino.getMinutes() <= 0 ){
                                    ReuniaoUtils.cancelarNotificacao(NotificacaoBeaconService.this, Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO);

                                    ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.thumbs_up, Constants.REUNIÃO_FINALIZADA, Constants.VOLTE_SEMPRE, null, Constants.ID_NOTIFICACAO_REUNIAO_FINALIZOU, !Constants.NOTIFICACAO_FIXA);

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
                        if (gc.getReuniaoVO() != null && (gc.getReuniaoAcontecera() != null && gc.getReuniaoAcontecera()) && !gc.getReuniaoAcontecendo()) {
                            if (!ReuniaoUtils.isNotificacaoAtiva(getApplicationContext(), Constants.ID_BEM_VINDO_REUNIAO)) {
                                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                globalVariable.setReuniaoAcontecera(true);
                                Intent intent = new Intent(getApplicationContext(), ReuniaoMainActivity.class);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                stackBuilder.addParentStack(ReuniaoMainActivity.class);
                                stackBuilder.addNextIntent(intent);
                                Intent intentEmailView = new Intent(getApplicationContext(), DetalhesReuniaoActivity.class);
                                stackBuilder.addNextIntent(intentEmailView);
                                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                                ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.keditbookmarks, Constants.BEM_VINDO, Constants.CONTINUACAO_BEM_VINDO, pendingIntent, Constants.ID_BEM_VINDO_REUNIAO, !Constants.NOTIFICACAO_FIXA);

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
        globalVariable.setReuniaoAcontecera(false);
        if (threadNotificacao != null) {
            threadNotificacao.interrupt();
        }
        ReuniaoUtils.cancelarNotificacao(this, Constants.ID_BEM_VINDO_REUNIAO);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    private void notificacaoReuniao(ReuniaoVO vo, long qtdeMinutos) {

        int horas = (int) (qtdeMinutos / 60);
        int minutos = (int) (qtdeMinutos % 60);
        String mensagem = Constants.MENSAGEM_REUNIAO;

        if (horas == 0) {
            mensagem += ReuniaoUtils.zeroAEsquerda(minutos) + ReuniaoUtils.pluralString(minutos, Constants.MINUTOS_LABEL);
        } else if (horas > 0) {
            mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, Constants.HORAS_LABEL);
        }

        Intent intent = new Intent(getApplicationContext(), ReuniaoNotificacaoActivity.class);
        intent.putExtra(Constants.TEMPO_RESTANTE_KEY, qtdeMinutos);
        intent.putExtra(Constants.MENSAGEM_KEY, mensagem);
        intent.putExtra(Constants.LOCAL_KEY, vo.getLocal());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(ReuniaoMainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.stock_new_meeting, Constants.REUNIAO, mensagem, resultPendingIntent, Constants.ID_NOTIFICACAO_REUNIAO, !Constants.NOTIFICACAO_FIXA);

    }

}
