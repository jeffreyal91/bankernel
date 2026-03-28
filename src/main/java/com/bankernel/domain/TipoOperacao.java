package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumSinalContabil;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TipoOperacao.
 */
@Entity
@Table(name = "mov_tipo_operacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoOperacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "codigo", nullable = false)
    private EnumTipoOperacao codigo;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sinal_credito", nullable = false)
    private EnumSinalContabil sinalCredito;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sinal_debito", nullable = false)
    private EnumSinalContabil sinalDebito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira" }, allowSetters = true)
    private ContaContabil contaCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira" }, allowSetters = true)
    private ContaContabil contaDebito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoOperacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoOperacao getCodigo() {
        return this.codigo;
    }

    public TipoOperacao codigo(EnumTipoOperacao codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(EnumTipoOperacao codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoOperacao nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TipoOperacao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public TipoOperacao ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnumSinalContabil getSinalCredito() {
        return this.sinalCredito;
    }

    public TipoOperacao sinalCredito(EnumSinalContabil sinalCredito) {
        this.setSinalCredito(sinalCredito);
        return this;
    }

    public void setSinalCredito(EnumSinalContabil sinalCredito) {
        this.sinalCredito = sinalCredito;
    }

    public EnumSinalContabil getSinalDebito() {
        return this.sinalDebito;
    }

    public TipoOperacao sinalDebito(EnumSinalContabil sinalDebito) {
        this.setSinalDebito(sinalDebito);
        return this;
    }

    public void setSinalDebito(EnumSinalContabil sinalDebito) {
        this.sinalDebito = sinalDebito;
    }

    public ContaContabil getContaCredito() {
        return this.contaCredito;
    }

    public void setContaCredito(ContaContabil contaContabil) {
        this.contaCredito = contaContabil;
    }

    public TipoOperacao contaCredito(ContaContabil contaContabil) {
        this.setContaCredito(contaContabil);
        return this;
    }

    public ContaContabil getContaDebito() {
        return this.contaDebito;
    }

    public void setContaDebito(ContaContabil contaContabil) {
        this.contaDebito = contaContabil;
    }

    public TipoOperacao contaDebito(ContaContabil contaContabil) {
        this.setContaDebito(contaContabil);
        return this;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public TipoOperacao moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoOperacao)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoOperacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoOperacao{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", sinalCredito='" + getSinalCredito() + "'" +
            ", sinalDebito='" + getSinalDebito() + "'" +
            "}";
    }
}
