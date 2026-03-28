package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumTipoPermissao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.PermissaoColaborador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissaoColaboradorDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoPermissao tipoPermissao;

    @NotNull
    private ColaboradorPJDTO colaborador;

    private CarteiraDTO carteira;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoPermissao getTipoPermissao() {
        return tipoPermissao;
    }

    public void setTipoPermissao(EnumTipoPermissao tipoPermissao) {
        this.tipoPermissao = tipoPermissao;
    }

    public ColaboradorPJDTO getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorPJDTO colaborador) {
        this.colaborador = colaborador;
    }

    public CarteiraDTO getCarteira() {
        return carteira;
    }

    public void setCarteira(CarteiraDTO carteira) {
        this.carteira = carteira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissaoColaboradorDTO)) {
            return false;
        }

        PermissaoColaboradorDTO permissaoColaboradorDTO = (PermissaoColaboradorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissaoColaboradorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissaoColaboradorDTO{" +
            "id=" + getId() +
            ", tipoPermissao='" + getTipoPermissao() + "'" +
            ", colaborador=" + getColaborador() +
            ", carteira=" + getCarteira() +
            "}";
    }
}
