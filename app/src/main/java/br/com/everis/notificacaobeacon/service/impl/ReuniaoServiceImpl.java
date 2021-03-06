package br.com.everis.notificacaobeacon.service.impl;

import android.content.Context;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.model.ReuniaoArquivoUsuarioVO;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.service.IReuniaoService;
import br.com.everis.notificacaobeacon.service.api.ReuniaoAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import br.com.everis.notificacaobeacon.utils.Constants;
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
    public void gravarReuniao(ReuniaoArquivoUsuarioVO reuniao) throws IOException {
        Call<JsonArray> call = reuniaoAPI.criarReuniao(reuniao);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                reuniaoListener.reuniaoReady(response.body());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                reuniaoListener.reuniaoFailed(new RestException(t.getMessage()));
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
                reuniaoListener.reuniaoFailed(new RestException(t.getMessage(), t));

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
    public void buscarReunioes(ReuniaoVO reuniao) throws ParseException {
        Call<List<ReuniaoVO>> call = reuniaoAPI.buscarReunioes(ReuniaoUtils.formatDate(Constants.DATETIME_PATTERN,reuniao.getDtInicio()));
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
                reuniaoListener.reuniaoFailed(new RestException(t.getMessage(), t));
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
