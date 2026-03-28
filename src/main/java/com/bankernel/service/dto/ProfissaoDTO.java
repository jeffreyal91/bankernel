package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Profissao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfissaoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nome;

    @NotNull
    private Boolean ativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        if (!(o instanceof ProfissaoDTO)) {
            return false;
        }

        ProfissaoDTO profissaoDTO = (ProfissaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profissaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfissaoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
