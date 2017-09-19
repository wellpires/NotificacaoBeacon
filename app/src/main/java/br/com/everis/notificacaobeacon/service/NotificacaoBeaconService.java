package br.com.everis.notificacaobeacon.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.everis.notificacaobeacon.DetalhesReuniaoActivity;
import br.com.everis.notificacaobeacon.NotificacaoMainActivity;
import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.ReuniaoNotificacaoActivity;
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

    public NotificacaoBeaconService() {
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        //======BEACON=======
        try {
            region = new Region("pocEverisBeacon", Identifier.parse("813c5c55-3a33-4c52-bc86-22cb7d49fc5c"), Identifier.parse("852"), Identifier.parse("258"));
            beaconManager = BeaconManager.getInstanceForApplication(this);
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            beaconManager.setForegroundScanPeriod(1900);
            beaconManager.setForegroundBetweenScanPeriod(100);
            beaconManager.setBackgroundScanPeriod(1900);
            beaconManager.setBackgroundBetweenScanPeriod(100);
            beaconManager.updateScanPeriods();

            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        regionBootstrap = new RegionBootstrap(this, region);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //========================
                    try {
                        Thread.sleep(5000);

                        List<ReuniaoVO> lstReunioes = new ArrayList<>();
                        datasource = new DBAdapter(getApplicationContext());
                        datasource.open();
                        Cursor c = datasource.getReuniao();
                        c.moveToFirst();
                        while (c.isAfterLast() == false) {

                            int indice = 0;
                            ReuniaoVO vo = new ReuniaoVO();
                            vo.setId(c.getInt(indice++));
                            vo.setAssunto(c.getString(indice++));
                            vo.setHoraInicio(c.getString(indice++));
                            vo.setHoraTermino(c.getString(indice++));
                            vo.setLocal(c.getString(indice++));
                            vo.setSala(c.getString(indice++));
                            vo.setDetalhes(c.getString(indice++));
                            lstReunioes.add(vo);

                            c.moveToNext();
                        }
                        datasource.close();

                        List<ReuniaoVO> lstReunioesHoje = new ArrayList<>();
                        for (ReuniaoVO vo : lstReunioes) {

                            DateTime dtHoje = new DateTime(new Date());
                            DateTime dtInicio = new DateTime(ReuniaoUtils.stringToDate(vo.getHoraInicio()));

                            Hours h = Hours.hoursBetween(dtHoje, dtInicio);

                            if (h.getHours() == 1) {
                                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                globalVariable.setReuniaoVO(vo);
                                notificacaoReuniao(vo, h.getHours());
                                break; //TODO MELHORAMENTO: FAZER COM QUE APAREÇA NOTIFICAÇÕES PARA SE CASO TIVER VÁRIAS REUNIÕES POR DIA.
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
        //TODO PROCURAR UMA FORMA DE CRIAR UM SERVICE EM LOOP EM BACKGROUND, POIS O DE HOJE SÓ EXECUTA UMA VEZ.
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

        Intent intent = new Intent(getApplicationContext(), NotificacaoMainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(NotificacaoMainActivity.class);
        stackBuilder.addNextIntent(intent);
        Intent intentEmailView = new Intent(getApplicationContext(), DetalhesReuniaoActivity.class);
        stackBuilder.addNextIntent(intentEmailView);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setContentTitle("Bem vindo á Everis!")
                .setContentText("Toque para ver detalhes da reunião.")
                .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "Got a didExitRegion call");
        //TODO APAGAR A NOTIFICAÇÃO QUANDO SAIR DA REGIÃO DO BEACON
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    private void notificacaoReuniao(ReuniaoVO vo, int qtdeHoras) {

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setContentTitle(Constants.REUNIAO)
                .setContentText("Sua reunião vai começar em " + qtdeHoras + " hora");

        Intent intent = new Intent(getApplicationContext(), ReuniaoNotificacaoActivity.class);
        intent.putExtra(Constants.TEMPO_RESTANTE, qtdeHoras);
        intent.putExtra(Constants.LOCAL, vo.getLocal());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(NotificacaoMainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }


}
