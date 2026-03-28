package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusDeposito;
import com.bankernel.domain.enumeration.EnumTipoDeposito;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deposito.
 */
@Entity
@Table(name = "dep_deposito")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Deposito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "valor_creditado", precision = 21, scale = 2)
    private BigDecimal valorCreditado;

    @Column(name = "valor_saldo_carteira", precision = 21, scale = 2)
    private BigDecimal valorSaldoCarteira;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_deposito", nullable = false)
    private EnumTipoDeposito tipoDeposito;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_deposito", nullable = false)
    private EnumStatusDeposito situacaoDeposito;

    @NotNull
    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50, nullable = false)
    private String numeroReferencia;

    @Size(max = 100)
    @Column(name = "referencia_externa", length = 100)
    private String referenciaExterna;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @Size(max = 500)
    @Column(name = "motivo_rejeicao", length = 500)
    private String motivoRejeicao;

    @NotNull
    @Column(name = "contabilizado", nullable = false)
    private Boolean contabilizado;

    @Size(max = 200)
    @Column(name = "nome_usuario_fixo", length = 200)
    private String nomeUsuarioFixo;

    @Column(name = "numero_parcela")
    private Integer numeroParcela;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteira;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuario", "pais", "moeda" }, allowSetters = true)
    private ContaBancaria contaBancaria;

    @JsonIgnoreProperties(value = { "deposito", "carteira", "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "deposito")
    private DepositoPix depositoPix;

    @JsonIgnoreProperties(value = { "deposito", "carteira", "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "deposito")
    private DepositoBoleto depositoBoleto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deposito id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Deposito valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorCreditado() {
        return this.valorCreditado;
    }

    public Deposito valorCreditado(BigDecimal valorCreditado) {
        this.setValorCreditado(valorCreditado);
        return this;
    }

    public void setValorCreditado(BigDecimal valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimal getValorSaldoCarteira() {
        return this.valorSaldoCarteira;
    }

    public Deposito valorSaldoCarteira(BigDecimal valorSaldoCarteira) {
        this.setValorSaldoCarteira(valorSaldoCarteira);
        return this;
    }

    public void setValorSaldoCarteira(BigDecimal valorSaldoCarteira) {
        this.valorSaldoCarteira = valorSaldoCarteira;
    }

    public EnumTipoDeposito getTipoDeposito() {
        return this.tipoDeposito;
    }

    public Deposito tipoDeposito(EnumTipoDeposito tipoDeposito) {
        this.setTipoDeposito(tipoDeposito);
        return this;
    }

    public void setTipoDeposito(EnumTipoDeposito tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    public EnumStatusDeposito getSituacaoDeposito() {
        return this.situacaoDeposito;
    }

    public Deposito situacaoDeposito(EnumStatusDeposito situacaoDeposito) {
        this.setSituacaoDeposito(situacaoDeposito);
        return this;
    }

    public void setSituacaoDeposito(EnumStatusDeposito situacaoDeposito) {
        this.situacaoDeposito = situacaoDeposito;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public Deposito numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getReferenciaExterna() {
        return this.referenciaExterna;
    }

    public Deposito referenciaExterna(String referenciaExterna) {
        this.setReferenciaExterna(referenciaExterna);
        return this;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Deposito descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMotivoRejeicao() {
        return this.motivoRejeicao;
    }

    public Deposito motivoRejeicao(String motivoRejeicao) {
        this.setMotivoRejeicao(motivoRejeicao);
        return this;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public Boolean getContabilizado() {
        return this.contabilizado;
    }

    public Deposito contabilizado(Boolean contabilizado) {
        this.setContabilizado(contabilizado);
        return this;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public String getNomeUsuarioFixo() {
        return this.nomeUsuarioFixo;
    }

    public Deposito nomeUsuarioFixo(String nomeUsuarioFixo) {
        this.setNomeUsuarioFixo(nomeUsuarioFixo);
        return this;
    }

    public void setNomeUsuarioFixo(String nomeUsuarioFixo) {
        this.nomeUsuarioFixo = nomeUsuarioFixo;
    }

    public Integer getNumeroParcela() {
        return this.numeroParcela;
    }

    public Deposito numeroParcela(Integer numeroParcela) {
        this.setNumeroParcela(numeroParcela);
        return this;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Transacao getTransacao() {
        return this.transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Deposito transacao(Transacao transacao) {
        this.setTransacao(transacao);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public Deposito carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public Deposito moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Deposito usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public ContaBancaria getContaBancaria() {
        return this.contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public Deposito contaBancaria(ContaBancaria contaBancaria) {
        this.setContaBancaria(contaBancaria);
        return this;
    }

    public DepositoPix getDepositoPix() {
        return this.depositoPix;
    }

    public void setDepositoPix(DepositoPix depositoPix) {
        if (this.depositoPix != null) {
            this.depositoPix.setDeposito(null);
        }
        if (depositoPix != null) {
            depositoPix.setDeposito(this);
        }
        this.depositoPix = depositoPix;
    }

    public Deposito depositoPix(DepositoPix depositoPix) {
        this.setDepositoPix(depositoPix);
        return this;
    }

    public DepositoBoleto getDepositoBoleto() {
        return this.depositoBoleto;
    }

    public void setDepositoBoleto(DepositoBoleto depositoBoleto) {
        if (this.depositoBoleto != null) {
            this.depositoBoleto.setDeposito(null);
        }
        if (depositoBoleto != null) {
            depositoBoleto.setDeposito(this);
        }
        this.depositoBoleto = depositoBoleto;
    }

    public Deposito depositoBoleto(DepositoBoleto depositoBoleto) {
        this.setDepositoBoleto(depositoBoleto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deposito)) {
            return false;
        }
        return getId() != null && getId().equals(((Deposito) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deposito{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorSaldoCarteira=" + getValorSaldoCarteira() +
            ", tipoDeposito='" + getTipoDeposito() + "'" +
            ", situacaoDeposito='" + getSituacaoDeposito() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", referenciaExterna='" + getReferenciaExterna() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", motivoRejeicao='" + getMotivoRejeicao() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            ", nomeUsuarioFixo='" + getNomeUsuarioFixo() + "'" +
            ", numeroParcela=" + getNumeroParcela() +
            "}";
    }
}
