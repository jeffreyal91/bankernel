package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumStatusHistorico;
import com.bankernel.domain.enumeration.EnumTipoHistorico;
import com.bankernel.domain.enumeration.EnumTipoSimbolo;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.HistoricoOperacao} entity. This class is used
 * in {@link com.bankernel.web.rest.HistoricoOperacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historico-operacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricoOperacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoSimbolo
     */
    public static class EnumTipoSimboloFilter extends Filter<EnumTipoSimbolo> {

        public EnumTipoSimboloFilter() {}

        public EnumTipoSimboloFilter(EnumTipoSimboloFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoSimboloFilter copy() {
            return new EnumTipoSimboloFilter(this);
        }
    }

    /**
     * Class for filtering EnumTipoHistorico
     */
    public static class EnumTipoHistoricoFilter extends Filter<EnumTipoHistorico> {

        public EnumTipoHistoricoFilter() {}

        public EnumTipoHistoricoFilter(EnumTipoHistoricoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoHistoricoFilter copy() {
            return new EnumTipoHistoricoFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusHistorico
     */
    public static class EnumStatusHistoricoFilter extends Filter<EnumStatusHistorico> {

        public EnumStatusHistoricoFilter() {}

        public EnumStatusHistoricoFilter(EnumStatusHistoricoFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusHistoricoFilter copy() {
            return new EnumStatusHistoricoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valor;

    private BigDecimalFilter saldoApos;

    private StringFilter descricao;

    private EnumTipoSimboloFilter tipoSimbolo;

    private StringFilter numeroReferencia;

    private EnumTipoHistoricoFilter tipoHistorico;

    private EnumStatusHistoricoFilter situacaoHistorico;

    private StringFilter tipoEntidadeOrigem;

    private LongFilter idEntidadeOrigem;

    private LongFilter transacaoId;

    private LongFilter usuarioId;

    private LongFilter carteiraId;

    private Boolean distinct;

    public HistoricoOperacaoCriteria() {}

    public HistoricoOperacaoCriteria(HistoricoOperacaoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valor = other.optionalValor().map(BigDecimalFilter::copy).orElse(null);
        this.saldoApos = other.optionalSaldoApos().map(BigDecimalFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.tipoSimbolo = other.optionalTipoSimbolo().map(EnumTipoSimboloFilter::copy).orElse(null);
        this.numeroReferencia = other.optionalNumeroReferencia().map(StringFilter::copy).orElse(null);
        this.tipoHistorico = other.optionalTipoHistorico().map(EnumTipoHistoricoFilter::copy).orElse(null);
        this.situacaoHistorico = other.optionalSituacaoHistorico().map(EnumStatusHistoricoFilter::copy).orElse(null);
        this.tipoEntidadeOrigem = other.optionalTipoEntidadeOrigem().map(StringFilter::copy).orElse(null);
        this.idEntidadeOrigem = other.optionalIdEntidadeOrigem().map(LongFilter::copy).orElse(null);
        this.transacaoId = other.optionalTransacaoId().map(LongFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.carteiraId = other.optionalCarteiraId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoricoOperacaoCriteria copy() {
        return new HistoricoOperacaoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getValor() {
        return valor;
    }

    public Optional<BigDecimalFilter> optionalValor() {
        return Optional.ofNullable(valor);
    }

    public BigDecimalFilter valor() {
        if (valor == null) {
            setValor(new BigDecimalFilter());
        }
        return valor;
    }

    public void setValor(BigDecimalFilter valor) {
        this.valor = valor;
    }

    public BigDecimalFilter getSaldoApos() {
        return saldoApos;
    }

    public Optional<BigDecimalFilter> optionalSaldoApos() {
        return Optional.ofNullable(saldoApos);
    }

    public BigDecimalFilter saldoApos() {
        if (saldoApos == null) {
            setSaldoApos(new BigDecimalFilter());
        }
        return saldoApos;
    }

    public void setSaldoApos(BigDecimalFilter saldoApos) {
        this.saldoApos = saldoApos;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public Optional<StringFilter> optionalDescricao() {
        return Optional.ofNullable(descricao);
    }

    public StringFilter descricao() {
        if (descricao == null) {
            setDescricao(new StringFilter());
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public EnumTipoSimboloFilter getTipoSimbolo() {
        return tipoSimbolo;
    }

    public Optional<EnumTipoSimboloFilter> optionalTipoSimbolo() {
        return Optional.ofNullable(tipoSimbolo);
    }

    public EnumTipoSimboloFilter tipoSimbolo() {
        if (tipoSimbolo == null) {
            setTipoSimbolo(new EnumTipoSimboloFilter());
        }
        return tipoSimbolo;
    }

    public void setTipoSimbolo(EnumTipoSimboloFilter tipoSimbolo) {
        this.tipoSimbolo = tipoSimbolo;
    }

    public StringFilter getNumeroReferencia() {
        return numeroReferencia;
    }

    public Optional<StringFilter> optionalNumeroReferencia() {
        return Optional.ofNullable(numeroReferencia);
    }

    public StringFilter numeroReferencia() {
        if (numeroReferencia == null) {
            setNumeroReferencia(new StringFilter());
        }
        return numeroReferencia;
    }

    public void setNumeroReferencia(StringFilter numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public EnumTipoHistoricoFilter getTipoHistorico() {
        return tipoHistorico;
    }

    public Optional<EnumTipoHistoricoFilter> optionalTipoHistorico() {
        return Optional.ofNullable(tipoHistorico);
    }

    public EnumTipoHistoricoFilter tipoHistorico() {
        if (tipoHistorico == null) {
            setTipoHistorico(new EnumTipoHistoricoFilter());
        }
        return tipoHistorico;
    }

    public void setTipoHistorico(EnumTipoHistoricoFilter tipoHistorico) {
        this.tipoHistorico = tipoHistorico;
    }

    public EnumStatusHistoricoFilter getSituacaoHistorico() {
        return situacaoHistorico;
    }

    public Optional<EnumStatusHistoricoFilter> optionalSituacaoHistorico() {
        return Optional.ofNullable(situacaoHistorico);
    }

    public EnumStatusHistoricoFilter situacaoHistorico() {
        if (situacaoHistorico == null) {
            setSituacaoHistorico(new EnumStatusHistoricoFilter());
        }
        return situacaoHistorico;
    }

    public void setSituacaoHistorico(EnumStatusHistoricoFilter situacaoHistorico) {
        this.situacaoHistorico = situacaoHistorico;
    }

    public StringFilter getTipoEntidadeOrigem() {
        return tipoEntidadeOrigem;
    }

    public Optional<StringFilter> optionalTipoEntidadeOrigem() {
        return Optional.ofNullable(tipoEntidadeOrigem);
    }

    public StringFilter tipoEntidadeOrigem() {
        if (tipoEntidadeOrigem == null) {
            setTipoEntidadeOrigem(new StringFilter());
        }
        return tipoEntidadeOrigem;
    }

    public void setTipoEntidadeOrigem(StringFilter tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public LongFilter getIdEntidadeOrigem() {
        return idEntidadeOrigem;
    }

    public Optional<LongFilter> optionalIdEntidadeOrigem() {
        return Optional.ofNullable(idEntidadeOrigem);
    }

    public LongFilter idEntidadeOrigem() {
        if (idEntidadeOrigem == null) {
            setIdEntidadeOrigem(new LongFilter());
        }
        return idEntidadeOrigem;
    }

    public void setIdEntidadeOrigem(LongFilter idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
    }

    public LongFilter getTransacaoId() {
        return transacaoId;
    }

    public Optional<LongFilter> optionalTransacaoId() {
        return Optional.ofNullable(transacaoId);
    }

    public LongFilter transacaoId() {
        if (transacaoId == null) {
            setTransacaoId(new LongFilter());
        }
        return transacaoId;
    }

    public void setTransacaoId(LongFilter transacaoId) {
        this.transacaoId = transacaoId;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public Optional<LongFilter> optionalUsuarioId() {
        return Optional.ofNullable(usuarioId);
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            setUsuarioId(new LongFilter());
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getCarteiraId() {
        return carteiraId;
    }

    public Optional<LongFilter> optionalCarteiraId() {
        return Optional.ofNullable(carteiraId);
    }

    public LongFilter carteiraId() {
        if (carteiraId == null) {
            setCarteiraId(new LongFilter());
        }
        return carteiraId;
    }

    public void setCarteiraId(LongFilter carteiraId) {
        this.carteiraId = carteiraId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoricoOperacaoCriteria that = (HistoricoOperacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(saldoApos, that.saldoApos) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(tipoSimbolo, that.tipoSimbolo) &&
            Objects.equals(numeroReferencia, that.numeroReferencia) &&
            Objects.equals(tipoHistorico, that.tipoHistorico) &&
            Objects.equals(situacaoHistorico, that.situacaoHistorico) &&
            Objects.equals(tipoEntidadeOrigem, that.tipoEntidadeOrigem) &&
            Objects.equals(idEntidadeOrigem, that.idEntidadeOrigem) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(carteiraId, that.carteiraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valor,
            saldoApos,
            descricao,
            tipoSimbolo,
            numeroReferencia,
            tipoHistorico,
            situacaoHistorico,
            tipoEntidadeOrigem,
            idEntidadeOrigem,
            transacaoId,
            usuarioId,
            carteiraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoOperacaoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValor().map(f -> "valor=" + f + ", ").orElse("") +
            optionalSaldoApos().map(f -> "saldoApos=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalTipoSimbolo().map(f -> "tipoSimbolo=" + f + ", ").orElse("") +
            optionalNumeroReferencia().map(f -> "numeroReferencia=" + f + ", ").orElse("") +
            optionalTipoHistorico().map(f -> "tipoHistorico=" + f + ", ").orElse("") +
            optionalSituacaoHistorico().map(f -> "situacaoHistorico=" + f + ", ").orElse("") +
            optionalTipoEntidadeOrigem().map(f -> "tipoEntidadeOrigem=" + f + ", ").orElse("") +
            optionalIdEntidadeOrigem().map(f -> "idEntidadeOrigem=" + f + ", ").orElse("") +
            optionalTransacaoId().map(f -> "transacaoId=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalCarteiraId().map(f -> "carteiraId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
