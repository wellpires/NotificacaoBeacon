package br.com.everis.notificacaobeacon.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.AnexosAdapter;
import br.com.everis.notificacaobeacon.model.ArquivoVO;
import br.com.everis.notificacaobeacon.utils.Constants;

public class AnexoFragment extends Fragment implements DialogSelectionListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    private static final int DOCS_REQUEST = 1999;

    private View view = null;
    private Context context = null;

    private FloatingActionButton fabAddAnexo = null;
    private ListView lvAnexos = null;

    private AnexosAdapter adapter = null;

    public AnexoFragment() {
    }

    public static AnexoFragment newInstance(ActionBar supportActionBar) {
        AnexoFragment fragment = new AnexoFragment();
        supportActionBar.setTitle(Constants.TITULO_ANEXO);
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
        fabAddAnexo = (FloatingActionButton) view.findViewById(R.id.fabAddAnexo);
        fabAddAnexo.setOnClickListener(this);


        adapter = new AnexosAdapter(getActivity());
        lvAnexos.setAdapter(adapter);
        lvAnexos.setOnItemLongClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String caminhoImagem = null;
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        caminhoImagem = uri.toString();
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                adapter.addItem(new ArquivoVO(0L, caminhoImagem));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSelectedFilePaths(String[] files) {
        for (String file : files) {
            adapter.addItem(new ArquivoVO(0L, file));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView lblLabel = (TextView) view;
        lblLabel.getText();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.image_viewer, null);
        builder.setView(inflate);
        ImageView ivImagem = (ImageView) inflate.findViewById(R.id.ivVisuImagem);
        ivImagem.setImageURI(Uri.parse(lblLabel.getText().toString()));
        builder.setNegativeButton(Constants.LABEL_CANCELAR, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog d = builder.create();
        d.show();

        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabAddAnexo) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_adicionar_anexo, null);
            builder.setView(view);
            Button btnArquivos = (Button) view.findViewById(R.id.btnArquivos);
            Button btnCamera = (Button) view.findViewById(R.id.btnCamera);
            builder.setTitle(Constants.TITULO_ADICIONAR_ANEXO);
            builder.setNegativeButton(Constants.LABEL_CANCELAR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            final Dialog dialog = builder.create();
            btnArquivos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogProperties properties = new DialogProperties();
                    properties.selection_mode = DialogConfigs.MULTI_MODE;
                    properties.selection_type = DialogConfigs.FILE_SELECT;
                    properties.root = new File(DialogConfigs.DEFAULT_DIR);
                    properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
                    properties.extensions = null;

                    FilePickerDialog fileDialog = new FilePickerDialog(context, properties);
                    fileDialog.setNegativeBtnName(Constants.LABEL_CANCELAR);
                    fileDialog.setPositiveBtnName(Constants.LABEL_SELECIONAR);
                    fileDialog.setDialogSelectionListener(AnexoFragment.this);
                    fileDialog.show();

                    dialog.dismiss();
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

        }

    }
}
