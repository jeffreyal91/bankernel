package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumStatusTransacao;
import com.bankernel.domain.enumeration.EnumTipoPagamento;
import com.bankernel.domain.enumeration.EnumTipoTransacao;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Transacao} entity. This class is used
 * in {@link com.bankernel.web.rest.TransacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoTransacao
     */
    public static class EnumTipoTransacaoFilter extends Filter<EnumTipoTransacao> {

        public EnumTipoTransacaoFilter() {}

        public EnumTipoTransacaoFilter(EnumTipoTransacaoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoTransacaoFilter copy() {
            return new EnumTipoTransacaoFilter(this);
        }
    }

    /**
     * Class for filtering EnumTipoPagamento
     */
    public static class EnumTipoPagamentoFilter extends Filter<EnumTipoPagamento> {

        public EnumTipoPagamentoFilter() {}

        public EnumTipoPagamentoFilter(EnumTipoPagamentoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoPagamentoFilter copy() {
            return new EnumTipoPagamentoFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusTransacao
     */
    public static class EnumStatusTransacaoFilter extends Filter<EnumStatusTransacao> {

        public EnumStatusTransacaoFilter() {}

        public EnumStatusTransacaoFilter(EnumStatusTransacaoFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusTransacaoFilter copy() {
            return new EnumStatusTransacaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valorEnviado;

    private BigDecimalFilter valorRecebido;

    private StringFilter descricao;

    private BooleanFilter estornada;

    private EnumTipoTransacaoFilter tipoTransacao;

    private EnumTipoPagamentoFilter tipoPagamento;

    private EnumStatusTransacaoFilter situacao;

    private BooleanFilter ativa;

    private StringFilter tipoEntidadeOrigem;

    private LongFilter idEntidadeOrigem;

    private LongFilter carteiraOrigemId;

    private LongFilter carteiraDestinoId;

    private LongFilter moedaOrigemId;

    private LongFilter moedaDestinoId;

    private Boolean distinct;

    public TransacaoCriteria() {}

    public TransacaoCriteria(TransacaoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valorEnviado = other.optionalValorEnviado().map(BigDecimalFilter::copy).orElse(null);
        this.valorRecebido = other.optionalValorRecebido().map(BigDecimalFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.estornada = other.optionalEstornada().map(BooleanFilter::copy).orElse(null);
        this.tipoTransacao = other.optionalTipoTransacao().map(EnumTipoTransacaoFilter::copy).orElse(null);
        this.tipoPagamento = other.optionalTipoPagamento().map(EnumTipoPagamentoFilter::copy).orElse(null);
        this.situacao = other.optionalSituacao().map(EnumStatusTransacaoFilter::copy).orElse(null);
        this.ativa = other.optionalAtiva().map(BooleanFilter::copy).orElse(null);
        this.tipoEntidadeOrigem = other.optionalTipoEntidadeOrigem().map(StringFilter::copy).orElse(null);
        this.idEntidadeOrigem = other.optionalIdEntidadeOrigem().map(LongFilter::copy).orElse(null);
        this.carteiraOrigemId = other.optionalCarteiraOrigemId().map(LongFilter::copy).orElse(null);
        this.carteiraDestinoId = other.optionalCarteiraDestinoId().map(LongFilter::copy).orElse(null);
        this.moedaOrigemId = other.optionalMoedaOrigemId().map(LongFilter::copy).orElse(null);
        this.moedaDestinoId = other.optionalMoedaDestinoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransacaoCriteria copy() {
        return new TransacaoCriteria(this);
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

    public BigDecimalFilter getValorEnviado() {
        return valorEnviado;
    }

    public Optional<BigDecimalFilter> optionalValorEnviado() {
        return Optional.ofNullable(valorEnviado);
    }

    public BigDecimalFilter valorEnviado() {
        if (valorEnviado == null) {
            setValorEnviado(new BigDecimalFilter());
        }
        return valorEnviado;
    }

    public void setValorEnviado(BigDecimalFilter valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public BigDecimalFilter getValorRecebido() {
        return valorRecebido;
    }

    public Optional<BigDecimalFilter> optionalValorRecebido() {
        return Optional.ofNullable(valorRecebido);
    }

    public BigDecimalFilter valorRecebido() {
        if (valorRecebido == null) {
            setValorRecebido(new BigDecimalFilter());
        }
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimalFilter valorRecebido) {
        this.valorRecebido = valorRecebido;
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

    public BooleanFilter getEstornada() {
        return estornada;
    }

    public Optional<BooleanFilter> optionalEstornada() {
        return Optional.ofNullable(estornada);
    }

    public BooleanFilter estornada() {
        if (estornada == null) {
            setEstornada(new BooleanFilter());
        }
        return estornada;
    }

    public void setEstornada(BooleanFilter estornada) {
        this.estornada = estornada;
    }

    public EnumTipoTransacaoFilter getTipoTransacao() {
        return tipoTransacao;
    }

    public Optional<EnumTipoTransacaoFilter> optionalTipoTransacao() {
        return Optional.ofNullable(tipoTransacao);
    }

    public EnumTipoTransacaoFilter tipoTransacao() {
        if (tipoTransacao == null) {
            setTipoTransacao(new EnumTipoTransacaoFilter());
        }
        return tipoTransacao;
    }

    public void setTipoTransacao(EnumTipoTransacaoFilter tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public EnumTipoPagamentoFilter getTipoPagamento() {
        return tipoPagamento;
    }

    public Optional<EnumTipoPagamentoFilter> optionalTipoPagamento() {
        return Optional.ofNullable(tipoPagamento);
    }

    public EnumTipoPagamentoFilter tipoPagamento() {
        if (tipoPagamento == null) {
            setTipoPagamento(new EnumTipoPagamentoFilter());
        }
        return tipoPagamento;
    }

    public void setTipoPagamento(EnumTipoPagamentoFilter tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public EnumStatusTransacaoFilter getSituacao() {
        return situacao;
    }

    public Optional<EnumStatusTransacaoFilter> optionalSituacao() {
        return Optional.ofNullable(situacao);
    }

    public EnumStatusTransacaoFilter situacao() {
        if (situacao == null) {
            setSituacao(new EnumStatusTransacaoFilter());
        }
        return situacao;
    }

    public void setSituacao(EnumStatusTransacaoFilter situacao) {
        this.situacao = situacao;
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

    public LongFilter getCarteiraOrigemId() {
        return carteiraOrigemId;
    }

    public Optional<LongFilter> optionalCarteiraOrigemId() {
        return Optional.ofNullable(carteiraOrigemId);
    }

    public LongFilter carteiraOrigemId() {
        if (carteiraOrigemId == null) {
            setCarteiraOrigemId(new LongFilter());
        }
        return carteiraOrigemId;
    }

    public void setCarteiraOrigemId(LongFilter carteiraOrigemId) {
        this.carteiraOrigemId = carteiraOrigemId;
    }

    public LongFilter getCarteiraDestinoId() {
        return carteiraDestinoId;
    }

    public Optional<LongFilter> optionalCarteiraDestinoId() {
        return Optional.ofNullable(carteiraDestinoId);
    }

    public LongFilter carteiraDestinoId() {
        if (carteiraDestinoId == null) {
            setCarteiraDestinoId(new LongFilter());
        }
        return carteiraDestinoId;
    }

    public void setCarteiraDestinoId(LongFilter carteiraDestinoId) {
        this.carteiraDestinoId = carteiraDestinoId;
    }

    public LongFilter getMoedaOrigemId() {
        return moedaOrigemId;
    }

    public Optional<LongFilter> optionalMoedaOrigemId() {
        return Optional.ofNullable(moedaOrigemId);
    }

    public LongFilter moedaOrigemId() {
        if (moedaOrigemId == null) {
            setMoedaOrigemId(new LongFilter());
        }
        return moedaOrigemId;
    }

    public void setMoedaOrigemId(LongFilter moedaOrigemId) {
        this.moedaOrigemId = moedaOrigemId;
    }

    public LongFilter getMoedaDestinoId() {
        return moedaDestinoId;
    }

    public Optional<LongFilter> optionalMoedaDestinoId() {
        return Optional.ofNullable(moedaDestinoId);
    }

    public LongFilter moedaDestinoId() {
        if (moedaDestinoId == null) {
            setMoedaDestinoId(new LongFilter());
        }
        return moedaDestinoId;
    }

    public void setMoedaDestinoId(LongFilter moedaDestinoId) {
        this.moedaDestinoId = moedaDestinoId;
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
        final TransacaoCriteria that = (TransacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valorEnviado, that.valorEnviado) &&
            Objects.equals(valorRecebido, that.valorRecebido) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(estornada, that.estornada) &&
            Objects.equals(tipoTransacao, that.tipoTransacao) &&
            Objects.equals(tipoPagamento, that.tipoPagamento) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(ativa, that.ativa) &&
            Objects.equals(tipoEntidadeOrigem, that.tipoEntidadeOrigem) &&
            Objects.equals(idEntidadeOrigem, that.idEntidadeOrigem) &&
            Objects.equals(carteiraOrigemId, that.carteiraOrigemId) &&
            Objects.equals(carteiraDestinoId, that.carteiraDestinoId) &&
            Objects.equals(moedaOrigemId, that.moedaOrigemId) &&
            Objects.equals(moedaDestinoId, that.moedaDestinoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valorEnviado,
            valorRecebido,
            descricao,
            estornada,
            tipoTransacao,
            tipoPagamento,
            situacao,
            ativa,
            tipoEntidadeOrigem,
            idEntidadeOrigem,
            carteiraOrigemId,
            carteiraDestinoId,
            moedaOrigemId,
            moedaDestinoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransacaoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValorEnviado().map(f -> "valorEnviado=" + f + ", ").orElse("") +
            optionalValorRecebido().map(f -> "valorRecebido=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalEstornada().map(f -> "estornada=" + f + ", ").orElse("") +
            optionalTipoTransacao().map(f -> "tipoTransacao=" + f + ", ").orElse("") +
            optionalTipoPagamento().map(f -> "tipoPagamento=" + f + ", ").orElse("") +
            optionalSituacao().map(f -> "situacao=" + f + ", ").orElse("") +
            optionalAtiva().map(f -> "ativa=" + f + ", ").orElse("") +
            optionalTipoEntidadeOrigem().map(f -> "tipoEntidadeOrigem=" + f + ", ").orElse("") +
            optionalIdEntidadeOrigem().map(f -> "idEntidadeOrigem=" + f + ", ").orElse("") +
            optionalCarteiraOrigemId().map(f -> "carteiraOrigemId=" + f + ", ").orElse("") +
            optionalCarteiraDestinoId().map(f -> "carteiraDestinoId=" + f + ", ").orElse("") +
            optionalMoedaOrigemId().map(f -> "moedaOrigemId=" + f + ", ").orElse("") +
            optionalMoedaDestinoId().map(f -> "moedaDestinoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
