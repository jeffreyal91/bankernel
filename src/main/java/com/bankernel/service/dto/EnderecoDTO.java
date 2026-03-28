package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumTipoEndereco;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Endereco} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnderecoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 8)
    private String cep;

    @NotNull
    @Size(max = 200)
    private String logradouro;

    @NotNull
    @Size(max = 20)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @NotNull
    @Size(max = 100)
    private String bairro;

    @NotNull
    @Size(max = 100)
    private String cidade;

    @NotNull
    @Size(max = 2)
    private String estado;

    @NotNull
    private EnumTipoEndereco tipoEndereco;

    @NotNull
    private Boolean principal;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EnumTipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(EnumTipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
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
        if (!(o instanceof EnderecoDTO)) {
            return false;
        }

        EnderecoDTO enderecoDTO = (EnderecoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enderecoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnderecoDTO{" +
            "id=" + getId() +
            ", cep='" + getCep() + "'" +
            ", logradouro='" + getLogradouro() + "'" +
            ", numero='" + getNumero() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", estado='" + getEstado() + "'" +
            ", tipoEndereco='" + getTipoEndereco() + "'" +
            ", principal='" + getPrincipal() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
