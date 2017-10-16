package br.com.everis.notificacaobeacon.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.AnexosAdapter;
import br.com.everis.notificacaobeacon.model.ArquivoVO;
import br.com.everis.notificacaobeacon.utils.Constants;

public class AnexoFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;

    private View view = null;
    private Context context = null;

    private ListView lvAnexos = null;

    private AnexosAdapter adapter = null;

    public AnexoFragment() {
    }

    public static AnexoFragment newInstance() {
        AnexoFragment fragment = new AnexoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        this.view = inflater.inflate(R.layout.fragment_anexo, container, false);
        this.context = view.getContext();
        lvAnexos = (ListView) view.findViewById(R.id.lvAnexos);
        adapter = new AnexosAdapter(getActivity());
        lvAnexos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.btnSalvar);
        item.setTitle(Constants.LABEL_ADICIONAR_PARTI);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_adicionar_anexo, null);
        builder.setView(view);
        Button btnArquivos = (Button) view.findViewById(R.id.btnArquivos);
        Button btnCamera = (Button) view.findViewById(R.id.btnCamera);
        builder.setTitle("Adicionar anexo");
        final Dialog dialog = builder.create();
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        btnArquivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
                dialog.dismiss();
            }
        });
        dialog.show();

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            String caminhoImagem = data.getDataString();
            adapter.addItem(new ArquivoVO(caminhoImagem));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
