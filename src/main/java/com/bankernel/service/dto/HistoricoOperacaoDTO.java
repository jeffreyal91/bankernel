package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusHistorico;
import com.bankernel.domain.enumeration.EnumTipoHistorico;
import com.bankernel.domain.enumeration.EnumTipoSimbolo;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.HistoricoOperacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricoOperacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private BigDecimal saldoApos;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private EnumTipoSimbolo tipoSimbolo;

    @Size(max = 50)
    private String numeroReferencia;

    @NotNull
    private EnumTipoHistorico tipoHistorico;

    @NotNull
    private EnumStatusHistorico situacaoHistorico;

    @Size(max = 50)
    private String tipoEntidadeOrigem;

    private Long idEntidadeOrigem;

    private TransacaoDTO transacao;

    @NotNull
    private UserDTO usuario;

    @NotNull
    private CarteiraDTO carteira;

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

    public BigDecimal getSaldoApos() {
        return saldoApos;
    }

    public void setSaldoApos(BigDecimal saldoApos) {
        this.saldoApos = saldoApos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoSimbolo getTipoSimbolo() {
        return tipoSimbolo;
    }

    public void setTipoSimbolo(EnumTipoSimbolo tipoSimbolo) {
        this.tipoSimbolo = tipoSimbolo;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public EnumTipoHistorico getTipoHistorico() {
        return tipoHistorico;
    }

    public void setTipoHistorico(EnumTipoHistorico tipoHistorico) {
        this.tipoHistorico = tipoHistorico;
    }

    public EnumStatusHistorico getSituacaoHistorico() {
        return situacaoHistorico;
    }

    public void setSituacaoHistorico(EnumStatusHistorico situacaoHistorico) {
        this.situacaoHistorico = situacaoHistorico;
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

    public TransacaoDTO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoDTO transacao) {
        this.transacao = transacao;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public CarteiraDTO getCarteira() {
        return carteira;
    }

    public void setCarteira(CarteiraDTO carteira) {
        this.carteira = carteira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricoOperacaoDTO)) {
            return false;
        }

        HistoricoOperacaoDTO historicoOperacaoDTO = (HistoricoOperacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historicoOperacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoOperacaoDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", saldoApos=" + getSaldoApos() +
            ", descricao='" + getDescricao() + "'" +
            ", tipoSimbolo='" + getTipoSimbolo() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", tipoHistorico='" + getTipoHistorico() + "'" +
            ", situacaoHistorico='" + getSituacaoHistorico() + "'" +
            ", tipoEntidadeOrigem='" + getTipoEntidadeOrigem() + "'" +
            ", idEntidadeOrigem=" + getIdEntidadeOrigem() +
            ", transacao=" + getTransacao() +
            ", usuario=" + getUsuario() +
            ", carteira=" + getCarteira() +
            "}";
    }
}
