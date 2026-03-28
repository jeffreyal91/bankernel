package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusHistorico;
import com.bankernel.domain.enumeration.EnumTipoHistorico;
import com.bankernel.domain.enumeration.EnumTipoSimbolo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HistoricoOperacao.
 */
@Entity
@Table(name = "mov_historico_operacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricoOperacao implements Serializable {

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
    @Column(name = "saldo_apos", precision = 21, scale = 2, nullable = false)
    private BigDecimal saldoApos;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_simbolo", nullable = false)
    private EnumTipoSimbolo tipoSimbolo;

    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50)
    private String numeroReferencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_historico", nullable = false)
    private EnumTipoHistorico tipoHistorico;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_historico", nullable = false)
    private EnumStatusHistorico situacaoHistorico;

    @Size(max = 50)
    @Column(name = "tipo_entidade_origem", length = 50)
    private String tipoEntidadeOrigem;

    @Column(name = "id_entidade_origem")
    private Long idEntidadeOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacao;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoricoOperacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public HistoricoOperacao valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldoApos() {
        return this.saldoApos;
    }

    public HistoricoOperacao saldoApos(BigDecimal saldoApos) {
        this.setSaldoApos(saldoApos);
        return this;
    }

    public void setSaldoApos(BigDecimal saldoApos) {
        this.saldoApos = saldoApos;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public HistoricoOperacao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoSimbolo getTipoSimbolo() {
        return this.tipoSimbolo;
    }

    public HistoricoOperacao tipoSimbolo(EnumTipoSimbolo tipoSimbolo) {
        this.setTipoSimbolo(tipoSimbolo);
        return this;
    }

    public void setTipoSimbolo(EnumTipoSimbolo tipoSimbolo) {
        this.tipoSimbolo = tipoSimbolo;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public HistoricoOperacao numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public EnumTipoHistorico getTipoHistorico() {
        return this.tipoHistorico;
    }

    public HistoricoOperacao tipoHistorico(EnumTipoHistorico tipoHistorico) {
        this.setTipoHistorico(tipoHistorico);
        return this;
    }

    public void setTipoHistorico(EnumTipoHistorico tipoHistorico) {
        this.tipoHistorico = tipoHistorico;
    }

    public EnumStatusHistorico getSituacaoHistorico() {
        return this.situacaoHistorico;
    }

    public HistoricoOperacao situacaoHistorico(EnumStatusHistorico situacaoHistorico) {
        this.setSituacaoHistorico(situacaoHistorico);
        return this;
    }

    public void setSituacaoHistorico(EnumStatusHistorico situacaoHistorico) {
        this.situacaoHistorico = situacaoHistorico;
    }

    public String getTipoEntidadeOrigem() {
        return this.tipoEntidadeOrigem;
    }

    public HistoricoOperacao tipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.setTipoEntidadeOrigem(tipoEntidadeOrigem);
        return this;
    }

    public void setTipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public Long getIdEntidadeOrigem() {
        return this.idEntidadeOrigem;
    }

    public HistoricoOperacao idEntidadeOrigem(Long idEntidadeOrigem) {
        this.setIdEntidadeOrigem(idEntidadeOrigem);
        return this;
    }

    public void setIdEntidadeOrigem(Long idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
    }

    public Transacao getTransacao() {
        return this.transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public HistoricoOperacao transacao(Transacao transacao) {
        this.setTransacao(transacao);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public HistoricoOperacao usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public HistoricoOperacao carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricoOperacao)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoricoOperacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoOperacao{" +
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
            "}";
    }
}
