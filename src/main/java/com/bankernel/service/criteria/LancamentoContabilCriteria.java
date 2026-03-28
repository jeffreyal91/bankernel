package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumSinalLancamento;
import com.bankernel.domain.enumeration.EnumTipoLancamento;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.LancamentoContabil} entity. This class is used
 * in {@link com.bankernel.web.rest.LancamentoContabilResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lancamento-contabils?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LancamentoContabilCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoLancamento
     */
    public static class EnumTipoLancamentoFilter extends Filter<EnumTipoLancamento> {

        public EnumTipoLancamentoFilter() {}

        public EnumTipoLancamentoFilter(EnumTipoLancamentoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoLancamentoFilter copy() {
            return new EnumTipoLancamentoFilter(this);
        }
    }

    /**
     * Class for filtering EnumSinalLancamento
     */
    public static class EnumSinalLancamentoFilter extends Filter<EnumSinalLancamento> {

        public EnumSinalLancamentoFilter() {}

        public EnumSinalLancamentoFilter(EnumSinalLancamentoFilter filter) {
            super(filter);
        }

        @Override
        public EnumSinalLancamentoFilter copy() {
            return new EnumSinalLancamentoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valor;

    private EnumTipoLancamentoFilter tipoLancamento;

    private EnumSinalLancamentoFilter sinalLancamento;

    private BooleanFilter ativo;

    private LongFilter transacaoId;

    private LongFilter contaContabilId;

    private Boolean distinct;

    public LancamentoContabilCriteria() {}

    public LancamentoContabilCriteria(LancamentoContabilCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valor = other.optionalValor().map(BigDecimalFilter::copy).orElse(null);
        this.tipoLancamento = other.optionalTipoLancamento().map(EnumTipoLancamentoFilter::copy).orElse(null);
        this.sinalLancamento = other.optionalSinalLancamento().map(EnumSinalLancamentoFilter::copy).orElse(null);
        this.ativo = other.optionalAtivo().map(BooleanFilter::copy).orElse(null);
        this.transacaoId = other.optionalTransacaoId().map(LongFilter::copy).orElse(null);
        this.contaContabilId = other.optionalContaContabilId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LancamentoContabilCriteria copy() {
        return new LancamentoContabilCriteria(this);
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

    public EnumTipoLancamentoFilter getTipoLancamento() {
        return tipoLancamento;
    }

    public Optional<EnumTipoLancamentoFilter> optionalTipoLancamento() {
        return Optional.ofNullable(tipoLancamento);
    }

    public EnumTipoLancamentoFilter tipoLancamento() {
        if (tipoLancamento == null) {
            setTipoLancamento(new EnumTipoLancamentoFilter());
        }
        return tipoLancamento;
    }

    public void setTipoLancamento(EnumTipoLancamentoFilter tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public EnumSinalLancamentoFilter getSinalLancamento() {
        return sinalLancamento;
    }

    public Optional<EnumSinalLancamentoFilter> optionalSinalLancamento() {
        return Optional.ofNullable(sinalLancamento);
    }

    public EnumSinalLancamentoFilter sinalLancamento() {
        if (sinalLancamento == null) {
            setSinalLancamento(new EnumSinalLancamentoFilter());
        }
        return sinalLancamento;
    }

    public void setSinalLancamento(EnumSinalLancamentoFilter sinalLancamento) {
        this.sinalLancamento = sinalLancamento;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public Optional<BooleanFilter> optionalAtivo() {
        return Optional.ofNullable(ativo);
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            setAtivo(new BooleanFilter());
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
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

    public LongFilter getContaContabilId() {
        return contaContabilId;
    }

    public Optional<LongFilter> optionalContaContabilId() {
        return Optional.ofNullable(contaContabilId);
    }

    public LongFilter contaContabilId() {
        if (contaContabilId == null) {
            setContaContabilId(new LongFilter());
        }
        return contaContabilId;
    }

    public void setContaContabilId(LongFilter contaContabilId) {
        this.contaContabilId = contaContabilId;
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
        final LancamentoContabilCriteria that = (LancamentoContabilCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(tipoLancamento, that.tipoLancamento) &&
            Objects.equals(sinalLancamento, that.sinalLancamento) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(contaContabilId, that.contaContabilId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, tipoLancamento, sinalLancamento, ativo, transacaoId, contaContabilId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LancamentoContabilCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValor().map(f -> "valor=" + f + ", ").orElse("") +
            optionalTipoLancamento().map(f -> "tipoLancamento=" + f + ", ").orElse("") +
            optionalSinalLancamento().map(f -> "sinalLancamento=" + f + ", ").orElse("") +
            optionalAtivo().map(f -> "ativo=" + f + ", ").orElse("") +
            optionalTransacaoId().map(f -> "transacaoId=" + f + ", ").orElse("") +
            optionalContaContabilId().map(f -> "contaContabilId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
