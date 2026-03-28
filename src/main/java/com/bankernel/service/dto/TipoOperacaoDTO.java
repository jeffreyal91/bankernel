package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumSinalContabil;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.TipoOperacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoOperacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoOperacao codigo;

    @NotNull
    @Size(max = 100)
    private String nome;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private Boolean ativo;

    @NotNull
    private EnumSinalContabil sinalCredito;

    @NotNull
    private EnumSinalContabil sinalDebito;

    private ContaContabilDTO contaCredito;

    private ContaContabilDTO contaDebito;

    private MoedaCarteiraDTO moedaCarteira;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoOperacao getCodigo() {
        return codigo;
    }

    public void setCodigo(EnumTipoOperacao codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnumSinalContabil getSinalCredito() {
        return sinalCredito;
    }

    public void setSinalCredito(EnumSinalContabil sinalCredito) {
        this.sinalCredito = sinalCredito;
    }

    public EnumSinalContabil getSinalDebito() {
        return sinalDebito;
    }

    public void setSinalDebito(EnumSinalContabil sinalDebito) {
        this.sinalDebito = sinalDebito;
    }

    public ContaContabilDTO getContaCredito() {
        return contaCredito;
    }

    public void setContaCredito(ContaContabilDTO contaCredito) {
        this.contaCredito = contaCredito;
    }

    public ContaContabilDTO getContaDebito() {
        return contaDebito;
    }

    public void setContaDebito(ContaContabilDTO contaDebito) {
        this.contaDebito = contaDebito;
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
        if (!(o instanceof TipoOperacaoDTO)) {
            return false;
        }

        TipoOperacaoDTO tipoOperacaoDTO = (TipoOperacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoOperacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoOperacaoDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", sinalCredito='" + getSinalCredito() + "'" +
            ", sinalDebito='" + getSinalDebito() + "'" +
            ", contaCredito=" + getContaCredito() +
            ", contaDebito=" + getContaDebito() +
            ", moedaCarteira=" + getMoedaCarteira() +
            "}";
    }
}
