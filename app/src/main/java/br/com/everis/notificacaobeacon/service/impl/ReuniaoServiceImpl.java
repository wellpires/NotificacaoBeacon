package br.com.everis.notificacaobeacon.service.impl;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.service.IReuniaoService;
import br.com.everis.notificacaobeacon.service.api.ReuniaoAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgoncalv on 10/10/2017.
 */

public class ReuniaoServiceImpl implements IReuniaoService {

    private final Context context;
    private final ReuniaoAPI reuniaoAPI;
    private final ReuniaoPresenterListener reuniaoListener;

    public ReuniaoServiceImpl(Context context, ReuniaoPresenterListener reuniaoListener) {
        this.context = context;
        this.reuniaoListener = reuniaoListener;
        this.reuniaoAPI = APIClient.getClient().create(ReuniaoAPI.class);
    }

    @Override
    public void gravarReuniao(ReuniaoVO reuniao) throws IOException {
        Call call = reuniaoAPI.criarReuniao(reuniao);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                reuniaoListener.reuniaoReady();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void editarReuniao(ReuniaoVO reuniao) throws IOException {
        Call call = reuniaoAPI.editarReuniao(reuniao.getIdReuniao(), reuniao);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                reuniaoListener.reuniaoReady();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void removerReuniao(ReuniaoVO reuniao) throws IOException {

        Call call = reuniaoAPI.removerReuniao(reuniao.getIdReuniao().toString());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                reuniaoListener.reuniaoReady();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    @Override
    public void buscarReuniao(ReuniaoVO reuniao) throws Exception {
        Call<ReuniaoVO> call = reuniaoAPI.buscarReuniao(reuniao.getIdReuniao().toString());
        call.enqueue(new Callback<ReuniaoVO>() {
            @Override
            public void onResponse(Call<ReuniaoVO> call, Response<ReuniaoVO> response) {
                ReuniaoVO reuniao = response.body();
                if (reuniao != null) {
                    reuniaoListener.reuniaoReady(reuniao);
                }
            }

            @Override
            public void onFailure(Call<ReuniaoVO> call, Throwable t) {

            }
        });
    }

    @Override
    public void buscarReunioes(ReuniaoVO reuniao) {
        String data = reuniao.getDtInicio();
        Call<List<ReuniaoVO>> call = reuniaoAPI.buscarReunioes(data);
        call.enqueue(new Callback<List<ReuniaoVO>>() {
            @Override
            public void onResponse(Call<List<ReuniaoVO>> call, Response<List<ReuniaoVO>> response) {
                List<ReuniaoVO> result = response.body();
                if (result != null) {
                    reuniaoListener.reunioesReady(result);
                }
            }

            @Override
            public void onFailure(Call<List<ReuniaoVO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void listarReunioes() throws Exception {
        Call<List<ReuniaoVO>> call = reuniaoAPI.listarReunioes();
        call.enqueue(new Callback<List<ReuniaoVO>>() {
            @Override
            public void onResponse(Call<List<ReuniaoVO>> call, Response<List<ReuniaoVO>> response) {
                List<ReuniaoVO> result = response.body();
                if (result != null) {
                    reuniaoListener.reunioesReady(result);
                }
            }

            @Override
            public void onFailure(Call<List<ReuniaoVO>> call, Throwable t) {

            }
        });
    }

}
