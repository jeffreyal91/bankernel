package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusDepositoBoleto;
import com.bankernel.domain.enumeration.EnumTipoDepositoBoleto;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.DepositoBoleto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepositoBoletoDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoDepositoBoleto tipo;

    @NotNull
    private EnumStatusDepositoBoleto situacao;

    @Size(max = 50)
    private String codigoBarras;

    @Size(max = 60)
    private String linhaDigitavel;

    @NotNull
    private BigDecimal valorOriginal;

    private BigDecimal valorCreditado;

    private BigDecimal valorRecebido;

    @Size(max = 200)
    private String pagadorNome;

    @Size(max = 11)
    private String pagadorCpf;

    @Size(max = 14)
    private String pagadorCnpj;

    private LocalDate dataVencimento;

    private Instant dataRecebimento;

    @NotNull
    private Boolean contabilizado;

    @NotNull
    private DepositoDTO deposito;

    @NotNull
    private CarteiraDTO carteira;

    @NotNull
    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoDepositoBoleto getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoDepositoBoleto tipo) {
        this.tipo = tipo;
    }

    public EnumStatusDepositoBoleto getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusDepositoBoleto situacao) {
        this.situacao = situacao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getLinhaDigitavel() {
        return linhaDigitavel;
    }

    public void setLinhaDigitavel(String linhaDigitavel) {
        this.linhaDigitavel = linhaDigitavel;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorCreditado() {
        return valorCreditado;
    }

    public void setValorCreditado(BigDecimal valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getPagadorNome() {
        return pagadorNome;
    }

    public void setPagadorNome(String pagadorNome) {
        this.pagadorNome = pagadorNome;
    }

    public String getPagadorCpf() {
        return pagadorCpf;
    }

    public void setPagadorCpf(String pagadorCpf) {
        this.pagadorCpf = pagadorCpf;
    }

    public String getPagadorCnpj() {
        return pagadorCnpj;
    }

    public void setPagadorCnpj(String pagadorCnpj) {
        this.pagadorCnpj = pagadorCnpj;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Instant getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Instant dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public DepositoDTO getDeposito() {
        return deposito;
    }

    public void setDeposito(DepositoDTO deposito) {
        this.deposito = deposito;
    }

    public CarteiraDTO getCarteira() {
        return carteira;
    }

    public void setCarteira(CarteiraDTO carteira) {
        this.carteira = carteira;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepositoBoletoDTO)) {
            return false;
        }

        DepositoBoletoDTO depositoBoletoDTO = (DepositoBoletoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depositoBoletoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoBoletoDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", codigoBarras='" + getCodigoBarras() + "'" +
            ", linhaDigitavel='" + getLinhaDigitavel() + "'" +
            ", valorOriginal=" + getValorOriginal() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorRecebido=" + getValorRecebido() +
            ", pagadorNome='" + getPagadorNome() + "'" +
            ", pagadorCpf='" + getPagadorCpf() + "'" +
            ", pagadorCnpj='" + getPagadorCnpj() + "'" +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            ", deposito=" + getDeposito() +
            ", carteira=" + getCarteira() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
