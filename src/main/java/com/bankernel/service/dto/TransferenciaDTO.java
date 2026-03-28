package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusTransferencia;
import com.bankernel.domain.enumeration.EnumTipoChaveTransferencia;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Transferencia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransferenciaDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valor;

    @Size(max = 100)
    private String chaveInterna;

    @NotNull
    private EnumTipoChaveTransferencia tipoChave;

    @NotNull
    private EnumStatusTransferencia situacao;

    @Size(max = 500)
    private String descricao;

    @Size(max = 500)
    private String motivoRejeicao;

    @NotNull
    @Size(max = 50)
    private String numeroReferencia;

    private TransacaoDTO transacao;

    @NotNull
    private UserDTO usuarioOrigem;

    private UserDTO usuarioDestino;

    @NotNull
    private CarteiraDTO carteiraOrigem;

    private CarteiraDTO carteiraDestino;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getChaveInterna() {
        return chaveInterna;
    }

    public void setChaveInterna(String chaveInterna) {
        this.chaveInterna = chaveInterna;
    }

    public EnumTipoChaveTransferencia getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(EnumTipoChaveTransferencia tipoChave) {
        this.tipoChave = tipoChave;
    }

    public EnumStatusTransferencia getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusTransferencia situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public TransacaoDTO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoDTO transacao) {
        this.transacao = transacao;
    }

    public UserDTO getUsuarioOrigem() {
        return usuarioOrigem;
    }

    public void setUsuarioOrigem(UserDTO usuarioOrigem) {
        this.usuarioOrigem = usuarioOrigem;
    }

    public UserDTO getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(UserDTO usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public CarteiraDTO getCarteiraOrigem() {
        return carteiraOrigem;
    }

    public void setCarteiraOrigem(CarteiraDTO carteiraOrigem) {
        this.carteiraOrigem = carteiraOrigem;
    }

    public CarteiraDTO getCarteiraDestino() {
        return carteiraDestino;
    }

    public void setCarteiraDestino(CarteiraDTO carteiraDestino) {
        this.carteiraDestino = carteiraDestino;
    }

    public MoedaCarteiraDTO getMoedaCarteira() {
        return moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteiraDTO moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransferenciaDTO)) {
            return false;
        }

        TransferenciaDTO transferenciaDTO = (TransferenciaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transferenciaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransferenciaDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", chaveInterna='" + getChaveInterna() + "'" +
            ", tipoChave='" + getTipoChave() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", motivoRejeicao='" + getMotivoRejeicao() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", transacao=" + getTransacao() +
            ", usuarioOrigem=" + getUsuarioOrigem() +
            ", usuarioDestino=" + getUsuarioDestino() +
            ", carteiraOrigem=" + getCarteiraOrigem() +
            ", carteiraDestino=" + getCarteiraDestino() +
            ", moedaCarteira=" + getMoedaCarteira() +
            "}";
    }
}
