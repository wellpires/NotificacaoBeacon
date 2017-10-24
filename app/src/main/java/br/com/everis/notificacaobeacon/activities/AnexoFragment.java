package br.com.everis.notificacaobeacon.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.everis.notificacaobeacon.R;
import br.com.everis.notificacaobeacon.adapter.AnexosAdapter;
import br.com.everis.notificacaobeacon.bd.DAOHelper;
import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.listener.ReuniaoPresenterListener;
import br.com.everis.notificacaobeacon.model.ArquivoVO;
import br.com.everis.notificacaobeacon.model.CargoVO;
import br.com.everis.notificacaobeacon.model.PermissaoVO;
import br.com.everis.notificacaobeacon.model.ReuniaoArquivoUsuarioVO;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.model.UsuarioVO;
import br.com.everis.notificacaobeacon.service.IReuniaoService;
import br.com.everis.notificacaobeacon.service.impl.ReuniaoServiceImpl;
import br.com.everis.notificacaobeacon.utils.Constants;
import br.com.everis.notificacaobeacon.utils.FirebaseUtils;

public class AnexoFragment extends Fragment implements DialogSelectionListener, AdapterView.OnItemLongClickListener, View.OnClickListener, OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener {
    private static final int CAMERA_REQUEST = 1888;

    private View view = null;
    private Context context = null;

    private FloatingActionButton fabAddAnexo = null;
    private ListView lvAnexos = null;
    private ProgressDialog barraProgresso = null;

    private AnexosAdapter anexosAdapter = null;
    private DAOHelper<ArquivoVO> arquivoDAO = null;
    private HashMap<String, Boolean> hmArquivosEnviados = null;
    private HashMap<String, String> hmCaminhoArquivos = null;
    private IReuniaoService reuniaoService = null;

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


        anexosAdapter = new AnexosAdapter(getActivity());
        lvAnexos.setAdapter(anexosAdapter);
        lvAnexos.setOnItemLongClickListener(this);

        arquivoDAO = new DAOHelper<>();

        List<ArquivoVO> all = arquivoDAO.findAll(ArquivoVO.class);
        anexosAdapter.addAll(all);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.btnSalvar);
        item.setTitle(Constants.LABEL_FINALIZAR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnSalvar) {
            hmArquivosEnviados = new HashMap<>();
            hmCaminhoArquivos = new HashMap<>();
            FirebaseUtils fsUtils = new FirebaseUtils();
            try {
                if (anexosAdapter.getCount() == 0) {
                    Toast.makeText(context, Constants.ERRO_FALTA_ANEXO, Toast.LENGTH_LONG).show();
                    return false;
                }

                barraProgresso = new ProgressDialog(context);
                barraProgresso.setMessage("Aguarde!");
                barraProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                barraProgresso.setIndeterminate(true);
                barraProgresso.show();

                arquivoDAO.deleteAll(ArquivoVO.class);

                for (int i = 0; i < anexosAdapter.getCount(); i++) {
                    ArquivoVO vo = anexosAdapter.getItem(i);
                    File arquivoEnviado = fsUtils.uploadFile(new File(vo.getCaminhoArquivo()), AnexoFragment.this, AnexoFragment.this);
                    hmArquivosEnviados.put(arquivoEnviado.getName(), false);
                    hmCaminhoArquivos.put(arquivoEnviado.getName(), arquivoEnviado.getAbsolutePath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                anexosAdapter.addItem(new ArquivoVO(0L, caminhoImagem));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSelectedFilePaths(String[] files) {
        for (String file : files) {
            anexosAdapter.addItem(new ArquivoVO(0L, file));
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

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        try {
            ArquivoVO vo = new ArquivoVO();
            vo.setIdArquivo(arquivoDAO.getNextId(ArquivoVO.class));
            vo.setArquivo(String.valueOf(taskSnapshot.getDownloadUrl()));
            for (Map.Entry<String, String> entry : hmCaminhoArquivos.entrySet()) {
                if (entry.getKey().equals(taskSnapshot.getStorage().getName())) {
                    vo.setCaminhoArquivo(entry.getValue());
                    break;
                }
            }
            arquivoDAO.insert(vo);
            for (Map.Entry<String, Boolean> entry : hmArquivosEnviados.entrySet()) {
                if (entry.getKey().equals(taskSnapshot.getStorage().getName())) {
                    entry.setValue(true);
                }
            }

            for (Map.Entry<String, Boolean> entry : hmArquivosEnviados.entrySet()) {
                if (!entry.getValue()) {
                    return;
                }
            }

            final DAOHelper<ReuniaoVO> reuniaoDAO = new DAOHelper<>();
            DAOHelper<UsuarioVO> usuarioDAO = new DAOHelper<>();

            ReuniaoVO reuniao = reuniaoDAO.find(ReuniaoVO.class);
            List<UsuarioVO> usuarios = usuarioDAO.findAll(UsuarioVO.class);
            List<ArquivoVO> arquivos = arquivoDAO.findAll(ArquivoVO.class);


            usuarios = usuarioDAO.detachFromRealm(usuarios);
            arquivos = arquivoDAO.detachFromRealm(arquivos);
            reuniao = reuniaoDAO.detachFromRealm(reuniao);

            for (UsuarioVO usuario : usuarios) {
                usuario.setPermissao(new PermissaoVO(usuario.getPermissaoFK(), ""));
                usuario.setCargo(new CargoVO(usuario.getCargoFK(), ""));
            }

            ReuniaoArquivoUsuarioVO rau = new ReuniaoArquivoUsuarioVO();
            rau.setListUsuarios(usuarios);
            rau.setListArquivos(arquivos);
            rau.setReuniao(reuniao);

            for (UsuarioVO usuarioVO : rau.getListUsuarios()) {
                usuarioVO.setIdUsuario(0L);
            }

            for (ArquivoVO arquivoVO : rau.getListArquivos()) {
                arquivoVO.setIdArquivo(0L);
            }

            rau.getReuniao().setIdReuniao(0);

            reuniaoService = new ReuniaoServiceImpl(context, new ReuniaoPresenterListener() {
                @Override
                public void reunioesReady(List<ReuniaoVO> lstReunioes) {
                }

                @Override
                public void reuniaoReady(ReuniaoVO reuniaoVO) {

                }

                @Override
                public void reuniaoReady(JsonArray usuarios) {
                    if(usuarios.size() == 0){
                        barraProgresso.dismiss();
                        return;
                    }

                    List<UsuarioVO> lstUsuarios = new Gson().fromJson(usuarios, new TypeToken<List<UsuarioVO>>(){}.getType());

                    ReuniaoVO reuniao = reuniaoDAO.find(ReuniaoVO.class);
                    reuniao = reuniaoDAO.detachFromRealm(reuniao);
                    EnviarEmail emailActivity = new EnviarEmail();
                    emailActivity.enviarEmail(context, lstUsuarios, reuniao);

                    //TODO TERMINAR ESSA PARTE. ENVIAR EMAIL PARA OS PARTICIPANTES COM O Constants.TEMPLATE_DEEP_LINK

                    arquivoDAO.deleteAll();
                    barraProgresso.dismiss();
                    Intent i = new Intent(context, ReuniaoMarcadaActivity.class);
                    startActivity(i);
                }

                @Override
                public void reuniaoReady() {
                }

                @Override
                public void reuniaoFailed(RestException exception) {
                    barraProgresso.dismiss();
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            reuniaoService.gravarReuniao(rau);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        barraProgresso.dismiss();
    }

}
