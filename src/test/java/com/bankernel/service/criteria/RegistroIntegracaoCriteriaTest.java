package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RegistroIntegracaoCriteriaTest {

    @Test
    void newRegistroIntegracaoCriteriaHasAllFiltersNullTest() {
        var registroIntegracaoCriteria = new RegistroIntegracaoCriteria();
        assertThat(registroIntegracaoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void registroIntegracaoCriteriaFluentMethodsCreatesFiltersTest() {
        var registroIntegracaoCriteria = new RegistroIntegracaoCriteria();

        setAllFilters(registroIntegracaoCriteria);

        assertThat(registroIntegracaoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void registroIntegracaoCriteriaCopyCreatesNullFilterTest() {
        var registroIntegracaoCriteria = new RegistroIntegracaoCriteria();
        var copy = registroIntegracaoCriteria.copy();

        assertThat(registroIntegracaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(registroIntegracaoCriteria)
        );
    }

    @Test
    void registroIntegracaoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var registroIntegracaoCriteria = new RegistroIntegracaoCriteria();
        setAllFilters(registroIntegracaoCriteria);

        var copy = registroIntegracaoCriteria.copy();

        assertThat(registroIntegracaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(registroIntegracaoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var registroIntegracaoCriteria = new RegistroIntegracaoCriteria();

        assertThat(registroIntegracaoCriteria).hasToString("RegistroIntegracaoCriteria{}");
    }

    private static void setAllFilters(RegistroIntegracaoCriteria registroIntegracaoCriteria) {
        registroIntegracaoCriteria.id();
        registroIntegracaoCriteria.fornecedor();
        registroIntegracaoCriteria.tipoIntegracao();
        registroIntegracaoCriteria.operacao();
        registroIntegracaoCriteria.codigoHttp();
        registroIntegracaoCriteria.sucesso();
        registroIntegracaoCriteria.mensagemErro();
        registroIntegracaoCriteria.duracaoMilissegundos();
        registroIntegracaoCriteria.tipoEntidadeOrigem();
        registroIntegracaoCriteria.idEntidadeOrigem();
        registroIntegracaoCriteria.distinct();
    }

    private static Condition<RegistroIntegracaoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFornecedor()) &&
                condition.apply(criteria.getTipoIntegracao()) &&
                condition.apply(criteria.getOperacao()) &&
                condition.apply(criteria.getCodigoHttp()) &&
                condition.apply(criteria.getSucesso()) &&
                condition.apply(criteria.getMensagemErro()) &&
                condition.apply(criteria.getDuracaoMilissegundos()) &&
                condition.apply(criteria.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RegistroIntegracaoCriteria> copyFiltersAre(
        RegistroIntegracaoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFornecedor(), copy.getFornecedor()) &&
                condition.apply(criteria.getTipoIntegracao(), copy.getTipoIntegracao()) &&
                condition.apply(criteria.getOperacao(), copy.getOperacao()) &&
                condition.apply(criteria.getCodigoHttp(), copy.getCodigoHttp()) &&
                condition.apply(criteria.getSucesso(), copy.getSucesso()) &&
                condition.apply(criteria.getMensagemErro(), copy.getMensagemErro()) &&
                condition.apply(criteria.getDuracaoMilissegundos(), copy.getDuracaoMilissegundos()) &&
                condition.apply(criteria.getTipoEntidadeOrigem(), copy.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem(), copy.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
