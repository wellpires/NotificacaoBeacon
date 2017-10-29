package br.com.everis.notificacaobeacon.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.JsonArray;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.activities.DetalhesReuniaoActivity;
import br.com.everis.notificacaobeacon.activities.ReuniaoMainActivity;
import br.com.everis.notificacaobeacon.activities.ReuniaoNotificacaoActivity;
import br.com.everis.notificacaobeacon.bd.DAOHelper;
import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.service.impl.GoogleServiceImpl;
import br.com.everis.notificacaobeacon.service.impl.ReuniaoServiceImpl;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.GlobalClass;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class NotificacaoBeaconService extends Service implements BootstrapNotifier, BeaconConsumer, ReuniaoPresenterListener {

    protected static final String TAG = "NotificacaoActivity";
    private BeaconManager beaconManager = null;
    private Region region = null;

    private DAOHelper<ReuniaoVO> reuniaoDAO = null;

    private Thread threadNotificacao = null;

    private boolean pararWhileTrue = false;

    private IReuniaoService reuniaoService = null;

    public NotificacaoBeaconService() {
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            region = new Region("pocEverisBeacon", Identifier.parse(Constants.UUID_BEACON), Identifier.parse(Constants.MAJOR_BEACON), Identifier.parse(Constants.MINOR_BEACON));
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(Constants.LAYOUT_BEACON));
            beaconManager.setForegroundScanPeriod(1900);
            beaconManager.setForegroundBetweenScanPeriod(100);
            beaconManager.setBackgroundScanPeriod(1900);
            beaconManager.setBackgroundBetweenScanPeriod(100);
            beaconManager.updateScanPeriods();

            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            ReuniaoUtils.mostrarAvisoDialogo(this, e.getLocalizedMessage());
        }
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        //======BEACON=======
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

        reuniaoService = new ReuniaoServiceImpl(this, this);

        //====================

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        //VERIFICAR SE A ACTIVITY ATUAL É A DA INICIAL
                        if(!ReuniaoUtils.checkCurrentActivity(getApplicationContext(), ReuniaoMainActivity.class)){
                            return;
                        }
                        //10 SEGUNDOS
                        Thread.sleep(10000);
                        ReuniaoVO r = new ReuniaoVO();
                        r.setDtInicio(new Date());
                        reuniaoService.buscarReunioes(r);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        RegionBootstrap regionBootstrap = new RegionBootstrap(this, region);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //========================
                    try {

                        //VERIFICAR SE A ACTIVITY ATUAL É A DA INICIAL
                        if(!ReuniaoUtils.checkCurrentActivity(getApplicationContext(), ReuniaoMainActivity.class)){
                            return;
                        }

                        Thread.sleep(5000);

                        reuniaoDAO = new DAOHelper<>();
//                        List<ReuniaoVO> lstReunioes = reuniaoDAO.detachFromRealm(reuniaoDAO.findAll(ReuniaoVO.class));
                        List<ReuniaoVO> lstReunioes = reuniaoDAO.detachFromRealm(reuniaoDAO.findByDate(ReuniaoVO.class, new Date()));

                        for (ReuniaoVO vo : lstReunioes) {

                            Location current = ReuniaoUtils.getCurrentLocation(getApplicationContext());
                            IGoogleService googleService = new GoogleServiceImpl();
                            Integer tempoDistancia = googleService.buscarTempoDistancia(vo.getLatitude() + "," + vo.getLongitude(), current.getLatitude() + "," + current.getLongitude());

                            //TEMPO DE DISTÂNCIA MAIS 20 MINUTOS, POIS ELE DEVERÁ CHEGAR COM 20 MINUTOS DE ANTECEDÊNCIA.
                            Integer tempoTotal = tempoDistancia + Constants.MINUTOS_ANTECEDENCIA;

                            DateTime dtAgora = new DateTime(new Date());
                            DateTime dtInicio = new DateTime(vo.getDtInicio());
                            DateTime dtTermino = new DateTime(vo.getDtTermino());
                            Minutes mTermino = Minutes.minutesBetween(dtAgora, dtTermino);

                            if (dtAgora.withTimeAtStartOfDay().isEqual(dtInicio.withTimeAtStartOfDay())) {
                                Duration duration = new Duration(dtAgora, dtInicio);
                                if (dtAgora.isBefore(dtInicio) || dtAgora.isBefore(dtTermino)) {
                                    if (dtAgora.isBefore(dtInicio) && duration.getStandardMinutes() < tempoTotal) {
                                        //REUNIÃO IRÁ COMEÇAR
                                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                        globalVariable.setReuniaoVO(vo);
                                        globalVariable.setReuniaoAcontecera(true);
                                        globalVariable.setReuniaoAcontecendo(false);
                                        notificacaoReuniao(vo, duration.getStandardMinutes());
                                        ReuniaoUtils.cancelarNotificacao(getApplicationContext(), Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO);
                                        break;
                                        //TODO MELHORAMENTO: FAZER COM QUE APAREÇA NOTIFICAÇÕES PARA SE CASO TIVER VÁRIAS REUNIÕES POR DIA.
                                    } else if ((dtInicio.isEqualNow() || dtAgora.isAfter(dtInicio)) && (dtTermino.isEqualNow() || dtAgora.isBefore(dtTermino))) {
                                        //REUNIÃO ACONTECENDO
                                        ReuniaoUtils.cancelarNotificacao(getApplicationContext(), new int[]{Constants.ID_NOTIFICACAO_REUNIAO, Constants.ID_BEM_VINDO_REUNIAO});
                                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                        globalVariable.setReuniaoAcontecera(true);
                                        globalVariable.setReuniaoAcontecendo(true);

                                        String mensagem = formatarMensagem(mTermino.getMinutes(), Constants.REUNIAO_TERMINARA);

                                        ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.ic_meet_table, Constants.REUNIAO_ACONTECENDO, mensagem, null, Constants.ID_NOTIFICACAO_REUNIAO_ACONTECENDO, Constants.NOTIFICACAO_FIXA);
                                    } else if (duration.getStandardMinutes() > tempoTotal) {
                                        //REUNIÃO AINDA NÃO COMEÇOU
                                        GlobalClass gc = (GlobalClass) getApplicationContext();
                                        gc.setReuniaoAcontecera(false);
                                        ReuniaoUtils.cancelarTodasNotificacoes(getApplicationContext());
                                    }
                                } else if (dtTermino.isBeforeNow() && new Duration(dtAgora, dtTermino).getStandardMinutes() == 0) {
                                    //VERIFICAR SE O HORÁRIO DE TERMINO É DEPOIS DE AGORA E SE A DIFERENÇA EM MINUTOS DO TERMINO PARA O INICIO SEJA IGUAL A 0
                                    //REUNIÃO TERMINOU
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
                        //FIXME:  ALTERAR PARA QUE NÃO PRECISE DAR ESSE RETORNO, MAS ALTERE O VALOR DA VARIÁVEL pararWhileTrue PARA FALSE ASSIM, PODENDO RETORNAR PARA O LOOPING QUANDO A VÁRIAVEL FOR NOVAMENTE TRUE.
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

                        } else if (ReuniaoUtils.isNotificacaoAtiva(getApplicationContext(), Constants.ID_BEM_VINDO_REUNIAO)) {
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

    private void notificacaoReuniao(final ReuniaoVO vo, final long qtdeMinutos) {

        Location location = new Location("");
        location.setLatitude(vo.getLatitude());
        location.setLongitude(vo.getLongitude());

        String mensagem = formatarMensagem((int) (qtdeMinutos), Constants.MENSAGEM_REUNIAO);

        Intent intent = new Intent(getApplicationContext(), ReuniaoNotificacaoActivity.class);
        intent.putExtra(Constants.TEMPO_RESTANTE_KEY, qtdeMinutos);
        intent.putExtra(Constants.MENSAGEM_KEY, mensagem);
        intent.putExtra(Constants.LOCAL_KEY, location);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(ReuniaoMainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        ReuniaoUtils.mostrarNotificacao(getApplicationContext(), R.mipmap.stock_new_meeting, Constants.REUNIAO, mensagem, resultPendingIntent, Constants.ID_NOTIFICACAO_REUNIAO, !Constants.NOTIFICACAO_FIXA);

    }

    private String formatarMensagem(int m, String mensagem) {

        int horas = m / 60;
        int minutos = m % 60;

        if (horas < 1) {
            mensagem += ReuniaoUtils.zeroAEsquerda(minutos) + ReuniaoUtils.pluralString(minutos, Constants.MINUTOS_LABEL);
        } else if (horas == 1) {
            if (minutos == 0) {
                mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, Constants.HORAS_LABEL);
            } else if (minutos > 0) {
                mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, Constants.HORAS_LABEL) + " e " + ReuniaoUtils.zeroAEsquerda(minutos) + ReuniaoUtils.pluralString(minutos, Constants.MINUTOS_LABEL);
            }
        } else {
            if (minutos == 0) {
                mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, Constants.HORAS_LABEL);
            } else if (minutos > 0) {
                mensagem += ReuniaoUtils.zeroAEsquerda(horas) + ReuniaoUtils.pluralString(horas, Constants.HORAS_LABEL) + " e " + ReuniaoUtils.zeroAEsquerda(minutos) + ReuniaoUtils.pluralString(minutos, Constants.MINUTOS_LABEL);
            }
        }
        return mensagem;
    }

    @Override
    public void reunioesReady(List<ReuniaoVO> lstReunioes) {
        try {
            ReuniaoUtils.popularBancoLocal(getApplicationContext(), lstReunioes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reuniaoReady(ReuniaoVO reuniaoVO) {

    }

    @Override
    public void reuniaoReady(JsonArray usuarios) {

    }

    @Override
    public void reuniaoReady() {

    }

    @Override
    public void reuniaoFailed(RestException exception) {

    }

}
