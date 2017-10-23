package br.com.everis.notificacaobeacon.service.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wgoncalv on 22/10/2017.
 */

public interface UsuarioAPI {

    @Headers({"Content-Type: text/plain; charset=utf-8"})
    @GET(APIUrls.GET_BUSCAR_DADOS_USUARIOS)
    Call<JsonArray> buscarDadosUsuario(@Query("idUsuario") long idUsuario);

    @PUT(APIUrls.PUT_GRAVAR_USUARIO + "/{idUsuario}/{idReuniao}")
    Call<Void> gravarUsuario(@Path("idUsuario") long idUsuario, @Path("idReuniao") long idReuniao, @Body JsonObject dados);

}
