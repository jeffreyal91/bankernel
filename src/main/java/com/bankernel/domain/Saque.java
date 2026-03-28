package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusSaque;
import com.bankernel.domain.enumeration.EnumTipoSaque;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Saque.
 */
@Entity
@Table(name = "saq_saque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Saque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valor_saque", precision = 21, scale = 2, nullable = false)
    private BigDecimal valorSaque;

    @Column(name = "valor_enviado", precision = 21, scale = 2)
    private BigDecimal valorEnviado;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_saque", nullable = false)
    private EnumTipoSaque tipoSaque;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_saque", nullable = false)
    private EnumStatusSaque situacaoSaque;

    @NotNull
    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50, nullable = false)
    private String numeroReferencia;

    @Size(max = 500)
    @Column(name = "motivo_rejeicao", length = 500)
    private String motivoRejeicao;

    @NotNull
    @Column(name = "contabilizado", nullable = false)
    private Boolean contabilizado;

    @Size(max = 200)
    @Column(name = "nome_usuario_fixo", length = 200)
    private String nomeUsuarioFixo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacaoEstorno;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteira;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuario", "pais", "moeda" }, allowSetters = true)
    private ContaBancaria contaBancariaDestino;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    private Escritorio escritorio;

    @JsonIgnoreProperties(value = { "saque", "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "saque")
    private SaquePix saquePix;

    @JsonIgnoreProperties(value = { "saque", "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "saque")
    private SaqueBoleto saqueBoleto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Saque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorSaque() {
        return this.valorSaque;
    }

    public Saque valorSaque(BigDecimal valorSaque) {
        this.setValorSaque(valorSaque);
        return this;
    }

    public void setValorSaque(BigDecimal valorSaque) {
        this.valorSaque = valorSaque;
    }

    public BigDecimal getValorEnviado() {
        return this.valorEnviado;
    }

    public Saque valorEnviado(BigDecimal valorEnviado) {
        this.setValorEnviado(valorEnviado);
        return this;
    }

    public void setValorEnviado(BigDecimal valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Saque descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoSaque getTipoSaque() {
        return this.tipoSaque;
    }

    public Saque tipoSaque(EnumTipoSaque tipoSaque) {
        this.setTipoSaque(tipoSaque);
        return this;
    }

    public void setTipoSaque(EnumTipoSaque tipoSaque) {
        this.tipoSaque = tipoSaque;
    }

    public EnumStatusSaque getSituacaoSaque() {
        return this.situacaoSaque;
    }

    public Saque situacaoSaque(EnumStatusSaque situacaoSaque) {
        this.setSituacaoSaque(situacaoSaque);
        return this;
    }

    public void setSituacaoSaque(EnumStatusSaque situacaoSaque) {
        this.situacaoSaque = situacaoSaque;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public Saque numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getMotivoRejeicao() {
        return this.motivoRejeicao;
    }

    public Saque motivoRejeicao(String motivoRejeicao) {
        this.setMotivoRejeicao(motivoRejeicao);
        return this;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public Boolean getContabilizado() {
        return this.contabilizado;
    }

    public Saque contabilizado(Boolean contabilizado) {
        this.setContabilizado(contabilizado);
        return this;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public String getNomeUsuarioFixo() {
        return this.nomeUsuarioFixo;
    }

    public Saque nomeUsuarioFixo(String nomeUsuarioFixo) {
        this.setNomeUsuarioFixo(nomeUsuarioFixo);
        return this;
    }

    public void setNomeUsuarioFixo(String nomeUsuarioFixo) {
        this.nomeUsuarioFixo = nomeUsuarioFixo;
    }

    public Transacao getTransacao() {
        return this.transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Saque transacao(Transacao transacao) {
        this.setTransacao(transacao);
        return this;
    }

    public Transacao getTransacaoEstorno() {
        return this.transacaoEstorno;
    }

    public void setTransacaoEstorno(Transacao transacao) {
        this.transacaoEstorno = transacao;
    }

    public Saque transacaoEstorno(Transacao transacao) {
        this.setTransacaoEstorno(transacao);
        return this;
    }

    public Carteira getCarteira() {
        return this.carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public Saque carteira(Carteira carteira) {
        this.setCarteira(carteira);
        return this;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public Saque moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    public ContaBancaria getContaBancariaDestino() {
        return this.contaBancariaDestino;
    }

    public void setContaBancariaDestino(ContaBancaria contaBancaria) {
        this.contaBancariaDestino = contaBancaria;
    }

    public Saque contaBancariaDestino(ContaBancaria contaBancaria) {
        this.setContaBancariaDestino(contaBancaria);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Saque usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public Escritorio getEscritorio() {
        return this.escritorio;
    }

    public void setEscritorio(Escritorio escritorio) {
        this.escritorio = escritorio;
    }

    public Saque escritorio(Escritorio escritorio) {
        this.setEscritorio(escritorio);
        return this;
    }

    public SaquePix getSaquePix() {
        return this.saquePix;
    }

    public void setSaquePix(SaquePix saquePix) {
        if (this.saquePix != null) {
            this.saquePix.setSaque(null);
        }
        if (saquePix != null) {
            saquePix.setSaque(this);
        }
        this.saquePix = saquePix;
    }

    public Saque saquePix(SaquePix saquePix) {
        this.setSaquePix(saquePix);
        return this;
    }

    public SaqueBoleto getSaqueBoleto() {
        return this.saqueBoleto;
    }

    public void setSaqueBoleto(SaqueBoleto saqueBoleto) {
        if (this.saqueBoleto != null) {
            this.saqueBoleto.setSaque(null);
        }
        if (saqueBoleto != null) {
            saqueBoleto.setSaque(this);
        }
        this.saqueBoleto = saqueBoleto;
    }

    public Saque saqueBoleto(SaqueBoleto saqueBoleto) {
        this.setSaqueBoleto(saqueBoleto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Saque)) {
            return false;
        }
        return getId() != null && getId().equals(((Saque) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Saque{" +
            "id=" + getId() +
            ", valorSaque=" + getValorSaque() +
            ", valorEnviado=" + getValorEnviado() +
            ", descricao='" + getDescricao() + "'" +
            ", tipoSaque='" + getTipoSaque() + "'" +
            ", situacaoSaque='" + getSituacaoSaque() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", motivoRejeicao='" + getMotivoRejeicao() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            ", nomeUsuarioFixo='" + getNomeUsuarioFixo() + "'" +
            "}";
    }
}
