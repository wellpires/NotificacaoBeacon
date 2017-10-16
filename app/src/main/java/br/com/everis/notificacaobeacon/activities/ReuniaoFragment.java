package br.com.everis.notificacaobeacon.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.PermissaoSpinnerAdapter;
import br.com.everis.notificacaobeacon.listener.PermissaoPresenterListener;
import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.service.IPermissaoService;
import br.com.everis.notificacaobeacon.service.IReuniaoService;
import br.com.everis.notificacaobeacon.service.api.PermissaoAPI;
import br.com.everis.notificacaobeacon.service.impl.PermissaoServiceImpl;
import br.com.everis.notificacaobeacon.service.impl.ReuniaoServiceImpl;
import br.com.everis.notificacaobeacon.utils.ReuniaoUtils;

public class ReuniaoFragment extends Fragment{

    private Spinner cbPermissao = null;

    private String mParam1;

    public ReuniaoFragment() {
    }

    public static ReuniaoFragment newInstance() {
        ReuniaoFragment fragment = new ReuniaoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reuniao, container, false);
        return view;
    }

}
