package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContaBancariaCriteriaTest {

    @Test
    void newContaBancariaCriteriaHasAllFiltersNullTest() {
        var contaBancariaCriteria = new ContaBancariaCriteria();
        assertThat(contaBancariaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contaBancariaCriteriaFluentMethodsCreatesFiltersTest() {
        var contaBancariaCriteria = new ContaBancariaCriteria();

        setAllFilters(contaBancariaCriteria);

        assertThat(contaBancariaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contaBancariaCriteriaCopyCreatesNullFilterTest() {
        var contaBancariaCriteria = new ContaBancariaCriteria();
        var copy = contaBancariaCriteria.copy();

        assertThat(contaBancariaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contaBancariaCriteria)
        );
    }

    @Test
    void contaBancariaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contaBancariaCriteria = new ContaBancariaCriteria();
        setAllFilters(contaBancariaCriteria);

        var copy = contaBancariaCriteria.copy();

        assertThat(contaBancariaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contaBancariaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contaBancariaCriteria = new ContaBancariaCriteria();

        assertThat(contaBancariaCriteria).hasToString("ContaBancariaCriteria{}");
    }

    private static void setAllFilters(ContaBancariaCriteria contaBancariaCriteria) {
        contaBancariaCriteria.id();
        contaBancariaCriteria.nomeTitular();
        contaBancariaCriteria.numeroConta();
        contaBancariaCriteria.agencia();
        contaBancariaCriteria.nomeBanco();
        contaBancariaCriteria.codigoBanco();
        contaBancariaCriteria.ispb();
        contaBancariaCriteria.codigoSwift();
        contaBancariaCriteria.tipoConta();
        contaBancariaCriteria.ativa();
        contaBancariaCriteria.usuarioId();
        contaBancariaCriteria.paisId();
        contaBancariaCriteria.moedaId();
        contaBancariaCriteria.distinct();
    }

    private static Condition<ContaBancariaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNomeTitular()) &&
                condition.apply(criteria.getNumeroConta()) &&
                condition.apply(criteria.getAgencia()) &&
                condition.apply(criteria.getNomeBanco()) &&
                condition.apply(criteria.getCodigoBanco()) &&
                condition.apply(criteria.getIspb()) &&
                condition.apply(criteria.getCodigoSwift()) &&
                condition.apply(criteria.getTipoConta()) &&
                condition.apply(criteria.getAtiva()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getPaisId()) &&
                condition.apply(criteria.getMoedaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContaBancariaCriteria> copyFiltersAre(
        ContaBancariaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNomeTitular(), copy.getNomeTitular()) &&
                condition.apply(criteria.getNumeroConta(), copy.getNumeroConta()) &&
                condition.apply(criteria.getAgencia(), copy.getAgencia()) &&
                condition.apply(criteria.getNomeBanco(), copy.getNomeBanco()) &&
                condition.apply(criteria.getCodigoBanco(), copy.getCodigoBanco()) &&
                condition.apply(criteria.getIspb(), copy.getIspb()) &&
                condition.apply(criteria.getCodigoSwift(), copy.getCodigoSwift()) &&
                condition.apply(criteria.getTipoConta(), copy.getTipoConta()) &&
                condition.apply(criteria.getAtiva(), copy.getAtiva()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getPaisId(), copy.getPaisId()) &&
                condition.apply(criteria.getMoedaId(), copy.getMoedaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
