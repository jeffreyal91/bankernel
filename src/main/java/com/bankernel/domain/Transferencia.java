package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumStatusTransferencia;
import com.bankernel.domain.enumeration.EnumTipoChaveTransferencia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transferencia.
 */
@Entity
@Table(name = "trf_transferencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @Size(max = 100)
    @Column(name = "chave_interna", length = 100)
    private String chaveInterna;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_chave", nullable = false)
    private EnumTipoChaveTransferencia tipoChave;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusTransferencia situacao;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @Size(max = 500)
    @Column(name = "motivo_rejeicao", length = 500)
    private String motivoRejeicao;

    @NotNull
    @Size(max = 50)
    @Column(name = "numero_referencia", length = 50, nullable = false)
    private String numeroReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "carteiraOrigem", "carteiraDestino", "moedaOrigem", "moedaDestino" }, allowSetters = true)
    private Transacao transacao;

    @ManyToOne(optional = false)
    @NotNull
    private User usuarioOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    private User usuarioDestino;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteiraOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "moedaCarteira", "usuario" }, allowSetters = true)
    private Carteira carteiraDestino;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    private MoedaCarteira moedaCarteira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transferencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Transferencia valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getChaveInterna() {
        return this.chaveInterna;
    }

    public Transferencia chaveInterna(String chaveInterna) {
        this.setChaveInterna(chaveInterna);
        return this;
    }

    public void setChaveInterna(String chaveInterna) {
        this.chaveInterna = chaveInterna;
    }

    public EnumTipoChaveTransferencia getTipoChave() {
        return this.tipoChave;
    }

    public Transferencia tipoChave(EnumTipoChaveTransferencia tipoChave) {
        this.setTipoChave(tipoChave);
        return this;
    }

    public void setTipoChave(EnumTipoChaveTransferencia tipoChave) {
        this.tipoChave = tipoChave;
    }

    public EnumStatusTransferencia getSituacao() {
        return this.situacao;
    }

    public Transferencia situacao(EnumStatusTransferencia situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusTransferencia situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Transferencia descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMotivoRejeicao() {
        return this.motivoRejeicao;
    }

    public Transferencia motivoRejeicao(String motivoRejeicao) {
        this.setMotivoRejeicao(motivoRejeicao);
        return this;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public String getNumeroReferencia() {
        return this.numeroReferencia;
    }

    public Transferencia numeroReferencia(String numeroReferencia) {
        this.setNumeroReferencia(numeroReferencia);
        return this;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public Transacao getTransacao() {
        return this.transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Transferencia transacao(Transacao transacao) {
        this.setTransacao(transacao);
        return this;
    }

    public User getUsuarioOrigem() {
        return this.usuarioOrigem;
    }

    public void setUsuarioOrigem(User user) {
        this.usuarioOrigem = user;
    }

    public Transferencia usuarioOrigem(User user) {
        this.setUsuarioOrigem(user);
        return this;
    }

    public User getUsuarioDestino() {
        return this.usuarioDestino;
    }

    public void setUsuarioDestino(User user) {
        this.usuarioDestino = user;
    }

    public Transferencia usuarioDestino(User user) {
        this.setUsuarioDestino(user);
        return this;
    }

    public Carteira getCarteiraOrigem() {
        return this.carteiraOrigem;
    }

    public void setCarteiraOrigem(Carteira carteira) {
        this.carteiraOrigem = carteira;
    }

    public Transferencia carteiraOrigem(Carteira carteira) {
        this.setCarteiraOrigem(carteira);
        return this;
    }

    public Carteira getCarteiraDestino() {
        return this.carteiraDestino;
    }

    public void setCarteiraDestino(Carteira carteira) {
        this.carteiraDestino = carteira;
    }

    public Transferencia carteiraDestino(Carteira carteira) {
        this.setCarteiraDestino(carteira);
        return this;
    }

    public MoedaCarteira getMoedaCarteira() {
        return this.moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteira moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public Transferencia moedaCarteira(MoedaCarteira moedaCarteira) {
        this.setMoedaCarteira(moedaCarteira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transferencia)) {
            return false;
        }
        return getId() != null && getId().equals(((Transferencia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transferencia{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", chaveInterna='" + getChaveInterna() + "'" +
            ", tipoChave='" + getTipoChave() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", motivoRejeicao='" + getMotivoRejeicao() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            "}";
    }
}
