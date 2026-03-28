package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumCodigoMoeda;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Moeda.
 */
@Entity
@Table(name = "moe_moeda")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Moeda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "codigo_moeda", nullable = false)
    private EnumCodigoMoeda codigoMoeda;

    @NotNull
    @Size(max = 50)
    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Size(max = 200)
    @Column(name = "descricao", length = 200)
    private String descricao;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Moeda id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumCodigoMoeda getCodigoMoeda() {
        return this.codigoMoeda;
    }

    public Moeda codigoMoeda(EnumCodigoMoeda codigoMoeda) {
        this.setCodigoMoeda(codigoMoeda);
        return this;
    }

    public void setCodigoMoeda(EnumCodigoMoeda codigoMoeda) {
        this.codigoMoeda = codigoMoeda;
    }

    public String getNome() {
        return this.nome;
    }

    public Moeda nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Moeda descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public Moeda ativa(Boolean ativa) {
        this.setAtiva(ativa);
        return this;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Moeda)) {
            return false;
        }
        return getId() != null && getId().equals(((Moeda) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Moeda{" +
            "id=" + getId() +
            ", codigoMoeda='" + getCodigoMoeda() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
