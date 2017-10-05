package br.com.everis.notificacaobeacon.utils;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public interface Constants {

    String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";
    String DATE_PATTERN = "dd/MM/yyyy";
    String TIME_PATTERN = "HH:mm";
    String DATETIME_CUSTOM_PATTERN = "dd/MM/yyyy 'ás' HH:mm";
    String TIMEZONE = "America/Sao_Paulo";
    String REUNIAO = "Reunião";
    String MENSAGEM_REUNIAO = "Sua reunião começará em ";
    String BEM_VINDO = "Bem vindo á Everis!";
    String CONTINUACAO_BEM_VINDO = "Toque para ver detalhes da reunião.";
    String SEM_REUNIAO_ATUAL = "Não há reunião no momento";

    String LABEL_OK = "OK";
    String LABEL_SIM = "Sim";

    boolean NOTIFICACAO_FIXA = true;

    String LABEL_NAO = "Não";
    String TITULO_APP = "Controle de Reunião";

    String TEMPO_RESTANTE_KEY = "TEMPO_RESTANTE";
    String ID_REUNIAO_KEY = "ID_REUNIAO";
    String NOVA_REUNIAO_KEY = "NOVO_REGISTRO";
    String LOCAL_KEY = "LOCAL";
    String MENSAGEM_KEY = "MENSAGEM";

    int ID_BEM_VINDO_REUNIAO = 0;
    int ID_NOTIFICACAO_REUNIAO = 1;
    int ID_NOTIFICACAO_REUNIAO_ACONTECENDO = 2;
    int ID_NOTIFICACAO_REUNIAO_FINALIZOU = 3;


    String ERRO_ASSUNTO_REUNIAO = "Forneça o Assunto da reunião";
    String ERRO_DATA_INICIO_REUNIAO = "Forneça a Data de Inicio da reunião";
    String ERRO_HORA_INICIO_REUNIAO = "Forneça a Hora de Inicio da reunião";
    String ERRO_DATA_TERMINO_REUNIAO = "Forneça a Data de Término da reunião";
    String ERRO_HORA_TERMINO_REUNIAO = "Forneça a Hora de Término da reunião";
    String ERRO_LOCAL_REUNIAO_REUNIAO = "Forneça o Local da reunião";
    String ERRO_DESCRICAO_REUNIAO = "Forneça a Descriçao da reunião";
    String ERRO_DATA_HORA_INICIO_TERMINO_DIFERENTES = "A data e o horário de inicio e término devem ser diferentes";
    String ERRO_DATA_INICIO_MENOR_TERMINO = "A data de inicio deve ser menor que a data de término";
    String LABEL_VOCE_TEM_CERTEZA = "Você tem certeza?";
    String TITULO_NOVA_REUNIAO = "Nova Reunião";
    String TITULO_EDITAR_REUNIAO = "Editar Reunião";

    String VOCE_ESTA_AQUI = "Você está aqui!";
    String ERRO_PESQUISA_ENDERECO = "Favor pesquise um endereço!";
    String LABEL_NENHUMA_REUNIAO = "Nenhuma reunião";

    String UUID_BEACON = "813c5c55-3a33-4c52-bc86-22cb7d49fc5c";
    String MINOR_BEACON = "258";
    String MAJOR_BEACON = "852";
    String LAYOUT_BEACON = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    String FLAG_NOVA_REUNIAO = "N";
    String FLAG_ALTERAR_REUNIAO = "A";
    String FLAG_DETALHES_REUNIAO = "D";
    String TITULO_DETALHES_REUNIAO = "Detalhes da Reunião";
    String REUNIAO_ACONTECENDO = "Reunião acontecendo agora";
    String REUNIAO_TERMINARA = "Sua reunião terminará em ";
    String MINUTOS_LABEL = " minuto";
    String HORAS_LABEL = " hora";
    String REUNIÃO_FINALIZADA = "A reunião finalizou";
    String VOLTE_SEMPRE = "Volte sempre!";

    int TEMPO_LIMITE_MINUTOS = 120;
}
