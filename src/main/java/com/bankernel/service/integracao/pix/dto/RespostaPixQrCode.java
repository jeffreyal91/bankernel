package com.bankernel.service.integracao.pix.dto;

import java.time.Instant;

/**
 * Resposta da criação de um QR Code PIX (estático ou dinâmico).
 */
public record RespostaPixQrCode(
    /** Identificador da transação PIX gerado pelo provider. */
    String txId,

    /** URL de localização da cobrança (padrão BACEN). */
    String location,

    /** Payload do QR Code (string "pix copia e cola"). */
    String pixCopiaECola,

    /** Imagem do QR Code em Base64 (opcional, depende do provider). */
    String qrCodeBase64,

    /** Data/hora de criação da cobrança. */
    Instant criadoEm,

    /** Data/hora de expiração (null para QR estático). */
    Instant expiraEm,

    /** Status da cobrança no provider. */
    String status
) {}
