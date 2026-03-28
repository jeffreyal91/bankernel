package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusDepositoPix;
import com.bankernel.domain.enumeration.EnumTipoDepositoPix;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepositoPix.
 */
@Entity
@Table(name = "dep_deposito_pix")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepositoPix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoDepositoPix tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusDepositoPix situacao;

    @NotNull
    @Column(name = "valor_original", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorOriginal;

    @Column(name = "valor_creditado", precision = 21, scale = 2)
    private BigDecimal valorCreditado;

    @Column(name = "valor_recebido", precision = 21, scale = 2)
    private BigDecimal valorRecebido;

    @Size(max = 500)
    @Column(name = "codigo_qr", length = 500)
    private String codigoQr;

    @Size(max = 100)
    @Column(name = "identificador_transacao", length = 100)
    private String identificadorTransacao;

    @Size(max = 100)
    @Column(name = "identificador_ponta_a_ponta", length = 100)
    private String identificadorPontaAPonta;

    @Size(max = 200)
    @Column(name = "pagador_nome", length = 200)
    private String pagadorNome;

    @Size(max = 11)
    @Column(name = "pagador_cpf", length = 11)
    private String pagadorCpf;

    @Size(max = 14)
    @Column(name = "pagador_cnpj", length = 14)
    private String pagadorCnpj;

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

    public DepositoPix id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoDepositoPix getTipo() {
        return this.tipo;
    }

    public DepositoPix tipo(EnumTipoDepositoPix tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoDepositoPix tipo) {
        this.tipo = tipo;
    }

    public EnumStatusDepositoPix getSituacao() {
        return this.situacao;
    }

    public DepositoPix situacao(EnumStatusDepositoPix situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusDepositoPix situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getValorOriginal() {
        return this.valorOriginal;
    }

    public DepositoPix valorOriginal(BigDecimal valorOriginal) {
        this.setValorOriginal(valorOriginal);
        return this;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorCreditado() {
        return this.valorCreditado;
    }

    public DepositoPix valorCreditado(BigDecimal valorCreditado) {
        this.setValorCreditado(valorCreditado);
        return this;
    }

    public void setValorCreditado(BigDecimal valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimal getValorRecebido() {
        return this.valorRecebido;
    }

    public DepositoPix valorRecebido(BigDecimal valorRecebido) {
        this.setValorRecebido(valorRecebido);
        return this;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getCodigoQr() {
        return this.codigoQr;
    }

    public DepositoPix codigoQr(String codigoQr) {
        this.setCodigoQr(codigoQr);
        return this;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public String getIdentificadorTransacao() {
        return this.identificadorTransacao;
    }

    public DepositoPix identificadorTransacao(String identificadorTransacao) {
        this.setIdentificadorTransacao(identificadorTransacao);
        return this;
    }

    public void setIdentificadorTransacao(String identificadorTransacao) {
        this.identificadorTransacao = identificadorTransacao;
    }

    public String getIdentificadorPontaAPonta() {
        return this.identificadorPontaAPonta;
    }

    public DepositoPix identificadorPontaAPonta(String identificadorPontaAPonta) {
        this.setIdentificadorPontaAPonta(identificadorPontaAPonta);
        return this;
    }

    public void setIdentificadorPontaAPonta(String identificadorPontaAPonta) {
        this.identificadorPontaAPonta = identificadorPontaAPonta;
    }

    public String getPagadorNome() {
        return this.pagadorNome;
    }

    public DepositoPix pagadorNome(String pagadorNome) {
        this.setPagadorNome(pagadorNome);
        return this;
    }

    public void setPagadorNome(String pagadorNome) {
        this.pagadorNome = pagadorNome;
    }

    public String getPagadorCpf() {
        return this.pagadorCpf;
    }

    public DepositoPix pagadorCpf(String pagadorCpf) {
        this.setPagadorCpf(pagadorCpf);
        return this;
    }

    public void setPagadorCpf(String pagadorCpf) {
        this.pagadorCpf = pagadorCpf;
    }

    public String getPagadorCnpj() {
        return this.pagadorCnpj;
    }

    public DepositoPix pagadorCnpj(String pagadorCnpj) {
        this.setPagadorCnpj(pagadorCnpj);
        return this;
    }

    public void setPagadorCnpj(String pagadorCnpj) {
        this.pagadorCnpj = pagadorCnpj;
    }

    public Instant getDataRecebimento() {
        return this.dataRecebimento;
    }

    public DepositoPix dataRecebimento(Instant dataRecebimento) {
        this.setDataRecebimento(dataRecebimento);
        return this;
    }

    public void setDataRecebimento(Instant dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public Boolean getContabilizado() {
        return this.contabilizado;
    }

    public DepositoPix contabilizado(Boolean contabilizado) {
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

    public DepositoPix deposito(Deposito deposito) {
        this.setDeposito(deposito);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public DepositoPix carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public DepositoPix usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepositoPix)) {
            return false;
        }
        return getId() != null && getId().equals(((DepositoPix) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoPix{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", valorOriginal=" + getValorOriginal() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorRecebido=" + getValorRecebido() +
            ", codigoQr='" + getCodigoQr() + "'" +
            ", identificadorTransacao='" + getIdentificadorTransacao() + "'" +
            ", identificadorPontaAPonta='" + getIdentificadorPontaAPonta() + "'" +
            ", pagadorNome='" + getPagadorNome() + "'" +
            ", pagadorCpf='" + getPagadorCpf() + "'" +
            ", pagadorCnpj='" + getPagadorCnpj() + "'" +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            "}";
    }
}
