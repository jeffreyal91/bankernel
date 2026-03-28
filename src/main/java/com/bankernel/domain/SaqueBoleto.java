package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusSaqueBoleto;
import com.bankernel.domain.enumeration.EnumTipoSaqueBoleto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SaqueBoleto.
 */
@Entity
@Table(name = "saq_saque_boleto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaqueBoleto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoSaqueBoleto tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusSaqueBoleto situacao;

    @NotNull
    @Column(name = "valor_saque", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorSaque;

    @Column(name = "valor_enviado", precision = 21, scale = 2)
    private BigDecimal valorEnviado;

    @Size(max = 50)
    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

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

    public SaqueBoleto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoSaqueBoleto getTipo() {
        return this.tipo;
    }

    public SaqueBoleto tipo(EnumTipoSaqueBoleto tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoSaqueBoleto tipo) {
        this.tipo = tipo;
    }

    public EnumStatusSaqueBoleto getSituacao() {
        return this.situacao;
    }

    public SaqueBoleto situacao(EnumStatusSaqueBoleto situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusSaqueBoleto situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getValorSaque() {
        return this.valorSaque;
    }

    public SaqueBoleto valorSaque(BigDecimal valorSaque) {
        this.setValorSaque(valorSaque);
        return this;
    }

    public void setValorSaque(BigDecimal valorSaque) {
        this.valorSaque = valorSaque;
    }

    public BigDecimal getValorEnviado() {
        return this.valorEnviado;
    }

    public SaqueBoleto valorEnviado(BigDecimal valorEnviado) {
        this.setValorEnviado(valorEnviado);
        return this;
    }

    public void setValorEnviado(BigDecimal valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getCodigoBarras() {
        return this.codigoBarras;
    }

    public SaqueBoleto codigoBarras(String codigoBarras) {
        this.setCodigoBarras(codigoBarras);
        return this;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCampoLivre() {
        return this.campoLivre;
    }

    public SaqueBoleto campoLivre(String campoLivre) {
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

    public SaqueBoleto saque(Saque saque) {
        this.setSaque(saque);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public SaqueBoleto usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaqueBoleto)) {
            return false;
        }
        return getId() != null && getId().equals(((SaqueBoleto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaqueBoleto{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", valorSaque=" + getValorSaque() +
            ", valorEnviado=" + getValorEnviado() +
            ", codigoBarras='" + getCodigoBarras() + "'" +
            ", campoLivre='" + getCampoLivre() + "'" +
            "}";
    }
}
