package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumCategoriaContaContabil;
import com.bankernel.domain.enumeration.EnumTipoContaContabil;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.ContaContabil} entity. This class is used
 * in {@link com.bankernel.web.rest.ContaContabilResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conta-contabils?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaContabilCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoContaContabil
     */
    public static class EnumTipoContaContabilFilter extends Filter<EnumTipoContaContabil> {

        public EnumTipoContaContabilFilter() {}

        public EnumTipoContaContabilFilter(EnumTipoContaContabilFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoContaContabilFilter copy() {
            return new EnumTipoContaContabilFilter(this);
        }
    }

    /**
     * Class for filtering EnumCategoriaContaContabil
     */
    public static class EnumCategoriaContaContabilFilter extends Filter<EnumCategoriaContaContabil> {

        public EnumCategoriaContaContabilFilter() {}

        public EnumCategoriaContaContabilFilter(EnumCategoriaContaContabilFilter filter) {
            super(filter);
        }

        @Override
        public EnumCategoriaContaContabilFilter copy() {
            return new EnumCategoriaContaContabilFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private StringFilter nome;

    private BigDecimalFilter saldo;

    private StringFilter descricao;

    private EnumTipoContaContabilFilter tipoContaContabil;

    private EnumCategoriaContaContabilFilter categoriaContaContabil;

    private BooleanFilter ativa;

    private LongFilter moedaCarteiraId;

    private Boolean distinct;

    public ContaContabilCriteria() {}

    public ContaContabilCriteria(ContaContabilCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.saldo = other.optionalSaldo().map(BigDecimalFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.tipoContaContabil = other.optionalTipoContaContabil().map(EnumTipoContaContabilFilter::copy).orElse(null);
        this.categoriaContaContabil = other.optionalCategoriaContaContabil().map(EnumCategoriaContaContabilFilter::copy).orElse(null);
        this.ativa = other.optionalAtiva().map(BooleanFilter::copy).orElse(null);
        this.moedaCarteiraId = other.optionalMoedaCarteiraId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContaContabilCriteria copy() {
        return new ContaContabilCriteria(this);
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

    public StringFilter getCodigo() {
        return codigo;
    }

    public Optional<StringFilter> optionalCodigo() {
        return Optional.ofNullable(codigo);
    }

    public StringFilter codigo() {
        if (codigo == null) {
            setCodigo(new StringFilter());
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public BigDecimalFilter getSaldo() {
        return saldo;
    }

    public Optional<BigDecimalFilter> optionalSaldo() {
        return Optional.ofNullable(saldo);
    }

    public BigDecimalFilter saldo() {
        if (saldo == null) {
            setSaldo(new BigDecimalFilter());
        }
        return saldo;
    }

    public void setSaldo(BigDecimalFilter saldo) {
        this.saldo = saldo;
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

    public EnumTipoContaContabilFilter getTipoContaContabil() {
        return tipoContaContabil;
    }

    public Optional<EnumTipoContaContabilFilter> optionalTipoContaContabil() {
        return Optional.ofNullable(tipoContaContabil);
    }

    public EnumTipoContaContabilFilter tipoContaContabil() {
        if (tipoContaContabil == null) {
            setTipoContaContabil(new EnumTipoContaContabilFilter());
        }
        return tipoContaContabil;
    }

    public void setTipoContaContabil(EnumTipoContaContabilFilter tipoContaContabil) {
        this.tipoContaContabil = tipoContaContabil;
    }

    public EnumCategoriaContaContabilFilter getCategoriaContaContabil() {
        return categoriaContaContabil;
    }

    public Optional<EnumCategoriaContaContabilFilter> optionalCategoriaContaContabil() {
        return Optional.ofNullable(categoriaContaContabil);
    }

    public EnumCategoriaContaContabilFilter categoriaContaContabil() {
        if (categoriaContaContabil == null) {
            setCategoriaContaContabil(new EnumCategoriaContaContabilFilter());
        }
        return categoriaContaContabil;
    }

    public void setCategoriaContaContabil(EnumCategoriaContaContabilFilter categoriaContaContabil) {
        this.categoriaContaContabil = categoriaContaContabil;
    }

    public BooleanFilter getAtiva() {
        return ativa;
    }

    public Optional<BooleanFilter> optionalAtiva() {
        return Optional.ofNullable(ativa);
    }

    public BooleanFilter ativa() {
        if (ativa == null) {
            setAtiva(new BooleanFilter());
        }
        return ativa;
    }

    public void setAtiva(BooleanFilter ativa) {
        this.ativa = ativa;
    }

    public LongFilter getMoedaCarteiraId() {
        return moedaCarteiraId;
    }

    public Optional<LongFilter> optionalMoedaCarteiraId() {
        return Optional.ofNullable(moedaCarteiraId);
    }

    public LongFilter moedaCarteiraId() {
        if (moedaCarteiraId == null) {
            setMoedaCarteiraId(new LongFilter());
        }
        return moedaCarteiraId;
    }

    public void setMoedaCarteiraId(LongFilter moedaCarteiraId) {
        this.moedaCarteiraId = moedaCarteiraId;
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
        final ContaContabilCriteria that = (ContaContabilCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(saldo, that.saldo) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(tipoContaContabil, that.tipoContaContabil) &&
            Objects.equals(categoriaContaContabil, that.categoriaContaContabil) &&
            Objects.equals(ativa, that.ativa) &&
            Objects.equals(moedaCarteiraId, that.moedaCarteiraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codigo,
            nome,
            saldo,
            descricao,
            tipoContaContabil,
            categoriaContaContabil,
            ativa,
            moedaCarteiraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaContabilCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalSaldo().map(f -> "saldo=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalTipoContaContabil().map(f -> "tipoContaContabil=" + f + ", ").orElse("") +
            optionalCategoriaContaContabil().map(f -> "categoriaContaContabil=" + f + ", ").orElse("") +
            optionalAtiva().map(f -> "ativa=" + f + ", ").orElse("") +
            optionalMoedaCarteiraId().map(f -> "moedaCarteiraId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
