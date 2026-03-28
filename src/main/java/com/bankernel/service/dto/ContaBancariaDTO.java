package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumTipoConta;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.ContaBancaria} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaBancariaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String nomeTitular;

    @NotNull
    @Size(max = 30)
    private String numeroConta;

    @Size(max = 10)
    private String agencia;

    @Size(max = 100)
    private String nomeBanco;

    @Size(max = 10)
    private String codigoBanco;

    @Size(max = 8)
    private String ispb;

    @Size(max = 20)
    private String codigoSwift;

    @NotNull
    private EnumTipoConta tipoConta;

    @NotNull
    private Boolean ativa;

    @NotNull
    private UserDTO usuario;

    private PaisDTO pais;

    private MoedaDTO moeda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getIspb() {
        return ispb;
    }

    public void setIspb(String ispb) {
        this.ispb = ispb;
    }

    public String getCodigoSwift() {
        return codigoSwift;
    }

    public void setCodigoSwift(String codigoSwift) {
        this.codigoSwift = codigoSwift;
    }

    public EnumTipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(EnumTipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public PaisDTO getPais() {
        return pais;
    }

    public void setPais(PaisDTO pais) {
        this.pais = pais;
    }

    public MoedaDTO getMoeda() {
        return moeda;
    }

    public void setMoeda(MoedaDTO moeda) {
        this.moeda = moeda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaBancariaDTO)) {
            return false;
        }

        ContaBancariaDTO contaBancariaDTO = (ContaBancariaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contaBancariaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaBancariaDTO{" +
            "id=" + getId() +
            ", nomeTitular='" + getNomeTitular() + "'" +
            ", numeroConta='" + getNumeroConta() + "'" +
            ", agencia='" + getAgencia() + "'" +
            ", nomeBanco='" + getNomeBanco() + "'" +
            ", codigoBanco='" + getCodigoBanco() + "'" +
            ", ispb='" + getIspb() + "'" +
            ", codigoSwift='" + getCodigoSwift() + "'" +
            ", tipoConta='" + getTipoConta() + "'" +
            ", ativa='" + getAtiva() + "'" +
            ", usuario=" + getUsuario() +
            ", pais=" + getPais() +
            ", moeda=" + getMoeda() +
            "}";
    }
}
