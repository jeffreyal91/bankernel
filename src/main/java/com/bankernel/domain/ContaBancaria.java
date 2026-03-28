package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumTipoConta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContaBancaria.
 */
@Entity
@Table(name = "saq_conta_bancaria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "nome_titular", length = 200, nullable = false)
    private String nomeTitular;

    @NotNull
    @Size(max = 30)
    @Column(name = "numero_conta", length = 30, nullable = false)
    private String numeroConta;

    @Size(max = 10)
    @Column(name = "agencia", length = 10)
    private String agencia;

    @Size(max = 100)
    @Column(name = "nome_banco", length = 100)
    private String nomeBanco;

    @Size(max = 10)
    @Column(name = "codigo_banco", length = 10)
    private String codigoBanco;

    @Size(max = 8)
    @Column(name = "ispb", length = 8)
    private String ispb;

    @Size(max = 20)
    @Column(name = "codigo_swift", length = 20)
    private String codigoSwift;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false)
    private EnumTipoConta tipoConta;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pais pais;

    @ManyToOne(fetch = FetchType.LAZY)
    private Moeda moeda;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContaBancaria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTitular() {
        return this.nomeTitular;
    }

    public ContaBancaria nomeTitular(String nomeTitular) {
        this.setNomeTitular(nomeTitular);
        return this;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getNumeroConta() {
        return this.numeroConta;
    }

    public ContaBancaria numeroConta(String numeroConta) {
        this.setNumeroConta(numeroConta);
        return this;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return this.agencia;
    }

    public ContaBancaria agencia(String agencia) {
        this.setAgencia(agencia);
        return this;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNomeBanco() {
        return this.nomeBanco;
    }

    public ContaBancaria nomeBanco(String nomeBanco) {
        this.setNomeBanco(nomeBanco);
        return this;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getCodigoBanco() {
        return this.codigoBanco;
    }

    public ContaBancaria codigoBanco(String codigoBanco) {
        this.setCodigoBanco(codigoBanco);
        return this;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getIspb() {
        return this.ispb;
    }

    public ContaBancaria ispb(String ispb) {
        this.setIspb(ispb);
        return this;
    }

    public void setIspb(String ispb) {
        this.ispb = ispb;
    }

    public String getCodigoSwift() {
        return this.codigoSwift;
    }

    public ContaBancaria codigoSwift(String codigoSwift) {
        this.setCodigoSwift(codigoSwift);
        return this;
    }

    public void setCodigoSwift(String codigoSwift) {
        this.codigoSwift = codigoSwift;
    }

    public EnumTipoConta getTipoConta() {
        return this.tipoConta;
    }

    public ContaBancaria tipoConta(EnumTipoConta tipoConta) {
        this.setTipoConta(tipoConta);
        return this;
    }

    public void setTipoConta(EnumTipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public ContaBancaria ativa(Boolean ativa) {
        this.setAtiva(ativa);
        return this;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public ContaBancaria usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public ContaBancaria pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    public Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(Moeda moeda) {
        this.moeda = moeda;
    }

    public ContaBancaria moeda(Moeda moeda) {
        this.setMoeda(moeda);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaBancaria)) {
            return false;
        }
        return getId() != null && getId().equals(((ContaBancaria) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaBancaria{" +
            "id=" + getId() +
            ", nomeTitular='" + getNomeTitular() + "'" +
            ", numeroConta='" + getNumeroConta() + "'" +
            ", agencia='" + getAgencia() + "'" +
            ", nomeBanco='" + getNomeBanco() + "'" +
            ", codigoBanco='" + getCodigoBanco() + "'" +
            ", ispb='" + getIspb() + "'" +
            ", codigoSwift='" + getCodigoSwift() + "'" +
            ", tipoConta='" + getTipoConta() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
