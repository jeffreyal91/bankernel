package com.bankernel.service.integracao.boleto;

import com.bankernel.service.integracao.boleto.dto.*;

/**
 * Interface para integração com serviços de Boleto Bancário.
 *
 * <p>Cada provider de pagamentos deve fornecer uma implementação desta interface.
 * O kernel bancário não contém implementação específica de nenhum provider.</p>
 *
 * <h3>Responsabilidades da implementação:</h3>
 * <ul>
 *   <li>Autenticação com o provider</li>
 *   <li>Registro do boleto conforme especificações FEBRABAN</li>
 *   <li>Geração do código de barras e linha digitável</li>
 *   <li>Consulta de status e conciliação</li>
 *   <li>Pagamento de boletos de terceiros</li>
 * </ul>
 *
 * <h3>Fluxo de cobrança (emissão):</h3>
 * <ol>
 *   <li>Gerar boleto via {@link #gerarBoleto}</li>
 *   <li>Enviar boleto ao sacado (PDF, email ou link)</li>
 *   <li>Monitorar pagamento via {@link #consultarBoleto} ou webhook</li>
 * </ol>
 *
 * <h3>Fluxo de pagamento (saque):</h3>
 * <ol>
 *   <li>Validar boleto (código de barras / linha digitável)</li>
 *   <li>Efetuar pagamento via {@link #pagarBoleto}</li>
 *   <li>Consultar status via {@link #consultarPagamento}</li>
 * </ol>
 */
public interface ServicoBoletoIntegracao {

    /**
     * Gera um boleto bancário registrado para cobrança.
     *
     * <p>O boleto é registrado na CIP/FEBRABAN e pode ser pago em qualquer
     * banco ou via PIX (se habilitado o BolePIX).</p>
     *
     * @param requisicao dados completos do boleto (sacado, valores, datas, mensagens)
     * @return resposta contendo código de barras, linha digitável, nosso número e URL do PDF
     * @throws IntegracaoBoletoException se houver falha na comunicação ou validação
     */
    RespostaBoleto gerarBoleto(RequisicaoBoleto requisicao);

    /**
     * Consulta o status atual de um boleto.
     *
     * @param idBoleto identificador do boleto no provider
     * @return status atual e dados do pagamento (se pago)
     * @throws IntegracaoBoletoException se houver falha na comunicação
     */
    RespostaConsultaBoleto consultarBoleto(String idBoleto);

    /**
     * Cancela (baixa) um boleto antes do pagamento.
     *
     * <p>Boletos já pagos não podem ser cancelados. A baixa pode demorar
     * até 1 dia útil para ser processada na CIP.</p>
     *
     * @param idBoleto identificador do boleto no provider
     * @return true se o cancelamento foi aceito, false caso contrário
     * @throws IntegracaoBoletoException se houver falha na comunicação
     */
    boolean cancelarBoleto(String idBoleto);

    /**
     * Efetua pagamento de um boleto de terceiro.
     *
     * <p>Debita o valor da conta do pagador e efetua o pagamento do boleto.
     * O pagamento pode ser agendado ou imediato dependendo do horário.</p>
     *
     * @param requisicao dados do boleto (código de barras ou linha digitável) e valor
     * @return resposta contendo ID do pagamento, status e valor pago
     * @throws IntegracaoBoletoException se houver falha na comunicação ou validação
     */
    RespostaPagamentoBoleto pagarBoleto(RequisicaoPagamentoBoleto requisicao);

    /**
     * Consulta o status de um pagamento de boleto efetuado.
     *
     * @param idPagamento identificador do pagamento retornado por {@link #pagarBoleto}
     * @return status atual do pagamento
     * @throws IntegracaoBoletoException se houver falha na comunicação
     */
    RespostaConsultaPagamentoBoleto consultarPagamento(String idPagamento);
}
