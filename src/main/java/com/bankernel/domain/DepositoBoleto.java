package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusDepositoBoleto;
import com.bankernel.domain.enumeration.EnumTipoDepositoBoleto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepositoBoleto.
 */
@Entity
@Table(name = "dep_deposito_boleto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepositoBoleto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoDepositoBoleto tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusDepositoBoleto situacao;

    @Size(max = 50)
    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

    @Size(max = 60)
    @Column(name = "linha_digitavel", length = 60)
    private String linhaDigitavel;

    @NotNull
    @Column(name = "valor_original", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorOriginal;

    @Column(name = "valor_creditado", precision = 21, scale = 2)
    private BigDecimal valorCreditado;

    @Column(name = "valor_recebido", precision = 21, scale = 2)
    private BigDecimal valorRecebido;

    @Size(max = 200)
    @Column(name = "pagador_nome", length = 200)
    private String pagadorNome;

    @Size(max = 11)
    @Column(name = "pagador_cpf", length = 11)
    private String pagadorCpf;

    @Size(max = 14)
    @Column(name = "pagador_cnpj", length = 14)
    private String pagadorCnpj;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_recebimento")
    private Instant dataRecebimento;

    @NotNull
    @Column(name = "contabilizado", nullable = false)
    private Boolean contabilizado;

    @JsonIgnoreProperties(
        value = { "transacao", "carteira", "moedaCarteira", "usuario", "contaBancaria", "depositoPix", "depositoBoleto" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Deposito deposito;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteira;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepositoBoleto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoDepositoBoleto getTipo() {
        return this.tipo;
    }

    public DepositoBoleto tipo(EnumTipoDepositoBoleto tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoDepositoBoleto tipo) {
        this.tipo = tipo;
    }

    public EnumStatusDepositoBoleto getSituacao() {
        return this.situacao;
    }

    public DepositoBoleto situacao(EnumStatusDepositoBoleto situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusDepositoBoleto situacao) {
        this.situacao = situacao;
    }

    public String getCodigoBarras() {
        return this.codigoBarras;
    }

    public DepositoBoleto codigoBarras(String codigoBarras) {
        this.setCodigoBarras(codigoBarras);
        return this;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getLinhaDigitavel() {
        return this.linhaDigitavel;
    }

    public DepositoBoleto linhaDigitavel(String linhaDigitavel) {
        this.setLinhaDigitavel(linhaDigitavel);
        return this;
    }

    public void setLinhaDigitavel(String linhaDigitavel) {
        this.linhaDigitavel = linhaDigitavel;
    }

    public BigDecimal getValorOriginal() {
        return this.valorOriginal;
    }

    public DepositoBoleto valorOriginal(BigDecimal valorOriginal) {
        this.setValorOriginal(valorOriginal);
        return this;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorCreditado() {
        return this.valorCreditado;
    }

    public DepositoBoleto valorCreditado(BigDecimal valorCreditado) {
        this.setValorCreditado(valorCreditado);
        return this;
    }

    public void setValorCreditado(BigDecimal valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimal getValorRecebido() {
        return this.valorRecebido;
    }

    public DepositoBoleto valorRecebido(BigDecimal valorRecebido) {
        this.setValorRecebido(valorRecebido);
        return this;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getPagadorNome() {
        return this.pagadorNome;
    }

    public DepositoBoleto pagadorNome(String pagadorNome) {
        this.setPagadorNome(pagadorNome);
        return this;
    }

    public void setPagadorNome(String pagadorNome) {
        this.pagadorNome = pagadorNome;
    }

    public String getPagadorCpf() {
        return this.pagadorCpf;
    }

    public DepositoBoleto pagadorCpf(String pagadorCpf) {
        this.setPagadorCpf(pagadorCpf);
        return this;
    }

    public void setPagadorCpf(String pagadorCpf) {
        this.pagadorCpf = pagadorCpf;
    }

    public String getPagadorCnpj() {
        return this.pagadorCnpj;
    }

    public DepositoBoleto pagadorCnpj(String pagadorCnpj) {
        this.setPagadorCnpj(pagadorCnpj);
        return this;
    }

    public void setPagadorCnpj(String pagadorCnpj) {
        this.pagadorCnpj = pagadorCnpj;
    }

    public LocalDate getDataVencimento() {
        return this.dataVencimento;
    }

    public DepositoBoleto dataVencimento(LocalDate dataVencimento) {
        this.setDataVencimento(dataVencimento);
        return this;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Instant getDataRecebimento() {
        return this.dataRecebimento;
    }

    public DepositoBoleto dataRecebimento(Instant dataRecebimento) {
        this.setDataRecebimento(dataRecebimento);
        return this;
    }

    public void setDataRecebimento(Instant dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public Boolean getContabilizado() {
        return this.contabilizado;
    }

    public DepositoBoleto contabilizado(Boolean contabilizado) {
        this.setContabilizado(contabilizado);
        return this;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public Deposito getDeposito() {
        return this.deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public DepositoBoleto deposito(Deposito deposito) {
        this.setDeposito(deposito);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public DepositoBoleto carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public DepositoBoleto usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepositoBoleto)) {
            return false;
        }
        return getId() != null && getId().equals(((DepositoBoleto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoBoleto{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", codigoBarras='" + getCodigoBarras() + "'" +
            ", linhaDigitavel='" + getLinhaDigitavel() + "'" +
            ", valorOriginal=" + getValorOriginal() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorRecebido=" + getValorRecebido() +
            ", pagadorNome='" + getPagadorNome() + "'" +
            ", pagadorCpf='" + getPagadorCpf() + "'" +
            ", pagadorCnpj='" + getPagadorCnpj() + "'" +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            "}";
    }
}
