package br.com.everis.notificacaobeacon.utils;

import android.content.Context;
import android.support.v7.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    public static String zeroAEsquerda(int numero){
        String numeroFinal = String.valueOf(numero);
        if(numeroFinal.length() == 1){
            numeroFinal = "0" + numeroFinal;
        }
        return numeroFinal;
    }

}
