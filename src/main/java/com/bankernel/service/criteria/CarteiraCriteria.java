package com.bankernel.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Carteira} entity. This class is used
 * in {@link com.bankernel.web.rest.CarteiraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /carteiras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarteiraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter saldo;

    private BigDecimalFilter limiteNegativo;

    private BigDecimalFilter valorCongelado;

    private StringFilter numeroConta;

    private BooleanFilter ativa;

    private LongFilter moedaCarteiraId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public CarteiraCriteria() {}

    public CarteiraCriteria(CarteiraCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.saldo = other.optionalSaldo().map(BigDecimalFilter::copy).orElse(null);
        this.limiteNegativo = other.optionalLimiteNegativo().map(BigDecimalFilter::copy).orElse(null);
        this.valorCongelado = other.optionalValorCongelado().map(BigDecimalFilter::copy).orElse(null);
        this.numeroConta = other.optionalNumeroConta().map(StringFilter::copy).orElse(null);
        this.ativa = other.optionalAtiva().map(BooleanFilter::copy).orElse(null);
        this.moedaCarteiraId = other.optionalMoedaCarteiraId().map(LongFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CarteiraCriteria copy() {
        return new CarteiraCriteria(this);
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

    public BigDecimalFilter getLimiteNegativo() {
        return limiteNegativo;
    }

    public Optional<BigDecimalFilter> optionalLimiteNegativo() {
        return Optional.ofNullable(limiteNegativo);
    }

    public BigDecimalFilter limiteNegativo() {
        if (limiteNegativo == null) {
            setLimiteNegativo(new BigDecimalFilter());
        }
        return limiteNegativo;
    }

    public void setLimiteNegativo(BigDecimalFilter limiteNegativo) {
        this.limiteNegativo = limiteNegativo;
    }

    public BigDecimalFilter getValorCongelado() {
        return valorCongelado;
    }

    public Optional<BigDecimalFilter> optionalValorCongelado() {
        return Optional.ofNullable(valorCongelado);
    }

    public BigDecimalFilter valorCongelado() {
        if (valorCongelado == null) {
            setValorCongelado(new BigDecimalFilter());
        }
        return valorCongelado;
    }

    public void setValorCongelado(BigDecimalFilter valorCongelado) {
        this.valorCongelado = valorCongelado;
    }

    public StringFilter getNumeroConta() {
        return numeroConta;
    }

    public Optional<StringFilter> optionalNumeroConta() {
        return Optional.ofNullable(numeroConta);
    }

    public StringFilter numeroConta() {
        if (numeroConta == null) {
            setNumeroConta(new StringFilter());
        }
        return numeroConta;
    }

    public void setNumeroConta(StringFilter numeroConta) {
        this.numeroConta = numeroConta;
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
        final CarteiraCriteria that = (CarteiraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(saldo, that.saldo) &&
            Objects.equals(limiteNegativo, that.limiteNegativo) &&
            Objects.equals(valorCongelado, that.valorCongelado) &&
            Objects.equals(numeroConta, that.numeroConta) &&
            Objects.equals(ativa, that.ativa) &&
            Objects.equals(moedaCarteiraId, that.moedaCarteiraId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, saldo, limiteNegativo, valorCongelado, numeroConta, ativa, moedaCarteiraId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteiraCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSaldo().map(f -> "saldo=" + f + ", ").orElse("") +
            optionalLimiteNegativo().map(f -> "limiteNegativo=" + f + ", ").orElse("") +
            optionalValorCongelado().map(f -> "valorCongelado=" + f + ", ").orElse("") +
            optionalNumeroConta().map(f -> "numeroConta=" + f + ", ").orElse("") +
            optionalAtiva().map(f -> "ativa=" + f + ", ").orElse("") +
            optionalMoedaCarteiraId().map(f -> "moedaCarteiraId=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
