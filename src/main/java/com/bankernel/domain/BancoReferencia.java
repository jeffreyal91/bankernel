package com.bankernel.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BancoReferencia.
 */
@Entity
@Table(name = "ref_banco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BancoReferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "codigo", length = 10, nullable = false)
    private String codigo;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Size(max = 8)
    @Column(name = "ispb", length = 8)
    private String ispb;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BancoReferencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public BancoReferencia codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public BancoReferencia nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIspb() {
        return this.ispb;
    }

    public BancoReferencia ispb(String ispb) {
        this.setIspb(ispb);
        return this;
    }

    public void setIspb(String ispb) {
        this.ispb = ispb;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public BancoReferencia ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BancoReferencia)) {
            return false;
        }
        return getId() != null && getId().equals(((BancoReferencia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BancoReferencia{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", ispb='" + getIspb() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
