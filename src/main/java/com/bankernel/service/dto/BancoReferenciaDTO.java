package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.BancoReferencia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BancoReferenciaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String codigo;

    @NotNull
    @Size(max = 100)
    private String nome;

    @Size(max = 8)
    private String ispb;

    @NotNull
    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIspb() {
        return ispb;
    }

    public void setIspb(String ispb) {
        this.ispb = ispb;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BancoReferenciaDTO)) {
            return false;
        }

        BancoReferenciaDTO bancoReferenciaDTO = (BancoReferenciaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bancoReferenciaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BancoReferenciaDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nome='" + getNome() + "'" +
            ", ispb='" + getIspb() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
