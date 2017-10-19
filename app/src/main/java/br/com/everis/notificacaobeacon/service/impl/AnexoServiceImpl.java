package br.com.everis.notificacaobeacon.service.impl;

import br.com.everis.notificacaobeacon.listener.AnexoPresenterListener;
import br.com.everis.notificacaobeacon.model.ArquivoVO;
import br.com.everis.notificacaobeacon.service.IAnexoService;
import br.com.everis.notificacaobeacon.service.api.AnexoAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgoncalv on 19/10/2017.
 */

public class AnexoServiceImpl implements IAnexoService {

    private final AnexoAPI anexoAPI;
    private final AnexoPresenterListener anexoListener;

    public AnexoServiceImpl(AnexoPresenterListener anexoListener) {
        this.anexoListener = anexoListener;
        this.anexoAPI = APIClient.getClient().create(AnexoAPI.class);
    }

    @Override
    public void gravarArquivos(ArquivoVO anexoVO) {
        Call<Void> call = anexoAPI.gravarArquivos(anexoVO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                anexoListener.anexoReady();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
