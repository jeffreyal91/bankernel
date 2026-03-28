package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HistoricoOperacaoCriteriaTest {

    @Test
    void newHistoricoOperacaoCriteriaHasAllFiltersNullTest() {
        var historicoOperacaoCriteria = new HistoricoOperacaoCriteria();
        assertThat(historicoOperacaoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void historicoOperacaoCriteriaFluentMethodsCreatesFiltersTest() {
        var historicoOperacaoCriteria = new HistoricoOperacaoCriteria();

        setAllFilters(historicoOperacaoCriteria);

        assertThat(historicoOperacaoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void historicoOperacaoCriteriaCopyCreatesNullFilterTest() {
        var historicoOperacaoCriteria = new HistoricoOperacaoCriteria();
        var copy = historicoOperacaoCriteria.copy();

        assertThat(historicoOperacaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(historicoOperacaoCriteria)
        );
    }

    @Test
    void historicoOperacaoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var historicoOperacaoCriteria = new HistoricoOperacaoCriteria();
        setAllFilters(historicoOperacaoCriteria);

        var copy = historicoOperacaoCriteria.copy();

        assertThat(historicoOperacaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(historicoOperacaoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var historicoOperacaoCriteria = new HistoricoOperacaoCriteria();

        assertThat(historicoOperacaoCriteria).hasToString("HistoricoOperacaoCriteria{}");
    }

    private static void setAllFilters(HistoricoOperacaoCriteria historicoOperacaoCriteria) {
        historicoOperacaoCriteria.id();
        historicoOperacaoCriteria.valor();
        historicoOperacaoCriteria.saldoApos();
        historicoOperacaoCriteria.descricao();
        historicoOperacaoCriteria.tipoSimbolo();
        historicoOperacaoCriteria.numeroReferencia();
        historicoOperacaoCriteria.tipoHistorico();
        historicoOperacaoCriteria.situacaoHistorico();
        historicoOperacaoCriteria.tipoEntidadeOrigem();
        historicoOperacaoCriteria.idEntidadeOrigem();
        historicoOperacaoCriteria.transacaoId();
        historicoOperacaoCriteria.usuarioId();
        historicoOperacaoCriteria.carteiraId();
        historicoOperacaoCriteria.distinct();
    }

    private static Condition<HistoricoOperacaoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValor()) &&
                condition.apply(criteria.getSaldoApos()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getTipoSimbolo()) &&
                condition.apply(criteria.getNumeroReferencia()) &&
                condition.apply(criteria.getTipoHistorico()) &&
                condition.apply(criteria.getSituacaoHistorico()) &&
                condition.apply(criteria.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getCarteiraId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HistoricoOperacaoCriteria> copyFiltersAre(
        HistoricoOperacaoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValor(), copy.getValor()) &&
                condition.apply(criteria.getSaldoApos(), copy.getSaldoApos()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getTipoSimbolo(), copy.getTipoSimbolo()) &&
                condition.apply(criteria.getNumeroReferencia(), copy.getNumeroReferencia()) &&
                condition.apply(criteria.getTipoHistorico(), copy.getTipoHistorico()) &&
                condition.apply(criteria.getSituacaoHistorico(), copy.getSituacaoHistorico()) &&
                condition.apply(criteria.getTipoEntidadeOrigem(), copy.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem(), copy.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getCarteiraId(), copy.getCarteiraId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
