package br.com.everis.notificacaobeacon.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by wgoncalv on 16/09/2017.
 */

public class ReuniaoUtils {

    public static void showNotification(String title, String conteudo, int icone, Context cx) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(cx)
                .setContentTitle(title)
                .setContentText(conteudo)
                .setSmallIcon(icone);
    }

    public static Date stringToDateTime(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE));
        return new Date(sdf.parse(data).getTime());
    }

    public static String dateTimeToString(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PATTERN);
        return sdf.format(data);
    }

    public static String timeToString(Date hora) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_PATTERN);
        return sdf.format(hora);
    }

    public static Date stringToTime(String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE));
        return new Date(sdf.parse(hora).getTime());
    }

    public static Date stringToDate(String data) throws ParseException {
        return new Date(formatStringDate(Constants.DATE_PATTERN, data).getTime());
    }

    public static String dateToString(Date data) {
        return new SimpleDateFormat(Constants.DATE_PATTERN).format(data);
    }

    public static Date formatHour(String pattern, String hora) throws ParseException {
        return new Date(new SimpleDateFormat(pattern).parse(hora).getTime());
    }

    public static Date formatStringDate(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static String formatDate(String pattern, Date date) throws ParseException {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String zeroAEsquerda(int numero) {
        String numeroFinal = String.valueOf(numero);
        if (numeroFinal.length() == 1) {
            numeroFinal = "0" + numeroFinal;
        }
        return numeroFinal;
    }

    public static void mostrarAvisoDialogo(Context context, String message) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(Constants.TITULO_APP);
        dlg.setMessage(message);
        dlg.setNeutralButton(Constants.LABEL_OK, null);
        dlg.show();
    }

    public static void mostrarPerguntaDialogo(Context context, String message, DialogInterface.OnClickListener eventoSim) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(Constants.TITULO_APP);
        dlg.setMessage(message);
        dlg.setPositiveButton(Constants.LABEL_SIM, eventoSim);
        dlg.setNegativeButton(Constants.LABEL_NAO, null);
        dlg.show();
    }

    public static String pluralString(int valor, String mensagem) {
        return valor > 1 ? mensagem + "s" : mensagem;
    }

    public static boolean isNotificacaoAtiva(Context c, int idNotificacao) {
        NotificationManager notificacao = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        for (StatusBarNotification sbn : notificacao.getActiveNotifications()) {
            if (sbn.getId() == idNotificacao) {
                return true;
            }
        }
        return false;
    }

    public static void cancelarNotificacao(Context c, int idReuniao) {
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(idReuniao);
    }

    public static void esconderTeclado(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isEmptyOrNull(String value) {
        return value == null || "".equals(value.trim());
    }

    public static void mostrarNotificacao(Context context, int icon, String title, String content, PendingIntent pendingIntent, int idNotificacao) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        if(pendingIntent != null){
            mBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(idNotificacao, mBuilder.build());
    }

}
