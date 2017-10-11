package br.com.everis.notificacaobeacon.service.impl;

import java.io.IOException;

import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.service.IReuniaoService;
import br.com.everis.notificacaobeacon.service.api.ReuniaoAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgoncalv on 10/10/2017.
 */

public class ReuniaoServiceImpl implements IReuniaoService, Callback<ReuniaoVO> {

    private ReuniaoAPI reuniaoAPI = null;

    @Override
    public void gravar(ReuniaoVO reuniao) throws IOException {
        reuniaoAPI = APIClient.getClient().create(ReuniaoAPI.class);
        Call call = reuniaoAPI.criarReuniao(reuniao);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ReuniaoVO> call, Response<ReuniaoVO> response) {

    }

    @Override
    public void onFailure(Call<ReuniaoVO> call, Throwable t) {

    }
}
