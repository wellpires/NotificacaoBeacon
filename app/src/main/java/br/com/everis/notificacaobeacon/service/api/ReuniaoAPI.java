package br.com.everis.notificacaobeacon.service.api;

import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.utils.APIUrls;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wgoncalv on 10/10/2017.
 */

public interface ReuniaoAPI {

    @POST(APIUrls.POST_GRAVAR_REUNIAO)
    Call<Void> criarReuniao(@Body ReuniaoVO reuniaoVO);
}
