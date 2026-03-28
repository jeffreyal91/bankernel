package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumSinalLancamento;
import com.bankernel.domain.enumeration.EnumTipoLancamento;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.LancamentoContabil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LancamentoContabilDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private EnumTipoLancamento tipoLancamento;

    @NotNull
    private EnumSinalLancamento sinalLancamento;

    @NotNull
    private Boolean ativo;

    private TransacaoDTO transacao;

    @NotNull
    private ContaContabilDTO contaContabil;

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

    public EnumTipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(EnumTipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public EnumSinalLancamento getSinalLancamento() {
        return sinalLancamento;
    }

    public void setSinalLancamento(EnumSinalLancamento sinalLancamento) {
        this.sinalLancamento = sinalLancamento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public TransacaoDTO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoDTO transacao) {
        this.transacao = transacao;
    }

    public ContaContabilDTO getContaContabil() {
        return contaContabil;
    }

    public void setContaContabil(ContaContabilDTO contaContabil) {
        this.contaContabil = contaContabil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LancamentoContabilDTO)) {
            return false;
        }

        LancamentoContabilDTO lancamentoContabilDTO = (LancamentoContabilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lancamentoContabilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LancamentoContabilDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", tipoLancamento='" + getTipoLancamento() + "'" +
            ", sinalLancamento='" + getSinalLancamento() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", transacao=" + getTransacao() +
            ", contaContabil=" + getContaContabil() +
            "}";
    }
}
