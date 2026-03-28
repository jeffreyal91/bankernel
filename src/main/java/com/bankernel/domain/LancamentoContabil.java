package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumSinalLancamento;
import com.bankernel.domain.enumeration.EnumTipoLancamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LancamentoContabil.
 */
@Entity
@Table(name = "ctb_lancamento_contabil")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LancamentoContabil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_lancamento", nullable = false)
    private EnumTipoLancamento tipoLancamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sinal_lancamento", nullable = false)
    private EnumSinalLancamento sinalLancamento;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira" }, allowSetters = true)
    private ContaContabil contaContabil;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LancamentoContabil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public LancamentoContabil valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public EnumTipoLancamento getTipoLancamento() {
        return this.tipoLancamento;
    }

    public LancamentoContabil tipoLancamento(EnumTipoLancamento tipoLancamento) {
        this.setTipoLancamento(tipoLancamento);
        return this;
    }

    public void setTipoLancamento(EnumTipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public EnumSinalLancamento getSinalLancamento() {
        return this.sinalLancamento;
    }

    public LancamentoContabil sinalLancamento(EnumSinalLancamento sinalLancamento) {
        this.setSinalLancamento(sinalLancamento);
        return this;
    }

    public void setSinalLancamento(EnumSinalLancamento sinalLancamento) {
        this.sinalLancamento = sinalLancamento;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public LancamentoContabil ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Transacao getTransacao() {
        return this.transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public LancamentoContabil transacao(Transacao transacao) {
        this.setTransacao(transacao);
        return this;
    }

    public ContaContabil getContaContabil() {
        return this.contaContabil;
    }

    public void setContaContabil(ContaContabil contaContabil) {
        this.contaContabil = contaContabil;
    }

    public LancamentoContabil contaContabil(ContaContabil contaContabil) {
        this.setContaContabil(contaContabil);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LancamentoContabil)) {
            return false;
        }
        return getId() != null && getId().equals(((LancamentoContabil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LancamentoContabil{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", tipoLancamento='" + getTipoLancamento() + "'" +
            ", sinalLancamento='" + getSinalLancamento() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
