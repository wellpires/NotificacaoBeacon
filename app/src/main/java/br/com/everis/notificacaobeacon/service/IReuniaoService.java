package br.com.everis.notificacaobeacon.service;

import java.io.IOException;

import br.com.everis.notificacaobeacon.model.ReuniaoArquivoUsuarioVO;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;

/**
 * Created by wgoncalv on 11/10/2017.
 */


public interface IReuniaoService{
    void gravarReuniao(ReuniaoArquivoUsuarioVO reuniao) throws IOException;
    void editarReuniao(ReuniaoVO reuniao) throws IOException;
    void removerReuniao(ReuniaoVO reuniao) throws IOException;
    void buscarReuniao(ReuniaoVO reuniao) throws Exception;
    void buscarReunioes(ReuniaoVO reuniao) throws Exception;
    void listarReunioes() throws Exception;
}
