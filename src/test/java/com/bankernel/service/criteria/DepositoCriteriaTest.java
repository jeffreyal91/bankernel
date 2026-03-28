package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DepositoCriteriaTest {

    @Test
    void newDepositoCriteriaHasAllFiltersNullTest() {
        var depositoCriteria = new DepositoCriteria();
        assertThat(depositoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void depositoCriteriaFluentMethodsCreatesFiltersTest() {
        var depositoCriteria = new DepositoCriteria();

        setAllFilters(depositoCriteria);

        assertThat(depositoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void depositoCriteriaCopyCreatesNullFilterTest() {
        var depositoCriteria = new DepositoCriteria();
        var copy = depositoCriteria.copy();

        assertThat(depositoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(depositoCriteria)
        );
    }

    @Test
    void depositoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var depositoCriteria = new DepositoCriteria();
        setAllFilters(depositoCriteria);

        var copy = depositoCriteria.copy();

        assertThat(depositoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(depositoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var depositoCriteria = new DepositoCriteria();

        assertThat(depositoCriteria).hasToString("DepositoCriteria{}");
    }

    private static void setAllFilters(DepositoCriteria depositoCriteria) {
        depositoCriteria.id();
        depositoCriteria.valor();
        depositoCriteria.valorCreditado();
        depositoCriteria.valorSaldoCarteira();
        depositoCriteria.tipoDeposito();
        depositoCriteria.situacaoDeposito();
        depositoCriteria.numeroReferencia();
        depositoCriteria.referenciaExterna();
        depositoCriteria.descricao();
        depositoCriteria.motivoRejeicao();
        depositoCriteria.contabilizado();
        depositoCriteria.nomeUsuarioFixo();
        depositoCriteria.numeroParcela();
        depositoCriteria.transacaoId();
        depositoCriteria.carteiraId();
        depositoCriteria.moedaCarteiraId();
        depositoCriteria.usuarioId();
        depositoCriteria.contaBancariaId();
        depositoCriteria.depositoPixId();
        depositoCriteria.depositoBoletoId();
        depositoCriteria.distinct();
    }

    private static Condition<DepositoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValor()) &&
                condition.apply(criteria.getValorCreditado()) &&
                condition.apply(criteria.getValorSaldoCarteira()) &&
                condition.apply(criteria.getTipoDeposito()) &&
                condition.apply(criteria.getSituacaoDeposito()) &&
                condition.apply(criteria.getNumeroReferencia()) &&
                condition.apply(criteria.getReferenciaExterna()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getMotivoRejeicao()) &&
                condition.apply(criteria.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getNumeroParcela()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getCarteiraId()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getContaBancariaId()) &&
                condition.apply(criteria.getDepositoPixId()) &&
                condition.apply(criteria.getDepositoBoletoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DepositoCriteria> copyFiltersAre(DepositoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValor(), copy.getValor()) &&
                condition.apply(criteria.getValorCreditado(), copy.getValorCreditado()) &&
                condition.apply(criteria.getValorSaldoCarteira(), copy.getValorSaldoCarteira()) &&
                condition.apply(criteria.getTipoDeposito(), copy.getTipoDeposito()) &&
                condition.apply(criteria.getSituacaoDeposito(), copy.getSituacaoDeposito()) &&
                condition.apply(criteria.getNumeroReferencia(), copy.getNumeroReferencia()) &&
                condition.apply(criteria.getReferenciaExterna(), copy.getReferenciaExterna()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getMotivoRejeicao(), copy.getMotivoRejeicao()) &&
                condition.apply(criteria.getContabilizado(), copy.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo(), copy.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getNumeroParcela(), copy.getNumeroParcela()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getCarteiraId(), copy.getCarteiraId()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getContaBancariaId(), copy.getContaBancariaId()) &&
                condition.apply(criteria.getDepositoPixId(), copy.getDepositoPixId()) &&
                condition.apply(criteria.getDepositoBoletoId(), copy.getDepositoBoletoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
