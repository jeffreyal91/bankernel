package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.ColaboradorPJ} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ColaboradorPJDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean ativo;

    @Size(max = 100)
    private String departamento;

    @NotNull
    private UserDTO usuario;

    @NotNull
    private PessoaJuridicaDTO pessoaJuridica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public PessoaJuridicaDTO getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridicaDTO pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColaboradorPJDTO)) {
            return false;
        }

        ColaboradorPJDTO colaboradorPJDTO = (ColaboradorPJDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, colaboradorPJDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ColaboradorPJDTO{" +
            "id=" + getId() +
            ", ativo='" + getAtivo() + "'" +
            ", departamento='" + getDepartamento() + "'" +
            ", usuario=" + getUsuario() +
            ", pessoaJuridica=" + getPessoaJuridica() +
            "}";
    }
}
