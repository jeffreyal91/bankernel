package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusDepositoPix;
import com.bankernel.domain.enumeration.EnumTipoDepositoPix;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.DepositoPix} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepositoPixDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoDepositoPix tipo;

    @NotNull
    private EnumStatusDepositoPix situacao;

    @NotNull
    private BigDecimal valorOriginal;

    private BigDecimal valorCreditado;

    private BigDecimal valorRecebido;

    @Size(max = 500)
    private String codigoQr;

    @Size(max = 100)
    private String identificadorTransacao;

    @Size(max = 100)
    private String identificadorPontaAPonta;

    @Size(max = 200)
    private String pagadorNome;

    @Size(max = 11)
    private String pagadorCpf;

    @Size(max = 14)
    private String pagadorCnpj;

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

    public EnumTipoDepositoPix getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoDepositoPix tipo) {
        this.tipo = tipo;
    }

    public EnumStatusDepositoPix getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusDepositoPix situacao) {
        this.situacao = situacao;
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

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public String getIdentificadorTransacao() {
        return identificadorTransacao;
    }

    public void setIdentificadorTransacao(String identificadorTransacao) {
        this.identificadorTransacao = identificadorTransacao;
    }

    public String getIdentificadorPontaAPonta() {
        return identificadorPontaAPonta;
    }

    public void setIdentificadorPontaAPonta(String identificadorPontaAPonta) {
        this.identificadorPontaAPonta = identificadorPontaAPonta;
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
        if (!(o instanceof DepositoPixDTO)) {
            return false;
        }

        DepositoPixDTO depositoPixDTO = (DepositoPixDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depositoPixDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoPixDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", valorOriginal=" + getValorOriginal() +
            ", valorCreditado=" + getValorCreditado() +
            ", valorRecebido=" + getValorRecebido() +
            ", codigoQr='" + getCodigoQr() + "'" +
            ", identificadorTransacao='" + getIdentificadorTransacao() + "'" +
            ", identificadorPontaAPonta='" + getIdentificadorPontaAPonta() + "'" +
            ", pagadorNome='" + getPagadorNome() + "'" +
            ", pagadorCpf='" + getPagadorCpf() + "'" +
            ", pagadorCnpj='" + getPagadorCnpj() + "'" +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            ", contabilizado='" + getContabilizado() + "'" +
            ", deposito=" + getDeposito() +
            ", carteira=" + getCarteira() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
