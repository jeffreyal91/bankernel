package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusTransacao;
import com.bankernel.domain.enumeration.EnumTipoPagamento;
import com.bankernel.domain.enumeration.EnumTipoTransacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transacao.
 */
@Entity
@Table(name = "mov_transacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valor_enviado", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorEnviado;

    @NotNull
    @Column(name = "valor_recebido", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorRecebido;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Column(name = "estornada", nullable = false)
    private Boolean estornada;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private EnumTipoTransacao tipoTransacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento")
    private EnumTipoPagamento tipoPagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusTransacao situacao;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @Size(max = 50)
    @Column(name = "tipo_entidade_origem", length = 50)
    private String tipoEntidadeOrigem;

    @Column(name = "id_entidade_origem")
    private Long idEntidadeOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteiraOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteiraDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaDestino;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorEnviado() {
        return this.valorEnviado;
    }

    public Transacao valorEnviado(BigDecimal valorEnviado) {
        this.setValorEnviado(valorEnviado);
        return this;
    }

    public void setValorEnviado(BigDecimal valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public BigDecimal getValorRecebido() {
        return this.valorRecebido;
    }

    public Transacao valorRecebido(BigDecimal valorRecebido) {
        this.setValorRecebido(valorRecebido);
        return this;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Transacao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getEstornada() {
        return this.estornada;
    }

    public Transacao estornada(Boolean estornada) {
        this.setEstornada(estornada);
        return this;
    }

    public void setEstornada(Boolean estornada) {
        this.estornada = estornada;
    }

    public EnumTipoTransacao getTipoTransacao() {
        return this.tipoTransacao;
    }

    public Transacao tipoTransacao(EnumTipoTransacao tipoTransacao) {
        this.setTipoTransacao(tipoTransacao);
        return this;
    }

    public void setTipoTransacao(EnumTipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public EnumTipoPagamento getTipoPagamento() {
        return this.tipoPagamento;
    }

    public Transacao tipoPagamento(EnumTipoPagamento tipoPagamento) {
        this.setTipoPagamento(tipoPagamento);
        return this;
    }

    public void setTipoPagamento(EnumTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public EnumStatusTransacao getSituacao() {
        return this.situacao;
    }

    public Transacao situacao(EnumStatusTransacao situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusTransacao situacao) {
        this.situacao = situacao;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public Transacao ativa(Boolean ativa) {
        this.setAtiva(ativa);
        return this;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public String getTipoEntidadeOrigem() {
        return this.tipoEntidadeOrigem;
    }

    public Transacao tipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.setTipoEntidadeOrigem(tipoEntidadeOrigem);
        return this;
    }

    public void setTipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public Long getIdEntidadeOrigem() {
        return this.idEntidadeOrigem;
    }

    public Transacao idEntidadeOrigem(Long idEntidadeOrigem) {
        this.setIdEntidadeOrigem(idEntidadeOrigem);
        return this;
    }

    public void setIdEntidadeOrigem(Long idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
    }

    public Carteira getCarteiraOrigem() {
        return this.carteiraOrigem;
    }

    public void setCarteiraOrigem(Carteira carteira) {
        this.carteiraOrigem = carteira;
    }

    public Transacao carteiraOrigem(Carteira carteira) {
        this.setCarteiraOrigem(carteira);
        return this;
    }

    public Carteira getCarteiraDestino() {
        return this.carteiraDestino;
    }

    public void setCarteiraDestino(Carteira carteira) {
        this.carteiraDestino = carteira;
    }

    public Transacao carteiraDestino(Carteira carteira) {
        this.setCarteiraDestino(carteira);
        return this;
    }

    public MoedaCarteira getMoedaOrigem() {
        return this.moedaOrigem;
    }

    public void setMoedaOrigem(MoedaCarteira moedaCarteira) {
        this.moedaOrigem = moedaCarteira;
    }

    public Transacao moedaOrigem(MoedaCarteira moedaCarteira) {
        this.setMoedaOrigem(moedaCarteira);
        return this;
    }

    public MoedaCarteira getMoedaDestino() {
        return this.moedaDestino;
    }

    public void setMoedaDestino(MoedaCarteira moedaCarteira) {
        this.moedaDestino = moedaCarteira;
    }

    public Transacao moedaDestino(MoedaCarteira moedaCarteira) {
        this.setMoedaDestino(moedaCarteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Transacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transacao{" +
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
            "}";
    }
}
