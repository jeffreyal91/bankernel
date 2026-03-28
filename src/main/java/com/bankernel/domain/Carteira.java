package com.bankernel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Carteira.
 */
@Entity
@Table(name = "car_carteira")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Carteira implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "saldo", precision = 21, scale = 2, nullable = false)
    private BigDecimal saldo;

    @NotNull
    @Column(name = "limite_negativo", precision = 21, scale = 2, nullable = false)
    private BigDecimal limiteNegativo;

    @NotNull
    @Column(name = "valor_congelado", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorCongelado;

    @NotNull
    @Size(max = 20)
    @Column(name = "numero_conta", length = 20, nullable = false, unique = true)
    private String numeroConta;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Carteira id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public Carteira saldo(BigDecimal saldo) {
        this.setSaldo(saldo);
        return this;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimiteNegativo() {
        return this.limiteNegativo;
    }

    public Carteira limiteNegativo(BigDecimal limiteNegativo) {
        this.setLimiteNegativo(limiteNegativo);
        return this;
    }

    public void setLimiteNegativo(BigDecimal limiteNegativo) {
        this.limiteNegativo = limiteNegativo;
    }

    public BigDecimal getValorCongelado() {
        return this.valorCongelado;
    }

    public Carteira valorCongelado(BigDecimal valorCongelado) {
        this.setValorCongelado(valorCongelado);
        return this;
    }

    public void setValorCongelado(BigDecimal valorCongelado) {
        this.valorCongelado = valorCongelado;
    }

    public String getNumeroConta() {
        return this.numeroConta;
    }

    public Carteira numeroConta(String numeroConta) {
        this.setNumeroConta(numeroConta);
        return this;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public Carteira ativa(Boolean ativa) {
        this.setAtiva(ativa);
        return this;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public Carteira moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Carteira usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Carteira)) {
            return false;
        }
        return getId() != null && getId().equals(((Carteira) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Carteira{" +
            "id=" + getId() +
            ", saldo=" + getSaldo() +
            ", limiteNegativo=" + getLimiteNegativo() +
            ", valorCongelado=" + getValorCongelado() +
            ", numeroConta='" + getNumeroConta() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
