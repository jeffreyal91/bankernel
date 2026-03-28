package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificacaoCriteriaTest {

    @Test
    void newNotificacaoCriteriaHasAllFiltersNullTest() {
        var notificacaoCriteria = new NotificacaoCriteria();
        assertThat(notificacaoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificacaoCriteriaFluentMethodsCreatesFiltersTest() {
        var notificacaoCriteria = new NotificacaoCriteria();

        setAllFilters(notificacaoCriteria);

        assertThat(notificacaoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificacaoCriteriaCopyCreatesNullFilterTest() {
        var notificacaoCriteria = new NotificacaoCriteria();
        var copy = notificacaoCriteria.copy();

        assertThat(notificacaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificacaoCriteria)
        );
    }

    @Test
    void notificacaoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificacaoCriteria = new NotificacaoCriteria();
        setAllFilters(notificacaoCriteria);

        var copy = notificacaoCriteria.copy();

        assertThat(notificacaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificacaoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificacaoCriteria = new NotificacaoCriteria();

        assertThat(notificacaoCriteria).hasToString("NotificacaoCriteria{}");
    }

    private static void setAllFilters(NotificacaoCriteria notificacaoCriteria) {
        notificacaoCriteria.id();
        notificacaoCriteria.titulo();
        notificacaoCriteria.mensagem();
        notificacaoCriteria.tipo();
        notificacaoCriteria.situacao();
        notificacaoCriteria.canal();
        notificacaoCriteria.lida();
        notificacaoCriteria.usuarioId();
        notificacaoCriteria.distinct();
    }

    private static Condition<NotificacaoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitulo()) &&
                condition.apply(criteria.getMensagem()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getSituacao()) &&
                condition.apply(criteria.getCanal()) &&
                condition.apply(criteria.getLida()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificacaoCriteria> copyFiltersAre(NotificacaoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitulo(), copy.getTitulo()) &&
                condition.apply(criteria.getMensagem(), copy.getMensagem()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getSituacao(), copy.getSituacao()) &&
                condition.apply(criteria.getCanal(), copy.getCanal()) &&
                condition.apply(criteria.getLida(), copy.getLida()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
