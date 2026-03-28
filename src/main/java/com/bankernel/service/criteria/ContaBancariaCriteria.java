package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumTipoConta;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.ContaBancaria} entity. This class is used
 * in {@link com.bankernel.web.rest.ContaBancariaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conta-bancarias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaBancariaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoConta
     */
    public static class EnumTipoContaFilter extends Filter<EnumTipoConta> {

        public EnumTipoContaFilter() {}

        public EnumTipoContaFilter(EnumTipoContaFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoContaFilter copy() {
            return new EnumTipoContaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeTitular;

    private StringFilter numeroConta;

    private StringFilter agencia;

    private StringFilter nomeBanco;

    private StringFilter codigoBanco;

    private StringFilter ispb;

    private StringFilter codigoSwift;

    private EnumTipoContaFilter tipoConta;

    private BooleanFilter ativa;

    private LongFilter usuarioId;

    private LongFilter paisId;

    private LongFilter moedaId;

    private Boolean distinct;

    public ContaBancariaCriteria() {}

    public ContaBancariaCriteria(ContaBancariaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nomeTitular = other.optionalNomeTitular().map(StringFilter::copy).orElse(null);
        this.numeroConta = other.optionalNumeroConta().map(StringFilter::copy).orElse(null);
        this.agencia = other.optionalAgencia().map(StringFilter::copy).orElse(null);
        this.nomeBanco = other.optionalNomeBanco().map(StringFilter::copy).orElse(null);
        this.codigoBanco = other.optionalCodigoBanco().map(StringFilter::copy).orElse(null);
        this.ispb = other.optionalIspb().map(StringFilter::copy).orElse(null);
        this.codigoSwift = other.optionalCodigoSwift().map(StringFilter::copy).orElse(null);
        this.tipoConta = other.optionalTipoConta().map(EnumTipoContaFilter::copy).orElse(null);
        this.ativa = other.optionalAtiva().map(BooleanFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.paisId = other.optionalPaisId().map(LongFilter::copy).orElse(null);
        this.moedaId = other.optionalMoedaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContaBancariaCriteria copy() {
        return new ContaBancariaCriteria(this);
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

    public StringFilter getNomeTitular() {
        return nomeTitular;
    }

    public Optional<StringFilter> optionalNomeTitular() {
        return Optional.ofNullable(nomeTitular);
    }

    public StringFilter nomeTitular() {
        if (nomeTitular == null) {
            setNomeTitular(new StringFilter());
        }
        return nomeTitular;
    }

    public void setNomeTitular(StringFilter nomeTitular) {
        this.nomeTitular = nomeTitular;
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

    public StringFilter getAgencia() {
        return agencia;
    }

    public Optional<StringFilter> optionalAgencia() {
        return Optional.ofNullable(agencia);
    }

    public StringFilter agencia() {
        if (agencia == null) {
            setAgencia(new StringFilter());
        }
        return agencia;
    }

    public void setAgencia(StringFilter agencia) {
        this.agencia = agencia;
    }

    public StringFilter getNomeBanco() {
        return nomeBanco;
    }

    public Optional<StringFilter> optionalNomeBanco() {
        return Optional.ofNullable(nomeBanco);
    }

    public StringFilter nomeBanco() {
        if (nomeBanco == null) {
            setNomeBanco(new StringFilter());
        }
        return nomeBanco;
    }

    public void setNomeBanco(StringFilter nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public StringFilter getCodigoBanco() {
        return codigoBanco;
    }

    public Optional<StringFilter> optionalCodigoBanco() {
        return Optional.ofNullable(codigoBanco);
    }

    public StringFilter codigoBanco() {
        if (codigoBanco == null) {
            setCodigoBanco(new StringFilter());
        }
        return codigoBanco;
    }

    public void setCodigoBanco(StringFilter codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public StringFilter getIspb() {
        return ispb;
    }

    public Optional<StringFilter> optionalIspb() {
        return Optional.ofNullable(ispb);
    }

    public StringFilter ispb() {
        if (ispb == null) {
            setIspb(new StringFilter());
        }
        return ispb;
    }

    public void setIspb(StringFilter ispb) {
        this.ispb = ispb;
    }

    public StringFilter getCodigoSwift() {
        return codigoSwift;
    }

    public Optional<StringFilter> optionalCodigoSwift() {
        return Optional.ofNullable(codigoSwift);
    }

    public StringFilter codigoSwift() {
        if (codigoSwift == null) {
            setCodigoSwift(new StringFilter());
        }
        return codigoSwift;
    }

    public void setCodigoSwift(StringFilter codigoSwift) {
        this.codigoSwift = codigoSwift;
    }

    public EnumTipoContaFilter getTipoConta() {
        return tipoConta;
    }

    public Optional<EnumTipoContaFilter> optionalTipoConta() {
        return Optional.ofNullable(tipoConta);
    }

    public EnumTipoContaFilter tipoConta() {
        if (tipoConta == null) {
            setTipoConta(new EnumTipoContaFilter());
        }
        return tipoConta;
    }

    public void setTipoConta(EnumTipoContaFilter tipoConta) {
        this.tipoConta = tipoConta;
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

    public LongFilter getPaisId() {
        return paisId;
    }

    public Optional<LongFilter> optionalPaisId() {
        return Optional.ofNullable(paisId);
    }

    public LongFilter paisId() {
        if (paisId == null) {
            setPaisId(new LongFilter());
        }
        return paisId;
    }

    public void setPaisId(LongFilter paisId) {
        this.paisId = paisId;
    }

    public LongFilter getMoedaId() {
        return moedaId;
    }

    public Optional<LongFilter> optionalMoedaId() {
        return Optional.ofNullable(moedaId);
    }

    public LongFilter moedaId() {
        if (moedaId == null) {
            setMoedaId(new LongFilter());
        }
        return moedaId;
    }

    public void setMoedaId(LongFilter moedaId) {
        this.moedaId = moedaId;
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
        final ContaBancariaCriteria that = (ContaBancariaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomeTitular, that.nomeTitular) &&
            Objects.equals(numeroConta, that.numeroConta) &&
            Objects.equals(agencia, that.agencia) &&
            Objects.equals(nomeBanco, that.nomeBanco) &&
            Objects.equals(codigoBanco, that.codigoBanco) &&
            Objects.equals(ispb, that.ispb) &&
            Objects.equals(codigoSwift, that.codigoSwift) &&
            Objects.equals(tipoConta, that.tipoConta) &&
            Objects.equals(ativa, that.ativa) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(paisId, that.paisId) &&
            Objects.equals(moedaId, that.moedaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nomeTitular,
            numeroConta,
            agencia,
            nomeBanco,
            codigoBanco,
            ispb,
            codigoSwift,
            tipoConta,
            ativa,
            usuarioId,
            paisId,
            moedaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaBancariaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNomeTitular().map(f -> "nomeTitular=" + f + ", ").orElse("") +
            optionalNumeroConta().map(f -> "numeroConta=" + f + ", ").orElse("") +
            optionalAgencia().map(f -> "agencia=" + f + ", ").orElse("") +
            optionalNomeBanco().map(f -> "nomeBanco=" + f + ", ").orElse("") +
            optionalCodigoBanco().map(f -> "codigoBanco=" + f + ", ").orElse("") +
            optionalIspb().map(f -> "ispb=" + f + ", ").orElse("") +
            optionalCodigoSwift().map(f -> "codigoSwift=" + f + ", ").orElse("") +
            optionalTipoConta().map(f -> "tipoConta=" + f + ", ").orElse("") +
            optionalAtiva().map(f -> "ativa=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalPaisId().map(f -> "paisId=" + f + ", ").orElse("") +
            optionalMoedaId().map(f -> "moedaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
