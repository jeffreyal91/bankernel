package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusSaquePix;
import com.bankernel.domain.enumeration.EnumTipoSaquePix;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SaquePix.
 */
@Entity
@Table(name = "saq_saque_pix")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaquePix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoSaquePix tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusSaquePix situacao;

    @NotNull
    @Column(name = "valor_saque", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorSaque;

    @Column(name = "valor_enviado", precision = 21, scale = 2)
    private BigDecimal valorEnviado;

    @Size(max = 100)
    @Column(name = "identificador_pagamento", length = 100)
    private String identificadorPagamento;

    @Size(max = 100)
    @Column(name = "identificador_ponta_a_ponta", length = 100)
    private String identificadorPontaAPonta;

    @Size(max = 200)
    @Column(name = "campo_livre", length = 200)
    private String campoLivre;

    @JsonIgnoreProperties(
        value = {
            "transacao",
            "transacaoEstorno",
            "carteira",
            "moedaCarteira",
            "contaBancariaDestino",
            "usuario",
            "escritorio",
            "saquePix",
            "saqueBoleto",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Saque saque;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SaquePix id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoSaquePix getTipo() {
        return this.tipo;
    }

    public SaquePix tipo(EnumTipoSaquePix tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoSaquePix tipo) {
        this.tipo = tipo;
    }

    public EnumStatusSaquePix getSituacao() {
        return this.situacao;
    }

    public SaquePix situacao(EnumStatusSaquePix situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusSaquePix situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getValorSaque() {
        return this.valorSaque;
    }

    public SaquePix valorSaque(BigDecimal valorSaque) {
        this.setValorSaque(valorSaque);
        return this;
    }

    public void setValorSaque(BigDecimal valorSaque) {
        this.valorSaque = valorSaque;
    }

    public BigDecimal getValorEnviado() {
        return this.valorEnviado;
    }

    public SaquePix valorEnviado(BigDecimal valorEnviado) {
        this.setValorEnviado(valorEnviado);
        return this;
    }

    public void setValorEnviado(BigDecimal valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getIdentificadorPagamento() {
        return this.identificadorPagamento;
    }

    public SaquePix identificadorPagamento(String identificadorPagamento) {
        this.setIdentificadorPagamento(identificadorPagamento);
        return this;
    }

    public void setIdentificadorPagamento(String identificadorPagamento) {
        this.identificadorPagamento = identificadorPagamento;
    }

    public String getIdentificadorPontaAPonta() {
        return this.identificadorPontaAPonta;
    }

    public SaquePix identificadorPontaAPonta(String identificadorPontaAPonta) {
        this.setIdentificadorPontaAPonta(identificadorPontaAPonta);
        return this;
    }

    public void setIdentificadorPontaAPonta(String identificadorPontaAPonta) {
        this.identificadorPontaAPonta = identificadorPontaAPonta;
    }

    public String getCampoLivre() {
        return this.campoLivre;
    }

    public SaquePix campoLivre(String campoLivre) {
        this.setCampoLivre(campoLivre);
        return this;
    }

    public void setCampoLivre(String campoLivre) {
        this.campoLivre = campoLivre;
    }

    public Saque getSaque() {
        return this.saque;
    }

    public void setSaque(Saque saque) {
        this.saque = saque;
    }

    public SaquePix saque(Saque saque) {
        this.setSaque(saque);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public SaquePix usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaquePix)) {
            return false;
        }
        return getId() != null && getId().equals(((SaquePix) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaquePix{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", valorSaque=" + getValorSaque() +
            ", valorEnviado=" + getValorEnviado() +
            ", identificadorPagamento='" + getIdentificadorPagamento() + "'" +
            ", identificadorPontaAPonta='" + getIdentificadorPontaAPonta() + "'" +
            ", campoLivre='" + getCampoLivre() + "'" +
            "}";
    }
}
