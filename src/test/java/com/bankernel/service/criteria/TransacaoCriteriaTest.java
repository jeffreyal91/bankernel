package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransacaoCriteriaTest {

    @Test
    void newTransacaoCriteriaHasAllFiltersNullTest() {
        var transacaoCriteria = new TransacaoCriteria();
        assertThat(transacaoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transacaoCriteriaFluentMethodsCreatesFiltersTest() {
        var transacaoCriteria = new TransacaoCriteria();

        setAllFilters(transacaoCriteria);

        assertThat(transacaoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transacaoCriteriaCopyCreatesNullFilterTest() {
        var transacaoCriteria = new TransacaoCriteria();
        var copy = transacaoCriteria.copy();

        assertThat(transacaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transacaoCriteria)
        );
    }

    @Test
    void transacaoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transacaoCriteria = new TransacaoCriteria();
        setAllFilters(transacaoCriteria);

        var copy = transacaoCriteria.copy();

        assertThat(transacaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transacaoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transacaoCriteria = new TransacaoCriteria();

        assertThat(transacaoCriteria).hasToString("TransacaoCriteria{}");
    }

    private static void setAllFilters(TransacaoCriteria transacaoCriteria) {
        transacaoCriteria.id();
        transacaoCriteria.valorEnviado();
        transacaoCriteria.valorRecebido();
        transacaoCriteria.descricao();
        transacaoCriteria.estornada();
        transacaoCriteria.tipoTransacao();
        transacaoCriteria.tipoPagamento();
        transacaoCriteria.situacao();
        transacaoCriteria.ativa();
        transacaoCriteria.tipoEntidadeOrigem();
        transacaoCriteria.idEntidadeOrigem();
        transacaoCriteria.carteiraOrigemId();
        transacaoCriteria.carteiraDestinoId();
        transacaoCriteria.moedaOrigemId();
        transacaoCriteria.moedaDestinoId();
        transacaoCriteria.distinct();
    }

    private static Condition<TransacaoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValorEnviado()) &&
                condition.apply(criteria.getValorRecebido()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getEstornada()) &&
                condition.apply(criteria.getTipoTransacao()) &&
                condition.apply(criteria.getTipoPagamento()) &&
                condition.apply(criteria.getSituacao()) &&
                condition.apply(criteria.getAtiva()) &&
                condition.apply(criteria.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getCarteiraOrigemId()) &&
                condition.apply(criteria.getCarteiraDestinoId()) &&
                condition.apply(criteria.getMoedaOrigemId()) &&
                condition.apply(criteria.getMoedaDestinoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransacaoCriteria> copyFiltersAre(TransacaoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValorEnviado(), copy.getValorEnviado()) &&
                condition.apply(criteria.getValorRecebido(), copy.getValorRecebido()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getEstornada(), copy.getEstornada()) &&
                condition.apply(criteria.getTipoTransacao(), copy.getTipoTransacao()) &&
                condition.apply(criteria.getTipoPagamento(), copy.getTipoPagamento()) &&
                condition.apply(criteria.getSituacao(), copy.getSituacao()) &&
                condition.apply(criteria.getAtiva(), copy.getAtiva()) &&
                condition.apply(criteria.getTipoEntidadeOrigem(), copy.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem(), copy.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getCarteiraOrigemId(), copy.getCarteiraOrigemId()) &&
                condition.apply(criteria.getCarteiraDestinoId(), copy.getCarteiraDestinoId()) &&
                condition.apply(criteria.getMoedaOrigemId(), copy.getMoedaOrigemId()) &&
                condition.apply(criteria.getMoedaDestinoId(), copy.getMoedaDestinoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
