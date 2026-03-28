package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumStatusDeposito;
import com.bankernel.domain.enumeration.EnumTipoDeposito;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Deposito} entity. This class is used
 * in {@link com.bankernel.web.rest.DepositoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depositos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepositoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoDeposito
     */
    public static class EnumTipoDepositoFilter extends Filter<EnumTipoDeposito> {

        public EnumTipoDepositoFilter() {}

        public EnumTipoDepositoFilter(EnumTipoDepositoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoDepositoFilter copy() {
            return new EnumTipoDepositoFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusDeposito
     */
    public static class EnumStatusDepositoFilter extends Filter<EnumStatusDeposito> {

        public EnumStatusDepositoFilter() {}

        public EnumStatusDepositoFilter(EnumStatusDepositoFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusDepositoFilter copy() {
            return new EnumStatusDepositoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valor;

    private BigDecimalFilter valorCreditado;

    private BigDecimalFilter valorSaldoCarteira;

    private EnumTipoDepositoFilter tipoDeposito;

    private EnumStatusDepositoFilter situacaoDeposito;

    private StringFilter numeroReferencia;

    private StringFilter referenciaExterna;

    private StringFilter descricao;

    private StringFilter motivoRejeicao;

    private BooleanFilter contabilizado;

    private StringFilter nomeUsuarioFixo;

    private IntegerFilter numeroParcela;

    private LongFilter transacaoId;

    private LongFilter carteiraId;

    private LongFilter moedaCarteiraId;

    private LongFilter usuarioId;

    private LongFilter contaBancariaId;

    private LongFilter depositoPixId;

    private LongFilter depositoBoletoId;

    private Boolean distinct;

    public DepositoCriteria() {}

    public DepositoCriteria(DepositoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valor = other.optionalValor().map(BigDecimalFilter::copy).orElse(null);
        this.valorCreditado = other.optionalValorCreditado().map(BigDecimalFilter::copy).orElse(null);
        this.valorSaldoCarteira = other.optionalValorSaldoCarteira().map(BigDecimalFilter::copy).orElse(null);
        this.tipoDeposito = other.optionalTipoDeposito().map(EnumTipoDepositoFilter::copy).orElse(null);
        this.situacaoDeposito = other.optionalSituacaoDeposito().map(EnumStatusDepositoFilter::copy).orElse(null);
        this.numeroReferencia = other.optionalNumeroReferencia().map(StringFilter::copy).orElse(null);
        this.referenciaExterna = other.optionalReferenciaExterna().map(StringFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.motivoRejeicao = other.optionalMotivoRejeicao().map(StringFilter::copy).orElse(null);
        this.contabilizado = other.optionalContabilizado().map(BooleanFilter::copy).orElse(null);
        this.nomeUsuarioFixo = other.optionalNomeUsuarioFixo().map(StringFilter::copy).orElse(null);
        this.numeroParcela = other.optionalNumeroParcela().map(IntegerFilter::copy).orElse(null);
        this.transacaoId = other.optionalTransacaoId().map(LongFilter::copy).orElse(null);
        this.carteiraId = other.optionalCarteiraId().map(LongFilter::copy).orElse(null);
        this.moedaCarteiraId = other.optionalMoedaCarteiraId().map(LongFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.contaBancariaId = other.optionalContaBancariaId().map(LongFilter::copy).orElse(null);
        this.depositoPixId = other.optionalDepositoPixId().map(LongFilter::copy).orElse(null);
        this.depositoBoletoId = other.optionalDepositoBoletoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DepositoCriteria copy() {
        return new DepositoCriteria(this);
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

    public BigDecimalFilter getValorCreditado() {
        return valorCreditado;
    }

    public Optional<BigDecimalFilter> optionalValorCreditado() {
        return Optional.ofNullable(valorCreditado);
    }

    public BigDecimalFilter valorCreditado() {
        if (valorCreditado == null) {
            setValorCreditado(new BigDecimalFilter());
        }
        return valorCreditado;
    }

    public void setValorCreditado(BigDecimalFilter valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimalFilter getValorSaldoCarteira() {
        return valorSaldoCarteira;
    }

    public Optional<BigDecimalFilter> optionalValorSaldoCarteira() {
        return Optional.ofNullable(valorSaldoCarteira);
    }

    public BigDecimalFilter valorSaldoCarteira() {
        if (valorSaldoCarteira == null) {
            setValorSaldoCarteira(new BigDecimalFilter());
        }
        return valorSaldoCarteira;
    }

    public void setValorSaldoCarteira(BigDecimalFilter valorSaldoCarteira) {
        this.valorSaldoCarteira = valorSaldoCarteira;
    }

    public EnumTipoDepositoFilter getTipoDeposito() {
        return tipoDeposito;
    }

    public Optional<EnumTipoDepositoFilter> optionalTipoDeposito() {
        return Optional.ofNullable(tipoDeposito);
    }

    public EnumTipoDepositoFilter tipoDeposito() {
        if (tipoDeposito == null) {
            setTipoDeposito(new EnumTipoDepositoFilter());
        }
        return tipoDeposito;
    }

    public void setTipoDeposito(EnumTipoDepositoFilter tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    public EnumStatusDepositoFilter getSituacaoDeposito() {
        return situacaoDeposito;
    }

    public Optional<EnumStatusDepositoFilter> optionalSituacaoDeposito() {
        return Optional.ofNullable(situacaoDeposito);
    }

    public EnumStatusDepositoFilter situacaoDeposito() {
        if (situacaoDeposito == null) {
            setSituacaoDeposito(new EnumStatusDepositoFilter());
        }
        return situacaoDeposito;
    }

    public void setSituacaoDeposito(EnumStatusDepositoFilter situacaoDeposito) {
        this.situacaoDeposito = situacaoDeposito;
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

    public StringFilter getReferenciaExterna() {
        return referenciaExterna;
    }

    public Optional<StringFilter> optionalReferenciaExterna() {
        return Optional.ofNullable(referenciaExterna);
    }

    public StringFilter referenciaExterna() {
        if (referenciaExterna == null) {
            setReferenciaExterna(new StringFilter());
        }
        return referenciaExterna;
    }

    public void setReferenciaExterna(StringFilter referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
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

    public StringFilter getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public Optional<StringFilter> optionalMotivoRejeicao() {
        return Optional.ofNullable(motivoRejeicao);
    }

    public StringFilter motivoRejeicao() {
        if (motivoRejeicao == null) {
            setMotivoRejeicao(new StringFilter());
        }
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(StringFilter motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public BooleanFilter getContabilizado() {
        return contabilizado;
    }

    public Optional<BooleanFilter> optionalContabilizado() {
        return Optional.ofNullable(contabilizado);
    }

    public BooleanFilter contabilizado() {
        if (contabilizado == null) {
            setContabilizado(new BooleanFilter());
        }
        return contabilizado;
    }

    public void setContabilizado(BooleanFilter contabilizado) {
        this.contabilizado = contabilizado;
    }

    public StringFilter getNomeUsuarioFixo() {
        return nomeUsuarioFixo;
    }

    public Optional<StringFilter> optionalNomeUsuarioFixo() {
        return Optional.ofNullable(nomeUsuarioFixo);
    }

    public StringFilter nomeUsuarioFixo() {
        if (nomeUsuarioFixo == null) {
            setNomeUsuarioFixo(new StringFilter());
        }
        return nomeUsuarioFixo;
    }

    public void setNomeUsuarioFixo(StringFilter nomeUsuarioFixo) {
        this.nomeUsuarioFixo = nomeUsuarioFixo;
    }

    public IntegerFilter getNumeroParcela() {
        return numeroParcela;
    }

    public Optional<IntegerFilter> optionalNumeroParcela() {
        return Optional.ofNullable(numeroParcela);
    }

    public IntegerFilter numeroParcela() {
        if (numeroParcela == null) {
            setNumeroParcela(new IntegerFilter());
        }
        return numeroParcela;
    }

    public void setNumeroParcela(IntegerFilter numeroParcela) {
        this.numeroParcela = numeroParcela;
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

    public LongFilter getContaBancariaId() {
        return contaBancariaId;
    }

    public Optional<LongFilter> optionalContaBancariaId() {
        return Optional.ofNullable(contaBancariaId);
    }

    public LongFilter contaBancariaId() {
        if (contaBancariaId == null) {
            setContaBancariaId(new LongFilter());
        }
        return contaBancariaId;
    }

    public void setContaBancariaId(LongFilter contaBancariaId) {
        this.contaBancariaId = contaBancariaId;
    }

    public LongFilter getDepositoPixId() {
        return depositoPixId;
    }

    public Optional<LongFilter> optionalDepositoPixId() {
        return Optional.ofNullable(depositoPixId);
    }

    public LongFilter depositoPixId() {
        if (depositoPixId == null) {
            setDepositoPixId(new LongFilter());
        }
        return depositoPixId;
    }

    public void setDepositoPixId(LongFilter depositoPixId) {
        this.depositoPixId = depositoPixId;
    }

    public LongFilter getDepositoBoletoId() {
        return depositoBoletoId;
    }

    public Optional<LongFilter> optionalDepositoBoletoId() {
        return Optional.ofNullable(depositoBoletoId);
    }

    public LongFilter depositoBoletoId() {
        if (depositoBoletoId == null) {
            setDepositoBoletoId(new LongFilter());
        }
        return depositoBoletoId;
    }

    public void setDepositoBoletoId(LongFilter depositoBoletoId) {
        this.depositoBoletoId = depositoBoletoId;
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
        final DepositoCriteria that = (DepositoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(valorCreditado, that.valorCreditado) &&
            Objects.equals(valorSaldoCarteira, that.valorSaldoCarteira) &&
            Objects.equals(tipoDeposito, that.tipoDeposito) &&
            Objects.equals(situacaoDeposito, that.situacaoDeposito) &&
            Objects.equals(numeroReferencia, that.numeroReferencia) &&
            Objects.equals(referenciaExterna, that.referenciaExterna) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(motivoRejeicao, that.motivoRejeicao) &&
            Objects.equals(contabilizado, that.contabilizado) &&
            Objects.equals(nomeUsuarioFixo, that.nomeUsuarioFixo) &&
            Objects.equals(numeroParcela, that.numeroParcela) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(carteiraId, that.carteiraId) &&
            Objects.equals(moedaCarteiraId, that.moedaCarteiraId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(contaBancariaId, that.contaBancariaId) &&
            Objects.equals(depositoPixId, that.depositoPixId) &&
            Objects.equals(depositoBoletoId, that.depositoBoletoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valor,
            valorCreditado,
            valorSaldoCarteira,
            tipoDeposito,
            situacaoDeposito,
            numeroReferencia,
            referenciaExterna,
            descricao,
            motivoRejeicao,
            contabilizado,
            nomeUsuarioFixo,
            numeroParcela,
            transacaoId,
            carteiraId,
            moedaCarteiraId,
            usuarioId,
            contaBancariaId,
            depositoPixId,
            depositoBoletoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValor().map(f -> "valor=" + f + ", ").orElse("") +
            optionalValorCreditado().map(f -> "valorCreditado=" + f + ", ").orElse("") +
            optionalValorSaldoCarteira().map(f -> "valorSaldoCarteira=" + f + ", ").orElse("") +
            optionalTipoDeposito().map(f -> "tipoDeposito=" + f + ", ").orElse("") +
            optionalSituacaoDeposito().map(f -> "situacaoDeposito=" + f + ", ").orElse("") +
            optionalNumeroReferencia().map(f -> "numeroReferencia=" + f + ", ").orElse("") +
            optionalReferenciaExterna().map(f -> "referenciaExterna=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalMotivoRejeicao().map(f -> "motivoRejeicao=" + f + ", ").orElse("") +
            optionalContabilizado().map(f -> "contabilizado=" + f + ", ").orElse("") +
            optionalNomeUsuarioFixo().map(f -> "nomeUsuarioFixo=" + f + ", ").orElse("") +
            optionalNumeroParcela().map(f -> "numeroParcela=" + f + ", ").orElse("") +
            optionalTransacaoId().map(f -> "transacaoId=" + f + ", ").orElse("") +
            optionalCarteiraId().map(f -> "carteiraId=" + f + ", ").orElse("") +
            optionalMoedaCarteiraId().map(f -> "moedaCarteiraId=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalContaBancariaId().map(f -> "contaBancariaId=" + f + ", ").orElse("") +
            optionalDepositoPixId().map(f -> "depositoPixId=" + f + ", ").orElse("") +
            optionalDepositoBoletoId().map(f -> "depositoBoletoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
