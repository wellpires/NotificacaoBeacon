package br.com.everis.notificacaobeacon.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import br.com.everis.notificacaobeacon.activities.ReuniaoMainActivity;
import br.com.everis.notificacaobeacon.bd.DAOHelper;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;

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
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PATTERN, Locale.US);
        return new Date(sdf.parse(data).getTime());
    }

    public static String dateTimeToString(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.SCREEN_DATETIME_PATTERN, Locale.US);
        return sdf.format(data);
    }

    public static String timeToString(Date hora) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.SCREEN_TIME_PATTERN, Locale.US);
        return sdf.format(hora);
    }

    public static String dateToString(Date data) {
        return new SimpleDateFormat(Constants.SCREEN_DATE_PATTERN, Locale.US).format(data);
    }

    public static Date stringToDate(String data) throws ParseException {
        return new Date(formatStringDate(Constants.SCREEN_DATE_PATTERN, data).getTime());
    }

    public static Date stringToTime(String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_PATTERN, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE));
        return new Date(sdf.parse(hora).getTime());
    }

    public static Date formatHour(String pattern, String hora) throws ParseException {
        return new Date(new SimpleDateFormat(pattern).parse(hora).getTime());
    }

    public static Date formatStringDate(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static String formatDate(String pattern, Date date) throws ParseException {
        return new SimpleDateFormat(pattern, Locale.US).format(date);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (StatusBarNotification sbn : notificacao.getActiveNotifications()) {
                if (sbn.getId() == idNotificacao) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void cancelarTodasNotificacoes(Context c) {
        NotificationManager notificacao = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();
        }
    }

    public static void cancelarNotificacao(Context c, int idReuniao) {
        if (!isNotificacaoAtiva(c, idReuniao)) {
            return;
        }
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(idReuniao);
    }

    public static void cancelarNotificacao(Context c, int[] idReuniao) {
        for (int id : idReuniao) {
            cancelarNotificacao(c, id);
        }
    }

    public static void esconderTeclado(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isEmptyOrNull(String value) {
        return value == null || "".equals(value.trim());
    }

    public static void mostrarNotificacao(Context context, int icon, String title, String content, PendingIntent pendingIntent, int idNotificacao, boolean isNotificacaoFixa) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(icon);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(content);
        notificationBuilder.setOngoing(isNotificacaoFixa);
        if (!isNotificacaoAtiva(context, idNotificacao)) {
            long[] v = {1000, 2000};
            notificationBuilder.setVibrate(v);
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        }
        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(idNotificacao, notificationBuilder.build());
    }

    public static void listarReunioes(Context context) {
//        try {
//            ReuniaoDAO datasource = new ReuniaoDAO(context);
//            List<ReuniaoVO> reunioes = datasource.getReunioes();
//            List<ReuniaoVO> reunioesFiltradas = new ArrayList<>();
//
//            for (ReuniaoVO vo : reunioes) {
//                DateTime dtInicio = new DateTime(stringToDateTime(vo.getDtInicio()));
//                DateTime dtTermino = new DateTime(stringToDateTime(vo.getDtTermino()));
//                DateTime dtAgora = new DateTime(new Date());
//                if (dtInicio.withTimeAtStartOfDay().isEqual(dtAgora.withTimeAtStartOfDay()) && dtAgora.isBefore(dtTermino)) {
//                    reunioesFiltradas.add(vo);
//                }
//            }
//
//            ReunioesHojeAdapter adapter = new ReunioesHojeAdapter(reunioesFiltradas, (Activity) context);
//
//            ListView lvReunioes = (ListView) ((Activity) context).findViewById(R.id.lvReunioes);
//            lvReunioes.setAdapter(adapter);
//
//            if (lvReunioes.getAdapter().getCount() <= 0) {
//                GlobalClass gc = (GlobalClass) context.getApplicationContext();
//                gc.setReuniaoAcontecera(false);
//                cancelarTodasNotificacoes(context);
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    public static Long stringToLong(String value) {
        if (isEmptyOrNull(value)) {
            value = "0";
        }
        return Long.valueOf(value);
    }

    public static String longToString(Long value) {
        if (value == null) {
            value = 0L;
        }
        return String.valueOf(value);
    }

    public static Double stringToDouble(String value) {
        if (isEmptyOrNull(value)) {
            value = "0";
        }
        return Double.parseDouble(value);
    }

    public static void popularBancoLocal(Context c, List<ReuniaoVO> lstReunioes) throws ParseException {
        DAOHelper<ReuniaoVO> reuniaoDAO = new DAOHelper<>();
        reuniaoDAO.deleteAll();
        for (ReuniaoVO vo : lstReunioes) {
            reuniaoDAO.insert(vo);
        }
    }

    public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }


    public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    public static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (Exception ex) {
            Toast.makeText(context, "Erro ao verificar se estava online! (" + ex.getMessage() + ")", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality();
                    }
                } catch (IOException e) {
                    Log.e("ERRO", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static Location getCurrentLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        return location;
    }

    public static boolean checkCurrentActivity(Context context, Class clazz){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getShortClassName().endsWith(clazz.getSimpleName());
    }

}
