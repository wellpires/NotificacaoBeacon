package br.com.everis.notificacaobeacon.service.api;

import br.com.everis.notificacaobeacon.model.ArquivoVO;
import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wgoncalv on 19/10/2017.
 */

public interface AnexoAPI {

    @POST(APIUrls.POST_GRAVAR_ARQUIVOS)
    Call<Void> gravarArquivos(@Body ArquivoVO arquivoVO);

}
