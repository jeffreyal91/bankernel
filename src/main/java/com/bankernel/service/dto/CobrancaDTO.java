package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusCobranca;
import com.bankernel.domain.enumeration.EnumTipoCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Cobranca} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CobrancaDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal valor;

    private BigDecimal valorCreditado;

    private BigDecimal valorCreditadoCarteira;

    @Size(max = 100)
    private String idProdutoExterno;

    @Size(max = 200)
    private String nomeProdutoExterno;

    @NotNull
    private EnumStatusCobranca situacao;

    @NotNull
    private EnumTipoCobranca tipo;

    private BigDecimal descontoGeral;

    private EnumTipoDesconto tipoDesconto;

    @NotNull
    private Boolean contabilizado;

    @Size(max = 200)
    private String nomeUsuarioFixo;

    @Size(max = 100)
    private String chaveCobranca;

    @Size(max = 100)
    private String identificadorExterno;

    @NotNull
    private Boolean retornoNotificado;

    private TransacaoDTO transacao;

    @NotNull
    private UserDTO usuario;

    @NotNull
    private CarteiraDTO carteira;

    private CarteiraDTO carteiraCreditada;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    private LinkCobrancaDTO linkCobranca;

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

    public BigDecimal getValorCreditadoCarteira() {
        return valorCreditadoCarteira;
    }

    public void setValorCreditadoCarteira(BigDecimal valorCreditadoCarteira) {
        this.valorCreditadoCarteira = valorCreditadoCarteira;
    }

    public String getIdProdutoExterno() {
        return idProdutoExterno;
    }

    public void setIdProdutoExterno(String idProdutoExterno) {
        this.idProdutoExterno = idProdutoExterno;
    }

    public String getNomeProdutoExterno() {
        return nomeProdutoExterno;
    }

    public void setNomeProdutoExterno(String nomeProdutoExterno) {
        this.nomeProdutoExterno = nomeProdutoExterno;
    }

    public EnumStatusCobranca getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusCobranca situacao) {
        this.situacao = situacao;
    }

    public EnumTipoCobranca getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoCobranca tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getDescontoGeral() {
        return descontoGeral;
    }

    public void setDescontoGeral(BigDecimal descontoGeral) {
        this.descontoGeral = descontoGeral;
    }

    public EnumTipoDesconto getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(EnumTipoDesconto tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
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

    public String getChaveCobranca() {
        return chaveCobranca;
    }

    public void setChaveCobranca(String chaveCobranca) {
        this.chaveCobranca = chaveCobranca;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public Boolean getRetornoNotificado() {
        return retornoNotificado;
    }

    public void setRetornoNotificado(Boolean retornoNotificado) {
        this.retornoNotificado = retornoNotificado;
    }

    public TransacaoDTO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoDTO transacao) {
        this.transacao = transacao;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public CarteiraDTO getCarteira() {
        return carteira;
    }

    public void setCarteira(CarteiraDTO carteira) {
        this.carteira = carteira;
    }

    public CarteiraDTO getCarteiraCreditada() {
        return carteiraCreditada;
    }

    public void setCarteiraCreditada(CarteiraDTO carteiraCreditada) {
        this.carteiraCreditada = carteiraCreditada;
    }

    public MoedaCarteiraDTO getMoedaCarteira() {
        return moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteiraDTO moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    public LinkCobrancaDTO getLinkCobranca() {
        return linkCobranca;
    }

    public void setLinkCobranca(LinkCobrancaDTO linkCobranca) {
        this.linkCobranca = linkCobranca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CobrancaDTO)) {
            return false;
        }

        CobrancaDTO cobrancaDTO = (CobrancaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cobrancaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CobrancaDTO{" +
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
            ", transacao=" + getTransacao() +
            ", usuario=" + getUsuario() +
            ", carteira=" + getCarteira() +
            ", carteiraCreditada=" + getCarteiraCreditada() +
            ", moedaCarteira=" + getMoedaCarteira() +
            ", linkCobranca=" + getLinkCobranca() +
            "}";
    }
}
