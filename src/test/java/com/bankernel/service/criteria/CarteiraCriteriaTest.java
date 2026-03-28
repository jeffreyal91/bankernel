package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CarteiraCriteriaTest {

    @Test
    void newCarteiraCriteriaHasAllFiltersNullTest() {
        var carteiraCriteria = new CarteiraCriteria();
        assertThat(carteiraCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void carteiraCriteriaFluentMethodsCreatesFiltersTest() {
        var carteiraCriteria = new CarteiraCriteria();

        setAllFilters(carteiraCriteria);

        assertThat(carteiraCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void carteiraCriteriaCopyCreatesNullFilterTest() {
        var carteiraCriteria = new CarteiraCriteria();
        var copy = carteiraCriteria.copy();

        assertThat(carteiraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(carteiraCriteria)
        );
    }

    @Test
    void carteiraCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var carteiraCriteria = new CarteiraCriteria();
        setAllFilters(carteiraCriteria);

        var copy = carteiraCriteria.copy();

        assertThat(carteiraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(carteiraCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var carteiraCriteria = new CarteiraCriteria();

        assertThat(carteiraCriteria).hasToString("CarteiraCriteria{}");
    }

    private static void setAllFilters(CarteiraCriteria carteiraCriteria) {
        carteiraCriteria.id();
        carteiraCriteria.saldo();
        carteiraCriteria.limiteNegativo();
        carteiraCriteria.valorCongelado();
        carteiraCriteria.numeroConta();
        carteiraCriteria.ativa();
        carteiraCriteria.moedaCarteiraId();
        carteiraCriteria.usuarioId();
        carteiraCriteria.distinct();
    }

    private static Condition<CarteiraCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSaldo()) &&
                condition.apply(criteria.getLimiteNegativo()) &&
                condition.apply(criteria.getValorCongelado()) &&
                condition.apply(criteria.getNumeroConta()) &&
                condition.apply(criteria.getAtiva()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CarteiraCriteria> copyFiltersAre(CarteiraCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSaldo(), copy.getSaldo()) &&
                condition.apply(criteria.getLimiteNegativo(), copy.getLimiteNegativo()) &&
                condition.apply(criteria.getValorCongelado(), copy.getValorCongelado()) &&
                condition.apply(criteria.getNumeroConta(), copy.getNumeroConta()) &&
                condition.apply(criteria.getAtiva(), copy.getAtiva()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
