package br.com.everis.notificacaobeacon.service.impl;

import android.content.Context;

import java.util.List;

import br.com.everis.notificacaobeacon.listener.CargoPresenterListener;
import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.service.ICargoService;
import br.com.everis.notificacaobeacon.service.api.CargoAPI;
import br.com.everis.notificacaobeacon.utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class CargoServiceImpl implements ICargoService {

    private final Context context;
    private final CargoAPI cargoAPI;
    private final CargoPresenterListener cargoListener;

    public CargoServiceImpl(Context context, CargoPresenterListener cargoListener) {
        this.context = context;
        this.cargoListener = cargoListener;
        this.cargoAPI = APIClient.getClient().create(CargoAPI.class);
    }

    @Override
    public void listarCargos() {

        Call<List<CargoVO>> call = cargoAPI.listarCargos();
        call.enqueue(new Callback<List<CargoVO>>() {
            @Override
            public void onResponse(Call<List<CargoVO>> call, Response<List<CargoVO>> response) {
                cargoListener.cargosReady(response.body());
            }

            @Override
            public void onFailure(Call<List<CargoVO>> call, Throwable t) {

            }
        });
    }
}
