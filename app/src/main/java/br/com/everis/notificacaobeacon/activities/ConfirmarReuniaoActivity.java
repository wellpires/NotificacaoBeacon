package br.com.everis.notificacaobeacon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.listener.UsuarioPresenterListener;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.model.UsuarioVO;
import br.com.everis.notificacaobeacon.service.IUsuarioService;
import br.com.everis.notificacaobeacon.service.impl.UsuarioServiceImpl;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class ConfirmarReuniaoActivity extends AppCompatActivity implements UsuarioPresenterListener, View.OnClickListener, Branch.BranchReferralInitListener {

    private TextView lblMensagem = null;
    private EditText txtUsuario = null;
    private EditText txtSenha = null;
    private Button btnConfirmar = null;
    private ProgressDialog barraProgresso = null;

    private IUsuarioService usuarioService = null;

    private UsuarioVO usuario = null;
    private ReuniaoVO reuniao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_reuniao);

        onNewIntent(getIntent());

        lblMensagem = (TextView) findViewById(R.id.lblMensagem);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        btnConfirmar.setOnClickListener(this);

        barraProgresso = new ProgressDialog(this);
        barraProgresso.setMessage(Constants.LABEL_AGUARDE);
        barraProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barraProgresso.setIndeterminate(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Branch init
        Branch.getInstance().initSession(this, this.getIntent().getData(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @Override
    public void usuarioReady(JsonArray dados) {

        if(dados.size() == 0){
            barraProgresso.dismiss();
            finish();
            return;
        }

        usuario = new Gson().fromJson(dados.get(0), UsuarioVO.class);
        reuniao = new Gson().fromJson(dados.get(1), ReuniaoVO.class);

        HashMap<String, String> hmValores = new HashMap<>();
        hmValores.put("nome", usuario.getNomeCompleto().split(" ")[0]);
        hmValores.put("assunto", reuniao.getAssunto());

        StrSubstitutor substitutor = new StrSubstitutor();
        String mensagemFinal = substitutor.replace(Constants.MENSAGEM_CONFIRMACAO, hmValores);

        lblMensagem.setText(mensagemFinal);

        txtUsuario.setVisibility(View.VISIBLE);
        txtSenha.setVisibility(View.VISIBLE);

        if (!ReuniaoUtils.isEmptyOrNull(usuario.getUsuario()) || !ReuniaoUtils.isEmptyOrNull(usuario.getSenha())) {
            txtUsuario.setVisibility(View.INVISIBLE);
            txtSenha.setVisibility(View.INVISIBLE);
        }

        barraProgresso.dismiss();
    }

    @Override
    public void usuarioReady() {
        barraProgresso.dismiss();
    }

    @Override
    public void usuarioFailed(RestException e) {
        barraProgresso.dismiss();
        ReuniaoUtils.mostrarAvisoDialogo(this, e.getMessage());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConfirmar) {
            try {
                barraProgresso.show();

                String senha = txtSenha.getText().toString();
                senha = ReuniaoUtils.generateStorngPasswordHash(senha);

                usuario.setUsuario(txtUsuario.getText().toString());
                usuario.setSenha(senha);

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("usuario", new JsonParser().parse(new Gson().toJson(usuario)));
                jsonObject.addProperty("confirmacao", Constants.CODIGO_CONFIRMADO);

                usuarioService.gravarUsuario(usuario.getIdUsuario(), reuniao.getIdReuniao(), jsonObject);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInitFinished(JSONObject referringParams, BranchError error) {
        barraProgresso.show();
        if (error == null) {
            try {
                String pIdUsuario = referringParams.getString("idUsuario");
                Long idUsuario = ReuniaoUtils.stringToLong(pIdUsuario);
                usuarioService = new UsuarioServiceImpl(this);
                usuarioService.buscarDadosUsuario(idUsuario);
                Log.i("BRANCH SDK", referringParams.getString("idUsuario"));
                Log.i("PASSOU AQUI PORRA", "TESTE PORRA");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("BRANCH SDK", error.getMessage());
        }
    }
}
