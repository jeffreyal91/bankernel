package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumTipoConfiguracao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.ConfiguracaoSistema} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracaoSistemaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String chave;

    @NotNull
    @Size(max = 500)
    private String valor;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private EnumTipoConfiguracao tipo;

    @NotNull
    @Size(max = 50)
    private String modulo;

    @NotNull
    private Boolean ativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoConfiguracao getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoConfiguracao tipo) {
        this.tipo = tipo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
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
        if (!(o instanceof ConfiguracaoSistemaDTO)) {
            return false;
        }

        ConfiguracaoSistemaDTO configuracaoSistemaDTO = (ConfiguracaoSistemaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, configuracaoSistemaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfiguracaoSistemaDTO{" +
            "id=" + getId() +
            ", chave='" + getChave() + "'" +
            ", valor='" + getValor() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", modulo='" + getModulo() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
