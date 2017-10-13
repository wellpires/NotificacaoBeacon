package br.com.everis.notificacaobeacon.service.api;

import java.util.List;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wgoncalv on 10/10/2017.
 */

public interface ReuniaoAPI {

    @POST(APIUrls.POST_GRAVAR_REUNIAO)
    Call<Void> criarReuniao(@Body ReuniaoVO reuniaoVO);

    @PUT(APIUrls.PUT_EDITAR_REUNIAO + "/{idReuniao}")
    Call<Void> editarReuniao(@Path("idReuniao") long idReuniao, @Body ReuniaoVO reuniaoVO);

    @Headers({"Content-Type: text/plain; charset=utf-8"})
    @DELETE(APIUrls.DELETE_REMOVER_REUNIAO + "/{idReuniao}")
    Call<Void> removerReuniao(@Path("idReuniao") String idReuniao);

    @Headers({"Content-Type: text/plain; charset=utf-8"})
    @GET(APIUrls.GET_BUSCAR_REUNIAO)
    Call<ReuniaoVO> buscarReuniao(@Query("idReuniao") String idReuniao);

    @Headers({"Content-Type: text/plain; charset=utf-8"})
    @GET(APIUrls.GET_BUSCAR_REUNIOES)
    Call<List<ReuniaoVO>> buscarReunioes(@Query("data") String data);

    @GET(APIUrls.GET_LISTAR_REUNIOES)
    Call<List<ReuniaoVO>> listarReunioes();
}
