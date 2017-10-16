package br.com.everis.notificacaobeacon.service.api;

import java.util.List;

import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public interface PermissaoAPI {

    @GET(APIUrls.GET_PERMISSAO_LISTAR)
    Call<List<PermissaoVO>> listarPermissoes();

}
