package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumDiaSemana;
import com.bankernel.domain.enumeration.EnumTipoExecucao;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.BloqueioOperacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BloqueioOperacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private EnumTipoOperacao tipoOperacao;

    private EnumDiaSemana diaSemana;

    @Size(max = 5)
    private String horaInicio;

    @Size(max = 5)
    private String horaFim;

    @Size(max = 500)
    private String motivo;

    @NotNull
    private Boolean ativo;

    @NotNull
    private EnumTipoExecucao tipoExecucao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(EnumTipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public EnumDiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(EnumDiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnumTipoExecucao getTipoExecucao() {
        return tipoExecucao;
    }

    public void setTipoExecucao(EnumTipoExecucao tipoExecucao) {
        this.tipoExecucao = tipoExecucao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BloqueioOperacaoDTO)) {
            return false;
        }

        BloqueioOperacaoDTO bloqueioOperacaoDTO = (BloqueioOperacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bloqueioOperacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BloqueioOperacaoDTO{" +
            "id=" + getId() +
            ", tipoOperacao='" + getTipoOperacao() + "'" +
            ", diaSemana='" + getDiaSemana() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFim='" + getHoraFim() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", tipoExecucao='" + getTipoExecucao() + "'" +
            "}";
    }
}
