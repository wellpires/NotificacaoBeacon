package br.com.everis.notificacaobeacon.service.impl;

import android.content.Context;

import java.util.List;

import br.com.everis.notificacaobeacon.listener.PermissaoPresenterListener;
import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.service.IPermissaoService;
import br.com.everis.notificacaobeacon.service.api.PermissaoAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class PermissaoServiceImpl implements IPermissaoService {

    private final Context context;
    private final PermissaoAPI permissaoAPI;
    private final PermissaoPresenterListener reuniaoListener;

    public PermissaoServiceImpl(Context context, PermissaoPresenterListener reuniaoListener) {
        this.context = context;
        this.reuniaoListener = reuniaoListener;
        this.permissaoAPI = APIClient.getClient().create(PermissaoAPI.class);
    }

    @Override
    public void listarPermissoes() {

        Call<List<PermissaoVO>> call = permissaoAPI.listarPermissoes();
        call.enqueue(new Callback<List<PermissaoVO>>() {
            @Override
            public void onResponse(Call<List<PermissaoVO>> call, Response<List<PermissaoVO>> response) {
                reuniaoListener.permissaoReady(response.body());
            }

            @Override
            public void onFailure(Call<List<PermissaoVO>> call, Throwable t) {

            }
        });

    }
}
