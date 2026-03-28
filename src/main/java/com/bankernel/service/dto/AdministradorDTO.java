package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Administrador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdministradorDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean ativo;

    @NotNull
    private UserDTO usuario;

    private EscritorioDTO escritorio;

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

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public EscritorioDTO getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(EscritorioDTO escritorio) {
        this.escritorio = escritorio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdministradorDTO)) {
            return false;
        }

        AdministradorDTO administradorDTO = (AdministradorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, administradorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministradorDTO{" +
            "id=" + getId() +
            ", ativo='" + getAtivo() + "'" +
            ", usuario=" + getUsuario() +
            ", escritorio=" + getEscritorio() +
            "}";
    }
}
