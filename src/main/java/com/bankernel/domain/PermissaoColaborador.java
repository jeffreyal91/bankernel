package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumTipoPermissao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PermissaoColaborador.
 */
@Entity
@Table(name = "pes_permissao_colaborador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissaoColaborador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_permissao", nullable = false)
    private EnumTipoPermissao tipoPermissao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "usuario", "pessoaJuridica" }, allowSetters = true)
    private ColaboradorPJ colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PermissaoColaborador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoPermissao getTipoPermissao() {
        return this.tipoPermissao;
    }

    public PermissaoColaborador tipoPermissao(EnumTipoPermissao tipoPermissao) {
        this.setTipoPermissao(tipoPermissao);
        return this;
    }

    public void setTipoPermissao(EnumTipoPermissao tipoPermissao) {
        this.tipoPermissao = tipoPermissao;
    }

    public ColaboradorPJ getColaborador() {
        return this.colaborador;
    }

    public void setColaborador(ColaboradorPJ colaboradorPJ) {
        this.colaborador = colaboradorPJ;
    }

    public PermissaoColaborador colaborador(ColaboradorPJ colaboradorPJ) {
        this.setColaborador(colaboradorPJ);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public PermissaoColaborador carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissaoColaborador)) {
            return false;
        }
        return getId() != null && getId().equals(((PermissaoColaborador) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissaoColaborador{" +
            "id=" + getId() +
            ", tipoPermissao='" + getTipoPermissao() + "'" +
            "}";
    }
}
