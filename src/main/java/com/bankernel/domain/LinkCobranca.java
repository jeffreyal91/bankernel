package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusLinkCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.bankernel.domain.enumeration.EnumTipoLinkCobranca;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LinkCobranca.
 */
@Entity
@Table(name = "cob_link_cobranca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LinkCobranca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @NotNull
    @Size(max = 100)
    @Column(name = "chave_acesso", length = 100, nullable = false)
    private String chaveAcesso;

    @Size(max = 100)
    @Column(name = "chave_api", length = 100)
    private String chaveApi;

    @Size(max = 2000)
    @Column(name = "url_retorno", length = 2000)
    private String urlRetorno;

    @Size(max = 2000)
    @Column(name = "url_pagamento_aprovado", length = 2000)
    private String urlPagamentoAprovado;

    @Size(max = 2000)
    @Column(name = "url_pagamento_cancelado", length = 2000)
    private String urlPagamentoCancelado;

    @Size(max = 2000)
    @Column(name = "url_pagamento_rejeitado", length = 2000)
    private String urlPagamentoRejeitado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoLinkCobranca tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusLinkCobranca situacao;

    @Column(name = "data_inicio")
    private Instant dataInicio;

    @Column(name = "data_fim")
    private Instant dataFim;

    @Column(name = "desconto", precision = 21, scale = 2)
    private BigDecimal desconto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_desconto")
    private EnumTipoDesconto tipoDesconto;

    @Column(name = "valor_minimo_pagamento", precision = 21, scale = 2)
    private BigDecimal valorMinimoPagamento;

    @Size(max = 100)
    @Column(name = "identificador_externo", length = 100)
    private String identificadorExterno;

    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50)
    private String numeroReferencia;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @NotNull
    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LinkCobranca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public LinkCobranca nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChaveAcesso() {
        return this.chaveAcesso;
    }

    public LinkCobranca chaveAcesso(String chaveAcesso) {
        this.setChaveAcesso(chaveAcesso);
        return this;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public String getChaveApi() {
        return this.chaveApi;
    }

    public LinkCobranca chaveApi(String chaveApi) {
        this.setChaveApi(chaveApi);
        return this;
    }

    public void setChaveApi(String chaveApi) {
        this.chaveApi = chaveApi;
    }

    public String getUrlRetorno() {
        return this.urlRetorno;
    }

    public LinkCobranca urlRetorno(String urlRetorno) {
        this.setUrlRetorno(urlRetorno);
        return this;
    }

    public void setUrlRetorno(String urlRetorno) {
        this.urlRetorno = urlRetorno;
    }

    public String getUrlPagamentoAprovado() {
        return this.urlPagamentoAprovado;
    }

    public LinkCobranca urlPagamentoAprovado(String urlPagamentoAprovado) {
        this.setUrlPagamentoAprovado(urlPagamentoAprovado);
        return this;
    }

    public void setUrlPagamentoAprovado(String urlPagamentoAprovado) {
        this.urlPagamentoAprovado = urlPagamentoAprovado;
    }

    public String getUrlPagamentoCancelado() {
        return this.urlPagamentoCancelado;
    }

    public LinkCobranca urlPagamentoCancelado(String urlPagamentoCancelado) {
        this.setUrlPagamentoCancelado(urlPagamentoCancelado);
        return this;
    }

    public void setUrlPagamentoCancelado(String urlPagamentoCancelado) {
        this.urlPagamentoCancelado = urlPagamentoCancelado;
    }

    public String getUrlPagamentoRejeitado() {
        return this.urlPagamentoRejeitado;
    }

    public LinkCobranca urlPagamentoRejeitado(String urlPagamentoRejeitado) {
        this.setUrlPagamentoRejeitado(urlPagamentoRejeitado);
        return this;
    }

    public void setUrlPagamentoRejeitado(String urlPagamentoRejeitado) {
        this.urlPagamentoRejeitado = urlPagamentoRejeitado;
    }

    public EnumTipoLinkCobranca getTipo() {
        return this.tipo;
    }

    public LinkCobranca tipo(EnumTipoLinkCobranca tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoLinkCobranca tipo) {
        this.tipo = tipo;
    }

    public EnumStatusLinkCobranca getSituacao() {
        return this.situacao;
    }

    public LinkCobranca situacao(EnumStatusLinkCobranca situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusLinkCobranca situacao) {
        this.situacao = situacao;
    }

    public Instant getDataInicio() {
        return this.dataInicio;
    }

    public LinkCobranca dataInicio(Instant dataInicio) {
        this.setDataInicio(dataInicio);
        return this;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return this.dataFim;
    }

    public LinkCobranca dataFim(Instant dataFim) {
        this.setDataFim(dataFim);
        return this;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getDesconto() {
        return this.desconto;
    }

    public LinkCobranca desconto(BigDecimal desconto) {
        this.setDesconto(desconto);
        return this;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public EnumTipoDesconto getTipoDesconto() {
        return this.tipoDesconto;
    }

    public LinkCobranca tipoDesconto(EnumTipoDesconto tipoDesconto) {
        this.setTipoDesconto(tipoDesconto);
        return this;
    }

    public void setTipoDesconto(EnumTipoDesconto tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public BigDecimal getValorMinimoPagamento() {
        return this.valorMinimoPagamento;
    }

    public LinkCobranca valorMinimoPagamento(BigDecimal valorMinimoPagamento) {
        this.setValorMinimoPagamento(valorMinimoPagamento);
        return this;
    }

    public void setValorMinimoPagamento(BigDecimal valorMinimoPagamento) {
        this.valorMinimoPagamento = valorMinimoPagamento;
    }

    public String getIdentificadorExterno() {
        return this.identificadorExterno;
    }

    public LinkCobranca identificadorExterno(String identificadorExterno) {
        this.setIdentificadorExterno(identificadorExterno);
        return this;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public LinkCobranca numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public LinkCobranca ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getBloqueado() {
        return this.bloqueado;
    }

    public LinkCobranca bloqueado(Boolean bloqueado) {
        this.setBloqueado(bloqueado);
        return this;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public LinkCobranca usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public LinkCobranca moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinkCobranca)) {
            return false;
        }
        return getId() != null && getId().equals(((LinkCobranca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LinkCobranca{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", chaveAcesso='" + getChaveAcesso() + "'" +
            ", chaveApi='" + getChaveApi() + "'" +
            ", urlRetorno='" + getUrlRetorno() + "'" +
            ", urlPagamentoAprovado='" + getUrlPagamentoAprovado() + "'" +
            ", urlPagamentoCancelado='" + getUrlPagamentoCancelado() + "'" +
            ", urlPagamentoRejeitado='" + getUrlPagamentoRejeitado() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            ", desconto=" + getDesconto() +
            ", tipoDesconto='" + getTipoDesconto() + "'" +
            ", valorMinimoPagamento=" + getValorMinimoPagamento() +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", bloqueado='" + getBloqueado() + "'" +
            "}";
    }
}
