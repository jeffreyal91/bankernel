package com.bankernel.service.integracao.notificacao;

/**
 * Interface para envio de notificações multi-canal.
 *
 * <p>Abstrai os diferentes canais de comunicação (email, push, SMS).
 * Cada canal pode ser implementado com um provider diferente.</p>
 *
 * <h3>Responsabilidades da implementação:</h3>
 * <ul>
 *   <li>Conexão com o serviço de envio (SMTP, FCM, gateway SMS)</li>
 *   <li>Formatação das mensagens conforme o canal</li>
 *   <li>Tratamento de erros e retry</li>
 *   <li>Rate limiting conforme provider</li>
 * </ul>
 */
public interface ServicoNotificacao {

    /**
     * Envia notificação por email.
     *
     * @param destinatario endereço de email do destinatário
     * @param assunto assunto do email
     * @param corpoHtml corpo do email em HTML
     * @throws NotificacaoException se falhar o envio
     */
    void enviarEmail(String destinatario, String assunto, String corpoHtml);

    /**
     * Envia notificação push para dispositivo móvel.
     *
     * @param tokenDispositivo token do dispositivo (FCM, APNs, etc.)
     * @param titulo título da notificação
     * @param mensagem corpo da mensagem
     * @throws NotificacaoException se falhar o envio
     */
    void enviarPush(String tokenDispositivo, String titulo, String mensagem);

    /**
     * Envia SMS para número de telefone.
     *
     * @param telefone número completo com código do país (ex: "+5511999999999")
     * @param mensagem texto do SMS (até 160 caracteres para single SMS)
     * @throws NotificacaoException se falhar o envio
     */
    void enviarSms(String telefone, String mensagem);
}
