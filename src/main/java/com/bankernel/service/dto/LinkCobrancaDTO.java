package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusLinkCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.bankernel.domain.enumeration.EnumTipoLinkCobranca;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.LinkCobranca} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LinkCobrancaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String nome;

    @NotNull
    @Size(max = 100)
    private String chaveAcesso;

    @Size(max = 100)
    private String chaveApi;

    @Size(max = 2000)
    private String urlRetorno;

    @Size(max = 2000)
    private String urlPagamentoAprovado;

    @Size(max = 2000)
    private String urlPagamentoCancelado;

    @Size(max = 2000)
    private String urlPagamentoRejeitado;

    @NotNull
    private EnumTipoLinkCobranca tipo;

    @NotNull
    private EnumStatusLinkCobranca situacao;

    private Instant dataInicio;

    private Instant dataFim;

    private BigDecimal desconto;

    private EnumTipoDesconto tipoDesconto;

    private BigDecimal valorMinimoPagamento;

    @Size(max = 100)
    private String identificadorExterno;

    @Size(max = 50)
    private String numeroReferencia;

    @NotNull
    private Boolean ativo;

    @NotNull
    private Boolean bloqueado;

    @NotNull
    private UserDTO usuario;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public String getChaveApi() {
        return chaveApi;
    }

    public void setChaveApi(String chaveApi) {
        this.chaveApi = chaveApi;
    }

    public String getUrlRetorno() {
        return urlRetorno;
    }

    public void setUrlRetorno(String urlRetorno) {
        this.urlRetorno = urlRetorno;
    }

    public String getUrlPagamentoAprovado() {
        return urlPagamentoAprovado;
    }

    public void setUrlPagamentoAprovado(String urlPagamentoAprovado) {
        this.urlPagamentoAprovado = urlPagamentoAprovado;
    }

    public String getUrlPagamentoCancelado() {
        return urlPagamentoCancelado;
    }

    public void setUrlPagamentoCancelado(String urlPagamentoCancelado) {
        this.urlPagamentoCancelado = urlPagamentoCancelado;
    }

    public String getUrlPagamentoRejeitado() {
        return urlPagamentoRejeitado;
    }

    public void setUrlPagamentoRejeitado(String urlPagamentoRejeitado) {
        this.urlPagamentoRejeitado = urlPagamentoRejeitado;
    }

    public EnumTipoLinkCobranca getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoLinkCobranca tipo) {
        this.tipo = tipo;
    }

    public EnumStatusLinkCobranca getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusLinkCobranca situacao) {
        this.situacao = situacao;
    }

    public Instant getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return dataFim;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public EnumTipoDesconto getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(EnumTipoDesconto tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public BigDecimal getValorMinimoPagamento() {
        return valorMinimoPagamento;
    }

    public void setValorMinimoPagamento(BigDecimal valorMinimoPagamento) {
        this.valorMinimoPagamento = valorMinimoPagamento;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public MoedaCarteiraDTO getMoedaCarteira() {
        return moedaCarteira;
    }

    public void setMoedaCarteira(MoedaCarteiraDTO moedaCarteira) {
        this.moedaCarteira = moedaCarteira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinkCobrancaDTO)) {
            return false;
        }

        LinkCobrancaDTO linkCobrancaDTO = (LinkCobrancaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, linkCobrancaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LinkCobrancaDTO{" +
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
            ", usuario=" + getUsuario() +
            ", moedaCarteira=" + getMoedaCarteira() +
            "}";
    }
}
