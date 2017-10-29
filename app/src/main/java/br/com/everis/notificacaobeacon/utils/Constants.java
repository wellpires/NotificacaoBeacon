package br.com.everis.notificacaobeacon.utils;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public interface Constants {

    String DATETIME_PATTERN = "MMM dd, yyyy HH:mm";
    String DATE_PATTERN = "MMM dd, yyyy";
    String TIME_PATTERN = "HH:mm";
    String DATETIME_CUSTOM_PATTERN = "dd/MM/yyyy 'ás' HH:mm";
    String SCREEN_DATETIME_PATTERN = "dd/MM/yyyy HH:mm";
    String SCREEN_DATE_PATTERN = "dd/MM/yyyy";
    String SCREEN_TIME_PATTERN = "HH:mm";


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
    String COMANDO_KEY = "COMANDO_NOTIFICACAO";
    String LISTAR_NOTIFICATION_KEY = "LISTAR_NOTIFICACAO";

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
//    int TEMPO_LIMITE_MINUTOS = 60;
    String ACTION_SERVICE = "br.com.everis.notificacaobeacon.service.NOTIFICATION_LISTENER_SERVICE_EXAMPLE";
    String LABEL_ADICIONAR_PARTI = "Adicionar";
    String TITULO_PARTICIPANTE = "Participantes";
    String LABEL_CANCELAR = "Cancelar";
    String LABEL_SELECIONAR = "Selecionar";
    String TITULO_ADICIONAR_ANEXO = "Adicionar anexo";
    String TITULO_REUNIAO = "Reunião";
    String TITULO_ANEXO = "Anexos";
    String LABEL_SALVAR = "Salvar";
    String ERRO_FALTA_PARTICIPANTE = "Favor adicione ao menos um participante";
    String ERRO_FALTA_ANEXO = "Selecione ao menos um anexo";
    String LABEL_FINALIZAR = "Finalizar";
    String LABEL_PROXIMO = "Próximo";
    String LABEL_AGUARDE = "Aguarde!";
    String MENSAGEM_CONFIRMACAO = "Olá Sr(a) ${nome}.Você foi convidado para a reunião sobre ${assunto}." +
            " Favor cadastrar um usuário e senha para ter acesso ao aplicativo";
    int CODIGO_CONFIRMADO = 1;
    int CODIGO_NAO_CONFIRMADO = 2;

    String TEMPLATE_DEEP_LINK = "https://appreuniao.test-app.link/WaXVADL8sH?idUsuario=${idUsuario}";

    String MENSAGEM_EMAIL_CONVITE = "Olá Sr(a). ${nome}. Você foi convidado para a reunião sobre ${assunto}, que acontecerá dia ${dataFormatada}. Clique no link abaixo para baixar e ter acesso aos detalhes da sua reunião:";

    String MENSAGEM_DETALHE_REUNIAO = "Sua reunião é sobre ${assunto}, começa em ${minutos} minutos e tem previsão de término a partir das ${dataTermino}. Por favor, dirija-se à sala ${sala}.";

    String WAZE_DEEP_LINK = "https://waze.com/ul?ll=${latitude},${longitude}";
    String API_KEY = "AIzaSyBJMHiMFBv11gMo9cUJ-PKv5FksAn36YBc";

    int MINUTOS_ANTECEDENCIA = 20;
}
