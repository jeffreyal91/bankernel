package com.bankernel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ColaboradorPJ.
 */
@Entity
@Table(name = "pes_colaborador_pj")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ColaboradorPJ implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Size(max = 100)
    @Column(name = "departamento", length = 100)
    private String departamento;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "usuario", "moedaPrincipal", "contratoSocial", "nacionalidade", "tipoNegocio", "plano", "escritorio" },
        allowSetters = true
    )
    private PessoaJuridica pessoaJuridica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ColaboradorPJ id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public ColaboradorPJ ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getDepartamento() {
        return this.departamento;
    }

    public ColaboradorPJ departamento(String departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public ColaboradorPJ usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public PessoaJuridica getPessoaJuridica() {
        return this.pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public ColaboradorPJ pessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.setPessoaJuridica(pessoaJuridica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColaboradorPJ)) {
            return false;
        }
        return getId() != null && getId().equals(((ColaboradorPJ) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ColaboradorPJ{" +
            "id=" + getId() +
            ", ativo='" + getAtivo() + "'" +
            ", departamento='" + getDepartamento() + "'" +
            "}";
    }
}
