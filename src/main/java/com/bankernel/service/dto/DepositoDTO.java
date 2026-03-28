package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusDeposito;
import com.bankernel.domain.enumeration.EnumTipoDeposito;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Deposito} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepositoDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valor;

    private BigDecimal valorCreditado;

    private BigDecimal valorSaldoCarteira;

    @NotNull
    private EnumTipoDeposito tipoDeposito;

    @NotNull
    private EnumStatusDeposito situacaoDeposito;

    @NotNull
    @Size(max = 50)
    private String numeroReferencia;

    @Size(max = 100)
    private String referenciaExterna;

    @Size(max = 500)
    private String descricao;

    @Size(max = 500)
    private String motivoRejeicao;

    @NotNull
    private Boolean contabilizado;

    @Size(max = 200)
    private String nomeUsuarioFixo;

    private Integer numeroParcela;

    private TransacaoDTO transacao;

    @NotNull
    private CarteiraDTO carteira;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    @NotNull
    private UserDTO usuario;

    private ContaBancariaDTO contaBancaria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorCreditado() {
        return valorCreditado;
    }

    public void setValorCreditado(BigDecimal valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimal getValorSaldoCarteira() {
        return valorSaldoCarteira;
    }

    public void setValorSaldoCarteira(BigDecimal valorSaldoCarteira) {
        this.valorSaldoCarteira = valorSaldoCarteira;
    }

    public EnumTipoDeposito getTipoDeposito() {
        return tipoDeposito;
    }

    public void setTipoDeposito(EnumTipoDeposito tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    public EnumStatusDeposito getSituacaoDeposito() {
        return situacaoDeposito;
    }

    public void setSituacaoDeposito(EnumStatusDeposito situacaoDeposito) {
        this.situacaoDeposito = situacaoDeposito;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public String getNomeUsuarioFixo() {
        return nomeUsuarioFixo;
    }

    public void setNomeUsuarioFixo(String nomeUsuarioFixo) {
        this.nomeUsuarioFixo = nomeUsuarioFixo;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public TransacaoDTO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoDTO transacao) {
        this.transacao = transacao;
    }

    public CarteiraDTO getCarteira() {
        return carteira;
    }

    public void setCarteira(CarteiraDTO carteira) {
        this.carteira = carteira;
    }

    public MoedaCarteiraDTO getMoedaCarteira() {
        return moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteiraDTO moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public ContaBancariaDTO getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancariaDTO contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepositoDTO)) {
            return false;
        }

        DepositoDTO depositoDTO = (DepositoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depositoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorSaldoCarteira=" + getValorSaldoCarteira() +
            ", tipoDeposito='" + getTipoDeposito() + "'" +
            ", situacaoDeposito='" + getSituacaoDeposito() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", referenciaExterna='" + getReferenciaExterna() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", motivoRejeicao='" + getMotivoRejeicao() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            ", nomeUsuarioFixo='" + getNomeUsuarioFixo() + "'" +
            ", numeroParcela=" + getNumeroParcela() +
            ", transacao=" + getTransacao() +
            ", carteira=" + getCarteira() +
            ", moedaCarteira=" + getMoedaCarteira() +
            ", usuario=" + getUsuario() +
            ", contaBancaria=" + getContaBancaria() +
            "}";
    }
}
