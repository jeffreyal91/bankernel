package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusTransacao;
import com.bankernel.domain.enumeration.EnumTipoPagamento;
import com.bankernel.domain.enumeration.EnumTipoTransacao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Transacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valorEnviado;

    @NotNull
    private BigDecimal valorRecebido;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private Boolean estornada;

    @NotNull
    private EnumTipoTransacao tipoTransacao;

    private EnumTipoPagamento tipoPagamento;

    @NotNull
    private EnumStatusTransacao situacao;

    @NotNull
    private Boolean ativa;

    @Size(max = 50)
    private String tipoEntidadeOrigem;

    private Long idEntidadeOrigem;

    private CarteiraDTO carteiraOrigem;

    private CarteiraDTO carteiraDestino;

    private MoedaCarteiraDTO moedaOrigem;

    private MoedaCarteiraDTO moedaDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorEnviado() {
        return valorEnviado;
    }

    public void setValorEnviado(BigDecimal valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getEstornada() {
        return estornada;
    }

    public void setEstornada(Boolean estornada) {
        this.estornada = estornada;
    }

    public EnumTipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(EnumTipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public EnumTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(EnumTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public EnumStatusTransacao getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusTransacao situacao) {
        this.situacao = situacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public String getTipoEntidadeOrigem() {
        return tipoEntidadeOrigem;
    }

    public void setTipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public Long getIdEntidadeOrigem() {
        return idEntidadeOrigem;
    }

    public void setIdEntidadeOrigem(Long idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
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

    public MoedaCarteiraDTO getMoedaOrigem() {
        return moedaOrigem;
    }

    public void setMoedaOrigem(MoedaCarteiraDTO moedaOrigem) {
        this.moedaOrigem = moedaOrigem;
    }

    public MoedaCarteiraDTO getMoedaDestino() {
        return moedaDestino;
    }

    public void setMoedaDestino(MoedaCarteiraDTO moedaDestino) {
        this.moedaDestino = moedaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransacaoDTO)) {
            return false;
        }

        TransacaoDTO transacaoDTO = (TransacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransacaoDTO{" +
            "id=" + getId() +
            ", valorEnviado=" + getValorEnviado() +
            ", valorRecebido=" + getValorRecebido() +
            ", descricao='" + getDescricao() + "'" +
            ", estornada='" + getEstornada() + "'" +
            ", tipoTransacao='" + getTipoTransacao() + "'" +
            ", tipoPagamento='" + getTipoPagamento() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", ativa='" + getAtiva() + "'" +
            ", tipoEntidadeOrigem='" + getTipoEntidadeOrigem() + "'" +
            ", idEntidadeOrigem=" + getIdEntidadeOrigem() +
            ", carteiraOrigem=" + getCarteiraOrigem() +
            ", carteiraDestino=" + getCarteiraDestino() +
            ", moedaOrigem=" + getMoedaOrigem() +
            ", moedaDestino=" + getMoedaDestino() +
            "}";
    }
}
