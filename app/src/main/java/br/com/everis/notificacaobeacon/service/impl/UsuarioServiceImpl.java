package br.com.everis.notificacaobeacon.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.listener.UsuarioPresenterListener;
import br.com.everis.notificacaobeacon.service.IUsuarioService;
import br.com.everis.notificacaobeacon.service.api.UsuarioAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgoncalv on 22/10/2017.
 */

public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioAPI usuarioAPI;
    private final UsuarioPresenterListener usuarioListener;

    public UsuarioServiceImpl(UsuarioPresenterListener usuarioListener) {
        this.usuarioListener = usuarioListener;
        this.usuarioAPI = APIClient.getClient().create(UsuarioAPI.class);
    }

    @Override
    public void buscarDadosUsuario(long idUsuario) {
        Call<JsonArray> call = usuarioAPI.buscarDadosUsuario(idUsuario);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                usuarioListener.usuarioReady(response.body());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                usuarioListener.usuarioFailed(new RestException(t.getMessage(), t));
            }
        });
    }

    @Override
    public void gravarUsuario(long idUsuario, long idReuniao, JsonObject jsonObject) {

        Call<Void> call = usuarioAPI.gravarUsuario(idUsuario, idReuniao, jsonObject.getAsJsonObject());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                usuarioListener.usuarioReady();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                usuarioListener.usuarioFailed(new RestException(t.getMessage(), t));
            }
        });

    }
}
