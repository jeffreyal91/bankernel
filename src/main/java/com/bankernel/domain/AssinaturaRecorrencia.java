package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusAssinatura;
import com.bankernel.domain.enumeration.EnumTipoAssinatura;
import com.bankernel.domain.enumeration.EnumTipoPagamentoAssinatura;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssinaturaRecorrencia.
 */
@Entity
@Table(name = "cob_assinatura_recorrencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssinaturaRecorrencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "numero_ordem", length = 50)
    private String numeroOrdem;

    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50)
    private String numeroReferencia;

    @Size(max = 200)
    @Column(name = "devedor_email", length = 200)
    private String devedorEmail;

    @Size(max = 14)
    @Column(name = "devedor_documento", length = 14)
    private String devedorDocumento;

    @Size(max = 200)
    @Column(name = "devedor_nome", length = 200)
    private String devedorNome;

    @Size(max = 20)
    @Column(name = "devedor_telefone", length = 20)
    private String devedorTelefone;

    @Size(max = 8)
    @Column(name = "devedor_cep", length = 8)
    private String devedorCep;

    @Size(max = 2)
    @Column(name = "devedor_uf", length = 2)
    private String devedorUf;

    @Size(max = 100)
    @Column(name = "devedor_cidade", length = 100)
    private String devedorCidade;

    @Size(max = 100)
    @Column(name = "devedor_bairro", length = 100)
    private String devedorBairro;

    @Size(max = 200)
    @Column(name = "devedor_endereco", length = 200)
    private String devedorEndereco;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Size(max = 100)
    @Column(name = "id_produto_externo", length = 100)
    private String idProdutoExterno;

    @Size(max = 100)
    @Column(name = "identificador_externo", length = 100)
    private String identificadorExterno;

    @Size(max = 200)
    @Column(name = "nome_produto_externo", length = 200)
    private String nomeProdutoExterno;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoAssinatura tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento", nullable = false)
    private EnumTipoPagamentoAssinatura tipoPagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusAssinatura situacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "usuario", "linkCobranca" }, allowSetters = true)
    private PlanoRecorrencia planoRecorrencia;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuario", "moedaCarteira" }, allowSetters = true)
    private LinkCobranca linkCobranca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssinaturaRecorrencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrdem() {
        return this.numeroOrdem;
    }

    public AssinaturaRecorrencia numeroOrdem(String numeroOrdem) {
        this.setNumeroOrdem(numeroOrdem);
        return this;
    }

    public void setNumeroOrdem(String numeroOrdem) {
        this.numeroOrdem = numeroOrdem;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public AssinaturaRecorrencia numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getDevedorEmail() {
        return this.devedorEmail;
    }

    public AssinaturaRecorrencia devedorEmail(String devedorEmail) {
        this.setDevedorEmail(devedorEmail);
        return this;
    }

    public void setDevedorEmail(String devedorEmail) {
        this.devedorEmail = devedorEmail;
    }

    public String getDevedorDocumento() {
        return this.devedorDocumento;
    }

    public AssinaturaRecorrencia devedorDocumento(String devedorDocumento) {
        this.setDevedorDocumento(devedorDocumento);
        return this;
    }

    public void setDevedorDocumento(String devedorDocumento) {
        this.devedorDocumento = devedorDocumento;
    }

    public String getDevedorNome() {
        return this.devedorNome;
    }

    public AssinaturaRecorrencia devedorNome(String devedorNome) {
        this.setDevedorNome(devedorNome);
        return this;
    }

    public void setDevedorNome(String devedorNome) {
        this.devedorNome = devedorNome;
    }

    public String getDevedorTelefone() {
        return this.devedorTelefone;
    }

    public AssinaturaRecorrencia devedorTelefone(String devedorTelefone) {
        this.setDevedorTelefone(devedorTelefone);
        return this;
    }

    public void setDevedorTelefone(String devedorTelefone) {
        this.devedorTelefone = devedorTelefone;
    }

    public String getDevedorCep() {
        return this.devedorCep;
    }

    public AssinaturaRecorrencia devedorCep(String devedorCep) {
        this.setDevedorCep(devedorCep);
        return this;
    }

    public void setDevedorCep(String devedorCep) {
        this.devedorCep = devedorCep;
    }

    public String getDevedorUf() {
        return this.devedorUf;
    }

    public AssinaturaRecorrencia devedorUf(String devedorUf) {
        this.setDevedorUf(devedorUf);
        return this;
    }

    public void setDevedorUf(String devedorUf) {
        this.devedorUf = devedorUf;
    }

    public String getDevedorCidade() {
        return this.devedorCidade;
    }

    public AssinaturaRecorrencia devedorCidade(String devedorCidade) {
        this.setDevedorCidade(devedorCidade);
        return this;
    }

    public void setDevedorCidade(String devedorCidade) {
        this.devedorCidade = devedorCidade;
    }

    public String getDevedorBairro() {
        return this.devedorBairro;
    }

    public AssinaturaRecorrencia devedorBairro(String devedorBairro) {
        this.setDevedorBairro(devedorBairro);
        return this;
    }

    public void setDevedorBairro(String devedorBairro) {
        this.devedorBairro = devedorBairro;
    }

    public String getDevedorEndereco() {
        return this.devedorEndereco;
    }

    public AssinaturaRecorrencia devedorEndereco(String devedorEndereco) {
        this.setDevedorEndereco(devedorEndereco);
        return this;
    }

    public void setDevedorEndereco(String devedorEndereco) {
        this.devedorEndereco = devedorEndereco;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public AssinaturaRecorrencia ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getIdProdutoExterno() {
        return this.idProdutoExterno;
    }

    public AssinaturaRecorrencia idProdutoExterno(String idProdutoExterno) {
        this.setIdProdutoExterno(idProdutoExterno);
        return this;
    }

    public void setIdProdutoExterno(String idProdutoExterno) {
        this.idProdutoExterno = idProdutoExterno;
    }

    public String getIdentificadorExterno() {
        return this.identificadorExterno;
    }

    public AssinaturaRecorrencia identificadorExterno(String identificadorExterno) {
        this.setIdentificadorExterno(identificadorExterno);
        return this;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getNomeProdutoExterno() {
        return this.nomeProdutoExterno;
    }

    public AssinaturaRecorrencia nomeProdutoExterno(String nomeProdutoExterno) {
        this.setNomeProdutoExterno(nomeProdutoExterno);
        return this;
    }

    public void setNomeProdutoExterno(String nomeProdutoExterno) {
        this.nomeProdutoExterno = nomeProdutoExterno;
    }

    public EnumTipoAssinatura getTipo() {
        return this.tipo;
    }

    public AssinaturaRecorrencia tipo(EnumTipoAssinatura tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoAssinatura tipo) {
        this.tipo = tipo;
    }

    public EnumTipoPagamentoAssinatura getTipoPagamento() {
        return this.tipoPagamento;
    }

    public AssinaturaRecorrencia tipoPagamento(EnumTipoPagamentoAssinatura tipoPagamento) {
        this.setTipoPagamento(tipoPagamento);
        return this;
    }

    public void setTipoPagamento(EnumTipoPagamentoAssinatura tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public EnumStatusAssinatura getSituacao() {
        return this.situacao;
    }

    public AssinaturaRecorrencia situacao(EnumStatusAssinatura situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusAssinatura situacao) {
        this.situacao = situacao;
    }

    public PlanoRecorrencia getPlanoRecorrencia() {
        return this.planoRecorrencia;
    }

    public void setPlanoRecorrencia(PlanoRecorrencia planoRecorrencia) {
        this.planoRecorrencia = planoRecorrencia;
    }

    public AssinaturaRecorrencia planoRecorrencia(PlanoRecorrencia planoRecorrencia) {
        this.setPlanoRecorrencia(planoRecorrencia);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public AssinaturaRecorrencia usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public LinkCobranca getLinkCobranca() {
        return this.linkCobranca;
    }

    public void setLinkCobranca(LinkCobranca linkCobranca) {
        this.linkCobranca = linkCobranca;
    }

    public AssinaturaRecorrencia linkCobranca(LinkCobranca linkCobranca) {
        this.setLinkCobranca(linkCobranca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssinaturaRecorrencia)) {
            return false;
        }
        return getId() != null && getId().equals(((AssinaturaRecorrencia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssinaturaRecorrencia{" +
            "id=" + getId() +
            ", numeroOrdem='" + getNumeroOrdem() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", devedorEmail='" + getDevedorEmail() + "'" +
            ", devedorDocumento='" + getDevedorDocumento() + "'" +
            ", devedorNome='" + getDevedorNome() + "'" +
            ", devedorTelefone='" + getDevedorTelefone() + "'" +
            ", devedorCep='" + getDevedorCep() + "'" +
            ", devedorUf='" + getDevedorUf() + "'" +
            ", devedorCidade='" + getDevedorCidade() + "'" +
            ", devedorBairro='" + getDevedorBairro() + "'" +
            ", devedorEndereco='" + getDevedorEndereco() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", idProdutoExterno='" + getIdProdutoExterno() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", nomeProdutoExterno='" + getNomeProdutoExterno() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", tipoPagamento='" + getTipoPagamento() + "'" +
            ", situacao='" + getSituacao() + "'" +
            "}";
    }
}
