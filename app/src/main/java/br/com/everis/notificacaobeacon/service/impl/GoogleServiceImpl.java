package br.com.everis.notificacaobeacon.service.impl;

import com.google.gson.JsonObject;

import java.io.IOException;

import br.com.everis.notificacaobeacon.service.IGoogleService;
import br.com.everis.notificacaobeacon.service.api.GoogleAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;
import retrofit2.Call;

/**
 * Created by wgoncalv on 27/10/2017.
 */

public class GoogleServiceImpl implements IGoogleService {

    private final GoogleAPI googleAPI;

    public GoogleServiceImpl() {
        this.googleAPI = APIClient.getGoogleClient().create(GoogleAPI.class);;
    }

    @Override
    public Integer buscarTempoDistancia(String origins, String destinations) throws IOException {
        Call<JsonObject> call = googleAPI.buscarTempoDistancia(origins, destinations, Constants.API_KEY);
        JsonObject results = call.execute().body();
        String strResult = results.get("rows").getAsJsonArray().get(0).getAsJsonObject().get("elements").getAsJsonArray().get(0).getAsJsonObject().get("duration").getAsJsonObject().get("value").toString();

        Long tempoDistanciaSegundos = ReuniaoUtils.stringToLong(strResult);
        Double tempoDistanciaMinutos = Math.ceil(tempoDistanciaSegundos / 60.0);

        return tempoDistanciaMinutos.intValue();
    }
}
