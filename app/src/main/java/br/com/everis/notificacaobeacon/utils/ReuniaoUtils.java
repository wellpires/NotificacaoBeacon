package br.com.everis.notificacaobeacon.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import br.com.everis.notificacaobeacon.bd.DBHelper;
import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;

/**
 * Created by wgoncalv on 16/09/2017.
 */

public class ReuniaoUtils {

    public static void showNotification(String title, String conteudo, int icone, Context cx){
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(cx)
                .setContentTitle(title)
                .setContentText(conteudo)
                .setSmallIcon(icone);
    }

    public static Date stringToDate(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE));
        return new Date(sdf.parse(data).getTime());
    }

    public static String dateToString(Date data){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PATTERN);
        return sdf.format(data);
    }

    public static String timeToString(Date hora){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_PATTERN);
        return sdf.format(hora);
    }

    public static Date stringToTime(String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE));
        return new Date(sdf.parse(hora).getTime());
    }

    public static String zeroAEsquerda(int numero){
        String numeroFinal = String.valueOf(numero);
        if(numeroFinal.length() == 1){
            numeroFinal = "0" + numeroFinal;
        }
        return numeroFinal;
    }

    public static List<ReuniaoVO> cursorToList(Cursor cursor){

        List<ReuniaoVO> lstReunioes = new ArrayList<>();
        //ursor.moveToFirst();
        while(cursor.moveToNext()){
            ReuniaoVO vo = new ReuniaoVO();
            vo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))));
            vo.setAssunto(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ASSUNTO)));
            vo.setHoraInicio(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_HORA_INICIO)));
            vo.setHoraTermino(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_HORA_TERMINO)));
            vo.setLocal(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LOCAL)));
            vo.setSala(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SALA)));
            vo.setDetalhes(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DETALHES)));
            lstReunioes.add(vo);
        }
        return lstReunioes;
    }

    public static void mostrarAvisoDialogo(Context context, String message){
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(Constants.TITULO_APP);
        dlg.setMessage(message);
        dlg.setNeutralButton(Constants.LABEL_OK, null);
        dlg.show();
    }

    public static String pluralString(int valor, String mensagem){
        return valor > 1 ? mensagem + "s" : mensagem;
    }

    public static boolean isNotificacaoAtiva(Context c, int idNotificacao){
        NotificationManager notificacao = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        for(StatusBarNotification sbn : notificacao.getActiveNotifications()){
            if(sbn.getId() == idNotificacao){
                return true;
            }
        }
        return false;
    }

}
