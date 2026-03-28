package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumDiaSemana;
import com.bankernel.domain.enumeration.EnumTipoExecucao;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BloqueioOperacao.
 */
@Entity
@Table(name = "conf_bloqueio_operacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BloqueioOperacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false)
    private EnumTipoOperacao tipoOperacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private EnumDiaSemana diaSemana;

    @Size(max = 5)
    @Column(name = "hora_inicio", length = 5)
    private String horaInicio;

    @Size(max = 5)
    @Column(name = "hora_fim", length = 5)
    private String horaFim;

    @Size(max = 500)
    @Column(name = "motivo", length = 500)
    private String motivo;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_execucao", nullable = false)
    private EnumTipoExecucao tipoExecucao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BloqueioOperacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumTipoOperacao getTipoOperacao() {
        return this.tipoOperacao;
    }

    public BloqueioOperacao tipoOperacao(EnumTipoOperacao tipoOperacao) {
        this.setTipoOperacao(tipoOperacao);
        return this;
    }

    public void setTipoOperacao(EnumTipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public EnumDiaSemana getDiaSemana() {
        return this.diaSemana;
    }

    public BloqueioOperacao diaSemana(EnumDiaSemana diaSemana) {
        this.setDiaSemana(diaSemana);
        return this;
    }

    public void setDiaSemana(EnumDiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return this.horaInicio;
    }

    public BloqueioOperacao horaInicio(String horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return this.horaFim;
    }

    public BloqueioOperacao horaFim(String horaFim) {
        this.setHoraFim(horaFim);
        return this;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public BloqueioOperacao motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public BloqueioOperacao ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnumTipoExecucao getTipoExecucao() {
        return this.tipoExecucao;
    }

    public BloqueioOperacao tipoExecucao(EnumTipoExecucao tipoExecucao) {
        this.setTipoExecucao(tipoExecucao);
        return this;
    }

    public void setTipoExecucao(EnumTipoExecucao tipoExecucao) {
        this.tipoExecucao = tipoExecucao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BloqueioOperacao)) {
            return false;
        }
        return getId() != null && getId().equals(((BloqueioOperacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BloqueioOperacao{" +
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
