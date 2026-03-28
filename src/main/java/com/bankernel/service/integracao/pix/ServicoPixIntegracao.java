package com.bankernel.service.integracao.pix;

import com.bankernel.service.integracao.pix.dto.*;
import java.math.BigDecimal;

/**
 * Interface para integração com serviços PIX.
 *
 * <p>Cada provider de pagamentos (instituição financeira ou PSP) deve fornecer
 * uma implementação desta interface. O kernel bancário não contém implementação
 * específica de nenhum provider.</p>
 *
 * <h3>Responsabilidades da implementação:</h3>
 * <ul>
 *   <li>Autenticação com o provider (OAuth2, mTLS, API Key, etc.)</li>
 *   <li>Serialização/deserialização conforme API do provider</li>
 *   <li>Tratamento de erros e retry</li>
 *   <li>Renovação de tokens quando necessário</li>
 * </ul>
 *
 * <h3>Fluxo de cobrança (recebimento):</h3>
 * <ol>
 *   <li>Criar QR Code via {@link #criarPixEstatico}, {@link #criarPixDinamicoImediato}
 *       ou {@link #criarPixDinamicoVencimento}</li>
 *   <li>Exibir QR Code ao pagador</li>
 *   <li>Receber notificação de pagamento via webhook</li>
 *   <li>Confirmar pagamento via {@link #consultarCobranca}</li>
 * </ol>
 *
 * <h3>Fluxo de pagamento (envio):</h3>
 * <ol>
 *   <li>Enviar PIX via {@link #enviarPixPorChave} ou {@link #enviarPixManual}</li>
 *   <li>Acompanhar status do pagamento</li>
 * </ol>
 *
 * @see <a href="https://www.bcb.gov.br/estabilidadefinanceira/pix">BACEN - PIX</a>
 */
public interface ServicoPixIntegracao {

    /**
     * Cria um QR Code PIX estático para recebimento.
     *
     * <p>QR estático pode ser reutilizado para múltiplos pagamentos.
     * Ideal para pontos de venda fixos ou doações.</p>
     *
     * @param requisicao dados para criação do QR
     * @return resposta contendo QR Code, txId e pix copia e cola
     * @throws IntegracaoPixException se houver falha na comunicação com o provider
     */
    RespostaPixQrCode criarPixEstatico(RequisicaoPixEstatico requisicao);

    /**
     * Cria um QR Code PIX dinâmico (cobrança imediata).
     *
     * <p>QR dinâmico é de uso único com valor definido e tempo de expiração.
     * Ideal para cobranças pontuais com valor específico.</p>
     *
     * @param requisicao dados incluindo valor, expiração e info do devedor
     * @return resposta contendo QR Code, txId, location e pix copia e cola
     * @throws IntegracaoPixException se houver falha na comunicação
     */
    RespostaPixQrCode criarPixDinamicoImediato(RequisicaoPixDinamico requisicao);

    /**
     * Cria um QR Code PIX dinâmico com vencimento.
     *
     * <p>Suporta juros, multa, desconto e abatimento conforme regulamentação BACEN.
     * Ideal para cobranças com prazo e penalidades.</p>
     *
     * @param requisicao dados incluindo vencimento, juros, multa, desconto
     * @return resposta contendo QR Code, txId e pix copia e cola
     * @throws IntegracaoPixException se houver falha na comunicação
     */
    RespostaPixQrCode criarPixDinamicoVencimento(RequisicaoPixDinamicoVencimento requisicao);

    /**
     * Consulta o status de uma cobrança PIX pelo txId.
     *
     * <p>Retorna o status atual da cobrança e, se pago, os dados do pagamento
     * incluindo endToEndId, valor pago, data e dados do pagador.</p>
     *
     * @param txId identificador da transação PIX
     * @return status atual e dados do pagamento (se concluído)
     * @throws IntegracaoPixException se houver falha na comunicação
     */
    RespostaConsultaPix consultarCobranca(String txId);

    /**
     * Envia um pagamento PIX por chave.
     *
     * <p>Tipos de chave suportados: CPF, CNPJ, email, telefone (+55...) ou
     * chave aleatória (EVP). O SPI resolve automaticamente os dados bancários
     * do destinatário a partir da chave.</p>
     *
     * @param requisicao dados incluindo chave destino, tipo, valor e descrição
     * @return resposta contendo endToEndId, status e dados do recebedor
     * @throws IntegracaoPixException se houver falha na comunicação
     */
    RespostaEnvioPix enviarPixPorChave(RequisicaoEnvioPixChave requisicao);

    /**
     * Envia um pagamento PIX manual (dados bancários completos).
     *
     * <p>Usado quando o destinatário não possui chave PIX cadastrada ou
     * quando se deseja enviar diretamente para uma conta específica.</p>
     *
     * @param requisicao dados bancários completos do destinatário
     * @return resposta contendo endToEndId e status
     * @throws IntegracaoPixException se houver falha na comunicação
     */
    RespostaEnvioPix enviarPixManual(RequisicaoEnvioPixManual requisicao);

    /**
     * Solicita devolução de um PIX recebido (total ou parcial).
     *
     * <p>Conforme regulamentação BACEN, a devolução pode ser solicitada em até
     * 90 dias após o recebimento. Motivos aceitos: fraude, erro operacional
     * ou solicitação do recebedor.</p>
     *
     * @param endToEndId end-to-end ID do PIX original recebido
     * @param valor valor a devolver (pode ser menor que o valor original para devolução parcial)
     * @param motivo código do motivo conforme BACEN (ex: "FRAUDE", "ERRO", "SOLICITACAO")
     * @return resposta contendo ID da devolução e status
     * @throws IntegracaoPixException se houver falha na comunicação
     */
    RespostaDevolucaoPix solicitarDevolucao(String endToEndId, BigDecimal valor, String motivo);

    /**
     * Configura webhook para receber notificações de PIX recebidos.
     *
     * <p>O webhook será chamado pelo provider sempre que um PIX for recebido
     * na chave configurada. A implementação deve registrar a URL no provider.</p>
     *
     * @param chave chave PIX para monitorar recebimentos
     * @param webhookUrl URL de callback que receberá as notificações (HTTPS obrigatório)
     * @throws IntegracaoPixException se houver falha na configuração
     */
    void configurarWebhook(String chave, String webhookUrl);
}
