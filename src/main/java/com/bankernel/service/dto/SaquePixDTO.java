package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusSaquePix;
import com.bankernel.domain.enumeration.EnumTipoSaquePix;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.SaquePix} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaquePixDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoSaquePix tipo;

    @NotNull
    private EnumStatusSaquePix situacao;

    @NotNull
    private BigDecimal valorSaque;

    private BigDecimal valorEnviado;

    @Size(max = 100)
    private String identificadorPagamento;

    @Size(max = 100)
    private String identificadorPontaAPonta;

    @Size(max = 200)
    private String campoLivre;

    @NotNull
    private SaqueDTO saque;

    @NotNull
    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoSaquePix getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoSaquePix tipo) {
        this.tipo = tipo;
    }

    public EnumStatusSaquePix getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusSaquePix situacao) {
        this.situacao = situacao;
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

    public String getIdentificadorPagamento() {
        return identificadorPagamento;
    }

    public void setIdentificadorPagamento(String identificadorPagamento) {
        this.identificadorPagamento = identificadorPagamento;
    }

    public String getIdentificadorPontaAPonta() {
        return identificadorPontaAPonta;
    }

    public void setIdentificadorPontaAPonta(String identificadorPontaAPonta) {
        this.identificadorPontaAPonta = identificadorPontaAPonta;
    }

    public String getCampoLivre() {
        return campoLivre;
    }

    public void setCampoLivre(String campoLivre) {
        this.campoLivre = campoLivre;
    }

    public SaqueDTO getSaque() {
        return saque;
    }

    public void setSaque(SaqueDTO saque) {
        this.saque = saque;
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
        if (!(o instanceof SaquePixDTO)) {
            return false;
        }

        SaquePixDTO saquePixDTO = (SaquePixDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saquePixDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaquePixDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", valorSaque=" + getValorSaque() +
            ", valorEnviado=" + getValorEnviado() +
            ", identificadorPagamento='" + getIdentificadorPagamento() + "'" +
            ", identificadorPontaAPonta='" + getIdentificadorPontaAPonta() + "'" +
            ", campoLivre='" + getCampoLivre() + "'" +
            ", saque=" + getSaque() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
