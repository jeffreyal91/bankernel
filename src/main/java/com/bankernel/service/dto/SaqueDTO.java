package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusSaque;
import com.bankernel.domain.enumeration.EnumTipoSaque;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Saque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaqueDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valorSaque;

    private BigDecimal valorEnviado;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private EnumTipoSaque tipoSaque;

    @NotNull
    private EnumStatusSaque situacaoSaque;

    @NotNull
    @Size(max = 50)
    private String numeroReferencia;

    @Size(max = 500)
    private String motivoRejeicao;

    @NotNull
    private Boolean contabilizado;

    @Size(max = 200)
    private String nomeUsuarioFixo;

    private TransacaoDTO transacao;

    private TransacaoDTO transacaoEstorno;

    @NotNull
    private CarteiraDTO carteira;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    private ContaBancariaDTO contaBancariaDestino;

    @NotNull
    private UserDTO usuario;

    private EscritorioDTO escritorio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorSaque() {
        return valorSaque;
    }

    public void setValorSaque(BigDecimal valorSaque) {
        this.valorSaque = valorSaque;
    }

    public BigDecimal getValorEnviado() {
        return valorEnviado;
    }

    public void setValorEnviado(BigDecimal valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoSaque getTipoSaque() {
        return tipoSaque;
    }

    public void setTipoSaque(EnumTipoSaque tipoSaque) {
        this.tipoSaque = tipoSaque;
    }

    public EnumStatusSaque getSituacaoSaque() {
        return situacaoSaque;
    }

    public void setSituacaoSaque(EnumStatusSaque situacaoSaque) {
        this.situacaoSaque = situacaoSaque;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
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

    public TransacaoDTO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoDTO transacao) {
        this.transacao = transacao;
    }

    public TransacaoDTO getTransacaoEstorno() {
        return transacaoEstorno;
    }

    public void setTransacaoEstorno(TransacaoDTO transacaoEstorno) {
        this.transacaoEstorno = transacaoEstorno;
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

    public ContaBancariaDTO getContaBancariaDestino() {
        return contaBancariaDestino;
    }

    public void setContaBancariaDestino(ContaBancariaDTO contaBancariaDestino) {
        this.contaBancariaDestino = contaBancariaDestino;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public EscritorioDTO getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(EscritorioDTO escritorio) {
        this.escritorio = escritorio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaqueDTO)) {
            return false;
        }

        SaqueDTO saqueDTO = (SaqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaqueDTO{" +
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
            ", transacao=" + getTransacao() +
            ", transacaoEstorno=" + getTransacaoEstorno() +
            ", carteira=" + getCarteira() +
            ", moedaCarteira=" + getMoedaCarteira() +
            ", contaBancariaDestino=" + getContaBancariaDestino() +
            ", usuario=" + getUsuario() +
            ", escritorio=" + getEscritorio() +
            "}";
    }
}
