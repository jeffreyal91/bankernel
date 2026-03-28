package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusSaqueBoleto;
import com.bankernel.domain.enumeration.EnumTipoSaqueBoleto;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.SaqueBoleto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaqueBoletoDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoSaqueBoleto tipo;

    @NotNull
    private EnumStatusSaqueBoleto situacao;

    @NotNull
    private BigDecimal valorSaque;

    private BigDecimal valorEnviado;

    @Size(max = 50)
    private String codigoBarras;

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

    public EnumTipoSaqueBoleto getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoSaqueBoleto tipo) {
        this.tipo = tipo;
    }

    public EnumStatusSaqueBoleto getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusSaqueBoleto situacao) {
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

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
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
        if (!(o instanceof SaqueBoletoDTO)) {
            return false;
        }

        SaqueBoletoDTO saqueBoletoDTO = (SaqueBoletoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saqueBoletoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaqueBoletoDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", valorSaque=" + getValorSaque() +
            ", valorEnviado=" + getValorEnviado() +
            ", codigoBarras='" + getCodigoBarras() + "'" +
            ", campoLivre='" + getCampoLivre() + "'" +
            ", saque=" + getSaque() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
