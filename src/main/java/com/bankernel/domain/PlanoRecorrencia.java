package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumMetodoPagamentoRecorrencia;
import com.bankernel.domain.enumeration.EnumTipoPlanoRecorrencia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlanoRecorrencia.
 */
@Entity
@Table(name = "cob_plano_recorrencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanoRecorrencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "identificador_externo", length = 100)
    private String identificadorExterno;

    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50)
    private String numeroReferencia;

    @NotNull
    @Size(max = 200)
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "dias_teste")
    private Integer diasTeste;

    @NotNull
    @Column(name = "intervalo", nullable = false)
    private Integer intervalo;

    @Column(name = "parcelas")
    private Integer parcelas;

    @NotNull
    @Column(name = "tentativas", nullable = false)
    private Integer tentativas;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false)
    private EnumMetodoPagamentoRecorrencia metodoPagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plano", nullable = false)
    private EnumTipoPlanoRecorrencia tipoPlano;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuario", "moedaCarteira" }, allowSetters = true)
    private LinkCobranca linkCobranca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlanoRecorrencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificadorExterno() {
        return this.identificadorExterno;
    }

    public PlanoRecorrencia identificadorExterno(String identificadorExterno) {
        this.setIdentificadorExterno(identificadorExterno);
        return this;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public PlanoRecorrencia numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getNome() {
        return this.nome;
    }

    public PlanoRecorrencia nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public PlanoRecorrencia descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public PlanoRecorrencia valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDiasTeste() {
        return this.diasTeste;
    }

    public PlanoRecorrencia diasTeste(Integer diasTeste) {
        this.setDiasTeste(diasTeste);
        return this;
    }

    public void setDiasTeste(Integer diasTeste) {
        this.diasTeste = diasTeste;
    }

    public Integer getIntervalo() {
        return this.intervalo;
    }

    public PlanoRecorrencia intervalo(Integer intervalo) {
        this.setIntervalo(intervalo);
        return this;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public Integer getParcelas() {
        return this.parcelas;
    }

    public PlanoRecorrencia parcelas(Integer parcelas) {
        this.setParcelas(parcelas);
        return this;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public Integer getTentativas() {
        return this.tentativas;
    }

    public PlanoRecorrencia tentativas(Integer tentativas) {
        this.setTentativas(tentativas);
        return this;
    }

    public void setTentativas(Integer tentativas) {
        this.tentativas = tentativas;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public PlanoRecorrencia ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnumMetodoPagamentoRecorrencia getMetodoPagamento() {
        return this.metodoPagamento;
    }

    public PlanoRecorrencia metodoPagamento(EnumMetodoPagamentoRecorrencia metodoPagamento) {
        this.setMetodoPagamento(metodoPagamento);
        return this;
    }

    public void setMetodoPagamento(EnumMetodoPagamentoRecorrencia metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public EnumTipoPlanoRecorrencia getTipoPlano() {
        return this.tipoPlano;
    }

    public PlanoRecorrencia tipoPlano(EnumTipoPlanoRecorrencia tipoPlano) {
        this.setTipoPlano(tipoPlano);
        return this;
    }

    public void setTipoPlano(EnumTipoPlanoRecorrencia tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public PlanoRecorrencia usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public LinkCobranca getLinkCobranca() {
        return this.linkCobranca;
    }

    public void setLinkCobranca(LinkCobranca linkCobranca) {
        this.linkCobranca = linkCobranca;
    }

    public PlanoRecorrencia linkCobranca(LinkCobranca linkCobranca) {
        this.setLinkCobranca(linkCobranca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanoRecorrencia)) {
            return false;
        }
        return getId() != null && getId().equals(((PlanoRecorrencia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanoRecorrencia{" +
            "id=" + getId() +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", diasTeste=" + getDiasTeste() +
            ", intervalo=" + getIntervalo() +
            ", parcelas=" + getParcelas() +
            ", tentativas=" + getTentativas() +
            ", ativo='" + getAtivo() + "'" +
            ", metodoPagamento='" + getMetodoPagamento() + "'" +
            ", tipoPlano='" + getTipoPlano() + "'" +
            "}";
    }
}
