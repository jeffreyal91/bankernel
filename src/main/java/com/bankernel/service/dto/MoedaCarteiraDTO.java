package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.MoedaCarteira} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoedaCarteiraDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String codigo;

    @NotNull
    @Size(max = 50)
    private String nome;

    @Size(max = 200)
    private String descricao;

    @NotNull
    private BigDecimal fatorConversao;

    @NotNull
    private Boolean ativa;

    @NotNull
    private Boolean principal;

    @NotNull
    private Integer casasDecimais;

    @Size(max = 1)
    private String separadorMilhar;

    @Size(max = 1)
    private String separadorDecimal;

    @Size(max = 5)
    private String simbolo;

    @NotNull
    private MoedaDTO moeda;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getFatorConversao() {
        return fatorConversao;
    }

    public void setFatorConversao(BigDecimal fatorConversao) {
        this.fatorConversao = fatorConversao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Integer getCasasDecimais() {
        return casasDecimais;
    }

    public void setCasasDecimais(Integer casasDecimais) {
        this.casasDecimais = casasDecimais;
    }

    public String getSeparadorMilhar() {
        return separadorMilhar;
    }

    public void setSeparadorMilhar(String separadorMilhar) {
        this.separadorMilhar = separadorMilhar;
    }

    public String getSeparadorDecimal() {
        return separadorDecimal;
    }

    public void setSeparadorDecimal(String separadorDecimal) {
        this.separadorDecimal = separadorDecimal;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public MoedaDTO getMoeda() {
        return moeda;
    }

    public void setMoeda(MoedaDTO moeda) {
        this.moeda = moeda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoedaCarteiraDTO)) {
            return false;
        }

        MoedaCarteiraDTO moedaCarteiraDTO = (MoedaCarteiraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moedaCarteiraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoedaCarteiraDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", fatorConversao=" + getFatorConversao() +
            ", ativa='" + getAtiva() + "'" +
            ", principal='" + getPrincipal() + "'" +
            ", casasDecimais=" + getCasasDecimais() +
            ", separadorMilhar='" + getSeparadorMilhar() + "'" +
            ", separadorDecimal='" + getSeparadorDecimal() + "'" +
            ", simbolo='" + getSimbolo() + "'" +
            ", moeda=" + getMoeda() +
            "}";
    }
}
