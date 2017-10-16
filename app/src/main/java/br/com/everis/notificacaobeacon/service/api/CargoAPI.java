package br.com.everis.notificacaobeacon.service.api;

import java.util.List;

import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public interface CargoAPI {

    @GET(APIUrls.GET_CARGO_LISTAR)
    Call<List<CargoVO>> listarCargos();

}
