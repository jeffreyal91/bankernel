package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumCategoriaContaContabil;
import com.bankernel.domain.enumeration.EnumTipoContaContabil;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.ContaContabil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaContabilDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String codigo;

    @NotNull
    @Size(max = 100)
    private String nome;

    @NotNull
    private BigDecimal saldo;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private EnumTipoContaContabil tipoContaContabil;

    @NotNull
    private EnumCategoriaContaContabil categoriaContaContabil;

    @NotNull
    private Boolean ativa;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoContaContabil getTipoContaContabil() {
        return tipoContaContabil;
    }

    public void setTipoContaContabil(EnumTipoContaContabil tipoContaContabil) {
        this.tipoContaContabil = tipoContaContabil;
    }

    public EnumCategoriaContaContabil getCategoriaContaContabil() {
        return categoriaContaContabil;
    }

    public void setCategoriaContaContabil(EnumCategoriaContaContabil categoriaContaContabil) {
        this.categoriaContaContabil = categoriaContaContabil;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
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
        if (!(o instanceof ContaContabilDTO)) {
            return false;
        }

        ContaContabilDTO contaContabilDTO = (ContaContabilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contaContabilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaContabilDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", saldo=" + getSaldo() +
            ", descricao='" + getDescricao() + "'" +
            ", tipoContaContabil='" + getTipoContaContabil() + "'" +
            ", categoriaContaContabil='" + getCategoriaContaContabil() + "'" +
            ", ativa='" + getAtiva() + "'" +
            ", moedaCarteira=" + getMoedaCarteira() +
            "}";
    }
}
