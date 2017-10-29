package br.com.everis.notificacaobeacon.service.api;


import com.google.gson.JsonObject;

import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wgoncalv on 27/10/2017.
 */

public interface GoogleAPI {

    @GET(APIUrls.GET_BUSCAR_TEMPO_DISTANCIA)
    Call<JsonObject> buscarTempoDistancia(@Query("origins") String origin, @Query("destinations") String destination, @Query("key") String apiKEY);
}
