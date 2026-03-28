package com.bankernel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MoedaCarteira.
 */
@Entity
@Table(name = "car_moeda_carteira")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoedaCarteira implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @NotNull
    @Size(max = 50)
    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Size(max = 200)
    @Column(name = "descricao", length = 200)
    private String descricao;

    @NotNull
    @Column(name = "fator_conversao", precision = 21, scale = 2, nullable = false)
    private BigDecimal fatorConversao;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    @NotNull
    @Column(name = "casas_decimais", nullable = false)
    private Integer casasDecimais;

    @Size(max = 1)
    @Column(name = "separador_milhar", length = 1)
    private String separadorMilhar;

    @Size(max = 1)
    @Column(name = "separador_decimal", length = 1)
    private String separadorDecimal;

    @Size(max = 5)
    @Column(name = "simbolo", length = 5)
    private String simbolo;

    @ManyToOne(optional = false)
    @NotNull
    private Moeda moeda;

    @JsonIgnoreProperties(value = { "usuario", "moedaPrincipal", "nacionalidade", "profissao", "plano", "escritorio" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "moedaPrincipal")
    private PessoaFisica pessoaFisica;

    @JsonIgnoreProperties(
        value = { "usuario", "moedaPrincipal", "contratoSocial", "nacionalidade", "tipoNegocio", "plano", "escritorio" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "moedaPrincipal")
    private PessoaJuridica pessoaJuridica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoedaCarteira id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public MoedaCarteira codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public MoedaCarteira nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public MoedaCarteira descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getFatorConversao() {
        return this.fatorConversao;
    }

    public MoedaCarteira fatorConversao(BigDecimal fatorConversao) {
        this.setFatorConversao(fatorConversao);
        return this;
    }

    public void setFatorConversao(BigDecimal fatorConversao) {
        this.fatorConversao = fatorConversao;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public MoedaCarteira ativa(Boolean ativa) {
        this.setAtiva(ativa);
        return this;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Boolean getPrincipal() {
        return this.principal;
    }

    public MoedaCarteira principal(Boolean principal) {
        this.setPrincipal(principal);
        return this;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Integer getCasasDecimais() {
        return this.casasDecimais;
    }

    public MoedaCarteira casasDecimais(Integer casasDecimais) {
        this.setCasasDecimais(casasDecimais);
        return this;
    }

    public void setCasasDecimais(Integer casasDecimais) {
        this.casasDecimais = casasDecimais;
    }

    public String getSeparadorMilhar() {
        return this.separadorMilhar;
    }

    public MoedaCarteira separadorMilhar(String separadorMilhar) {
        this.setSeparadorMilhar(separadorMilhar);
        return this;
    }

    public void setSeparadorMilhar(String separadorMilhar) {
        this.separadorMilhar = separadorMilhar;
    }

    public String getSeparadorDecimal() {
        return this.separadorDecimal;
    }

    public MoedaCarteira separadorDecimal(String separadorDecimal) {
        this.setSeparadorDecimal(separadorDecimal);
        return this;
    }

    public void setSeparadorDecimal(String separadorDecimal) {
        this.separadorDecimal = separadorDecimal;
    }

    public String getSimbolo() {
        return this.simbolo;
    }

    public MoedaCarteira simbolo(String simbolo) {
        this.setSimbolo(simbolo);
        return this;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(Moeda moeda) {
        this.moeda = moeda;
    }

    public MoedaCarteira moeda(Moeda moeda) {
        this.setMoeda(moeda);
        return this;
    }

    public PessoaFisica getPessoaFisica() {
        return this.pessoaFisica;
    }

    public void setPessoaFisica(PessoaFisica pessoaFisica) {
        if (this.pessoaFisica != null) {
            this.pessoaFisica.setMoedaPrincipal(null);
        }
        if (pessoaFisica != null) {
            pessoaFisica.setMoedaPrincipal(this);
        }
        this.pessoaFisica = pessoaFisica;
    }

    public MoedaCarteira pessoaFisica(PessoaFisica pessoaFisica) {
        this.setPessoaFisica(pessoaFisica);
        return this;
    }

    public PessoaJuridica getPessoaJuridica() {
        return this.pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        if (this.pessoaJuridica != null) {
            this.pessoaJuridica.setMoedaPrincipal(null);
        }
        if (pessoaJuridica != null) {
            pessoaJuridica.setMoedaPrincipal(this);
        }
        this.pessoaJuridica = pessoaJuridica;
    }

    public MoedaCarteira pessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.setPessoaJuridica(pessoaJuridica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoedaCarteira)) {
            return false;
        }
        return getId() != null && getId().equals(((MoedaCarteira) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoedaCarteira{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", fatorConversao=" + getFatorConversao() +
            ", ativa='" + getAtiva() + "'" +
            ", principal='" + getPrincipal() + "'" +
            ", casasDecimais=" + getCasasDecimais() +
            ", separadorMilhar='" + getSeparadorMilhar() + "'" +
            ", separadorDecimal='" + getSeparadorDecimal() + "'" +
            ", simbolo='" + getSimbolo() + "'" +
            "}";
    }
}
