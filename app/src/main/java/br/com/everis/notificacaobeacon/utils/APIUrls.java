package br.com.everis.notificacaobeacon.utils;

/**
 * Created by wgoncalv on 10/10/2017.
 */

public interface APIUrls {

    String BASE_URL = "http://192.168.0.25:8080/ControleReunioesWS/";
    String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/";

    //REUNIÃO
    String POST_GRAVAR_REUNIAO = "reuniao/gravarReuniao";
    String PUT_EDITAR_REUNIAO = "reuniao/editarReuniao";
    String DELETE_REMOVER_REUNIAO = "reuniao/removerReuniao";
    String GET_BUSCAR_REUNIAO = "reuniao/buscarReuniao";
    String GET_BUSCAR_REUNIOES = "reuniao/buscarReunioes";
    String GET_LISTAR_REUNIOES = "reuniao/listarReunioes";

    //PERMISSÃO
    String GET_PERMISSAO_LISTAR = "permissao/listarPermissoes";

    //CARGOS
    String GET_CARGO_LISTAR = "cargo/listarCargos";

    //USUARIOS
    String GET_BUSCAR_DADOS_USUARIOS = "usuario/buscarDadosUsuario";
    String PUT_GRAVAR_USUARIO = "usuario/gravarUsuario";

    //GOOGLE APIS
    String GET_BUSCAR_TEMPO_DISTANCIA = "distancematrix/json";

}
