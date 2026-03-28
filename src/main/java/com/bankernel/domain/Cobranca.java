package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusCobranca;
import com.bankernel.domain.enumeration.EnumTipoCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cobranca.
 */
@Entity
@Table(name = "cob_cobranca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cobranca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "valor_creditado", precision = 21, scale = 2)
    private BigDecimal valorCreditado;

    @Column(name = "valor_creditado_carteira", precision = 21, scale = 2)
    private BigDecimal valorCreditadoCarteira;

    @Size(max = 100)
    @Column(name = "id_produto_externo", length = 100)
    private String idProdutoExterno;

    @Size(max = 200)
    @Column(name = "nome_produto_externo", length = 200)
    private String nomeProdutoExterno;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusCobranca situacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoCobranca tipo;

    @Column(name = "desconto_geral", precision = 21, scale = 2)
    private BigDecimal descontoGeral;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_desconto")
    private EnumTipoDesconto tipoDesconto;

    @NotNull
    @Column(name = "contabilizado", nullable = false)
    private Boolean contabilizado;

    @Size(max = 200)
    @Column(name = "nome_usuario_fixo", length = 200)
    private String nomeUsuarioFixo;

    @Size(max = 100)
    @Column(name = "chave_cobranca", length = 100)
    private String chaveCobranca;

    @Size(max = 100)
    @Column(name = "identificador_externo", length = 100)
    private String identificadorExterno;

    @NotNull
    @Column(name = "retorno_notificado", nullable = false)
    private Boolean retornoNotificado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacao;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteira;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteiraCreditada;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuario", "moedaCarteira" }, allowSetters = true)
    private LinkCobranca linkCobranca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cobranca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Cobranca valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorCreditado() {
        return this.valorCreditado;
    }

    public Cobranca valorCreditado(BigDecimal valorCreditado) {
        this.setValorCreditado(valorCreditado);
        return this;
    }

    public void setValorCreditado(BigDecimal valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimal getValorCreditadoCarteira() {
        return this.valorCreditadoCarteira;
    }

    public Cobranca valorCreditadoCarteira(BigDecimal valorCreditadoCarteira) {
        this.setValorCreditadoCarteira(valorCreditadoCarteira);
        return this;
    }

    public void setValorCreditadoCarteira(BigDecimal valorCreditadoCarteira) {
        this.valorCreditadoCarteira = valorCreditadoCarteira;
    }

    public String getIdProdutoExterno() {
        return this.idProdutoExterno;
    }

    public Cobranca idProdutoExterno(String idProdutoExterno) {
        this.setIdProdutoExterno(idProdutoExterno);
        return this;
    }

    public void setIdProdutoExterno(String idProdutoExterno) {
        this.idProdutoExterno = idProdutoExterno;
    }

    public String getNomeProdutoExterno() {
        return this.nomeProdutoExterno;
    }

    public Cobranca nomeProdutoExterno(String nomeProdutoExterno) {
        this.setNomeProdutoExterno(nomeProdutoExterno);
        return this;
    }

    public void setNomeProdutoExterno(String nomeProdutoExterno) {
        this.nomeProdutoExterno = nomeProdutoExterno;
    }

    public EnumStatusCobranca getSituacao() {
        return this.situacao;
    }

    public Cobranca situacao(EnumStatusCobranca situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusCobranca situacao) {
        this.situacao = situacao;
    }

    public EnumTipoCobranca getTipo() {
        return this.tipo;
    }

    public Cobranca tipo(EnumTipoCobranca tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoCobranca tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getDescontoGeral() {
        return this.descontoGeral;
    }

    public Cobranca descontoGeral(BigDecimal descontoGeral) {
        this.setDescontoGeral(descontoGeral);
        return this;
    }

    public void setDescontoGeral(BigDecimal descontoGeral) {
        this.descontoGeral = descontoGeral;
    }

    public EnumTipoDesconto getTipoDesconto() {
        return this.tipoDesconto;
    }

    public Cobranca tipoDesconto(EnumTipoDesconto tipoDesconto) {
        this.setTipoDesconto(tipoDesconto);
        return this;
    }

    public void setTipoDesconto(EnumTipoDesconto tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public Boolean getContabilizado() {
        return this.contabilizado;
    }

    public Cobranca contabilizado(Boolean contabilizado) {
        this.setContabilizado(contabilizado);
        return this;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public String getNomeUsuarioFixo() {
        return this.nomeUsuarioFixo;
    }

    public Cobranca nomeUsuarioFixo(String nomeUsuarioFixo) {
        this.setNomeUsuarioFixo(nomeUsuarioFixo);
        return this;
    }

    public void setNomeUsuarioFixo(String nomeUsuarioFixo) {
        this.nomeUsuarioFixo = nomeUsuarioFixo;
    }

    public String getChaveCobranca() {
        return this.chaveCobranca;
    }

    public Cobranca chaveCobranca(String chaveCobranca) {
        this.setChaveCobranca(chaveCobranca);
        return this;
    }

    public void setChaveCobranca(String chaveCobranca) {
        this.chaveCobranca = chaveCobranca;
    }

    public String getIdentificadorExterno() {
        return this.identificadorExterno;
    }

    public Cobranca identificadorExterno(String identificadorExterno) {
        this.setIdentificadorExterno(identificadorExterno);
        return this;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public Boolean getRetornoNotificado() {
        return this.retornoNotificado;
    }

    public Cobranca retornoNotificado(Boolean retornoNotificado) {
        this.setRetornoNotificado(retornoNotificado);
        return this;
    }

    public void setRetornoNotificado(Boolean retornoNotificado) {
        this.retornoNotificado = retornoNotificado;
    }

    public Transacao getTransacao() {
        return this.transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Cobranca transacao(Transacao transacao) {
        this.setTransacao(transacao);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Cobranca usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public Cobranca carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    public Carteira getCarteiraCreditada() {
        return this.carteiraCreditada;
    }

    public void setCarteiraCreditada(Carteira carteira) {
        this.carteiraCreditada = carteira;
    }

    public Cobranca carteiraCreditada(Carteira carteira) {
        this.setCarteiraCreditada(carteira);
        return this;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public Cobranca moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    public LinkCobranca getLinkCobranca() {
        return this.linkCobranca;
    }

    public void setLinkCobranca(LinkCobranca linkCobranca) {
        this.linkCobranca = linkCobranca;
    }

    public Cobranca linkCobranca(LinkCobranca linkCobranca) {
        this.setLinkCobranca(linkCobranca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cobranca)) {
            return false;
        }
        return getId() != null && getId().equals(((Cobranca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cobranca{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorCreditadoCarteira=" + getValorCreditadoCarteira() +
            ", idProdutoExterno='" + getIdProdutoExterno() + "'" +
            ", nomeProdutoExterno='" + getNomeProdutoExterno() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", descontoGeral=" + getDescontoGeral() +
            ", tipoDesconto='" + getTipoDesconto() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            ", nomeUsuarioFixo='" + getNomeUsuarioFixo() + "'" +
            ", chaveCobranca='" + getChaveCobranca() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", retornoNotificado='" + getRetornoNotificado() + "'" +
            "}";
    }
}
