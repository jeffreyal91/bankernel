package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumCodigoMoeda;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Moeda} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoedaDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumCodigoMoeda codigoMoeda;

    @NotNull
    @Size(max = 50)
    private String nome;

    @Size(max = 200)
    private String descricao;

    @NotNull
    private Boolean ativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumCodigoMoeda getCodigoMoeda() {
        return codigoMoeda;
    }

    public void setCodigoMoeda(EnumCodigoMoeda codigoMoeda) {
        this.codigoMoeda = codigoMoeda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoedaDTO)) {
            return false;
        }

        MoedaDTO moedaDTO = (MoedaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moedaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoedaDTO{" +
            "id=" + getId() +
            ", codigoMoeda='" + getCodigoMoeda() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
