package br.com.everis.notificacaobeacon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.model.UsuarioVO;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.Email;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

/**
 * Created by wgoncalv on 24/10/2017.
 */

public class EnviarEmail {


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().size() == 0) {
                return;
            }
            String mensagem = msg.getData().get("Mensagem").toString();
        }

    };


    public void enviarEmail(final Context context, final List<UsuarioVO> listUsuarios, final ReuniaoVO reuniao) {

        if (ReuniaoUtils.isOnline(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StrSubstitutor substitutor = new StrSubstitutor();
                    int contador = 0;
                    for (UsuarioVO usuarioVO : listUsuarios) {
                        Email m = new Email();
                        HashMap<String, String> hmValores = new HashMap<>();
                        try {
                            hmValores.put("nome", usuarioVO.getNomeCompleto());
                            hmValores.put("dataFormatada", ReuniaoUtils.formatDate(Constants.DATETIME_CUSTOM_PATTERN, ReuniaoUtils.stringToDateTime(reuniao.getDtInicio())));
                            hmValores.put("assunto", reuniao.getAssunto());
                            hmValores.put("idUsuario", usuarioVO.getIdUsuario() + "");
                            String mensagemFinal = substitutor.replace(Constants.MENSAGEM_EMAIL_CONVITE, hmValores) + "\n\n\n\n" + substitutor.replace(Constants.TEMPLATE_DEEP_LINK, hmValores);
                            String[] toArr = {usuarioVO.getEmail()};
                            m.setTo(toArr);

                            m.setBody(mensagemFinal);

                            //m.addAttachment("pathDoAnexo");//anexo opcional
                            Bundle b = new Bundle();
                            b.putString("Mensagem", "Enviando " + ++contador + "º email ...");
                            Message message = new Message();
                            message.setData(b);
                            handler.sendMessage(message);

                            if (ReuniaoUtils.isOnline(context)) {
                                m.send();
                            }

                        } catch (RuntimeException | ParseException rex) {
                            rex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    handler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            Toast.makeText(context, "Não estava online para enviar e-mail!", Toast.LENGTH_SHORT).show();
        }
    }
}
