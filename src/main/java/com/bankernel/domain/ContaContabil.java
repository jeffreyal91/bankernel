package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumCategoriaContaContabil;
import com.bankernel.domain.enumeration.EnumTipoContaContabil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContaContabil.
 */
@Entity
@Table(name = "ctb_conta_contabil")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaContabil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @NotNull
    @Column(name = "saldo", precision = 21, scale = 2, nullable = false)
    private BigDecimal saldo;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta_contabil", nullable = false)
    private EnumTipoContaContabil tipoContaContabil;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_conta_contabil", nullable = false)
    private EnumCategoriaContaContabil categoriaContaContabil;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContaContabil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public ContaContabil codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public ContaContabil nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public ContaContabil saldo(BigDecimal saldo) {
        this.setSaldo(saldo);
        return this;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ContaContabil descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoContaContabil getTipoContaContabil() {
        return this.tipoContaContabil;
    }

    public ContaContabil tipoContaContabil(EnumTipoContaContabil tipoContaContabil) {
        this.setTipoContaContabil(tipoContaContabil);
        return this;
    }

    public void setTipoContaContabil(EnumTipoContaContabil tipoContaContabil) {
        this.tipoContaContabil = tipoContaContabil;
    }

    public EnumCategoriaContaContabil getCategoriaContaContabil() {
        return this.categoriaContaContabil;
    }

    public ContaContabil categoriaContaContabil(EnumCategoriaContaContabil categoriaContaContabil) {
        this.setCategoriaContaContabil(categoriaContaContabil);
        return this;
    }

    public void setCategoriaContaContabil(EnumCategoriaContaContabil categoriaContaContabil) {
        this.categoriaContaContabil = categoriaContaContabil;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public ContaContabil ativa(Boolean ativa) {
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

    public ContaContabil moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaContabil)) {
            return false;
        }
        return getId() != null && getId().equals(((ContaContabil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaContabil{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", saldo=" + getSaldo() +
            ", descricao='" + getDescricao() + "'" +
            ", tipoContaContabil='" + getTipoContaContabil() + "'" +
            ", categoriaContaContabil='" + getCategoriaContaContabil() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
