package br.com.everis.notificacaobeacon.utils;

/**
 * Created by wgoncalv on 10/10/2017.
 */

public interface APIUrls {

    String BASE_URL = "http://192.168.0.25:8080/ControleReunioesWS/";

    String POST_GRAVAR_REUNIAO = "gravarReuniao";
    String PUT_EDITAR_REUNIAO = "editarReuniao";
    String DELETE_REMOVER_REUNIAO = "removerReuniao";
    String GET_BUSCAR_REUNIAO = "buscarReuniao";
    String GET_BUSCAR_REUNIOES = "buscarReunioes";
    String GET_LISTAR_REUNIOES = "listarReunioes";

}
