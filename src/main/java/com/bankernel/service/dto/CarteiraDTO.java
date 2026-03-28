package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Carteira} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarteiraDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal saldo;

    @NotNull
    private BigDecimal limiteNegativo;

    @NotNull
    private BigDecimal valorCongelado;

    @NotNull
    @Size(max = 20)
    private String numeroConta;

    @NotNull
    private Boolean ativa;

    @NotNull
    private MoedaCarteiraDTO moedaCarteira;

    @NotNull
    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimiteNegativo() {
        return limiteNegativo;
    }

    public void setLimiteNegativo(BigDecimal limiteNegativo) {
        this.limiteNegativo = limiteNegativo;
    }

    public BigDecimal getValorCongelado() {
        return valorCongelado;
    }

    public void setValorCongelado(BigDecimal valorCongelado) {
        this.valorCongelado = valorCongelado;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarteiraDTO)) {
            return false;
        }

        CarteiraDTO carteiraDTO = (CarteiraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carteiraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteiraDTO{" +
            "id=" + getId() +
            ", saldo=" + getSaldo() +
            ", limiteNegativo=" + getLimiteNegativo() +
            ", valorCongelado=" + getValorCongelado() +
            ", numeroConta='" + getNumeroConta() + "'" +
            ", ativa='" + getAtiva() + "'" +
            ", moedaCarteira=" + getMoedaCarteira() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
