package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransferenciaCriteriaTest {

    @Test
    void newTransferenciaCriteriaHasAllFiltersNullTest() {
        var transferenciaCriteria = new TransferenciaCriteria();
        assertThat(transferenciaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transferenciaCriteriaFluentMethodsCreatesFiltersTest() {
        var transferenciaCriteria = new TransferenciaCriteria();

        setAllFilters(transferenciaCriteria);

        assertThat(transferenciaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transferenciaCriteriaCopyCreatesNullFilterTest() {
        var transferenciaCriteria = new TransferenciaCriteria();
        var copy = transferenciaCriteria.copy();

        assertThat(transferenciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transferenciaCriteria)
        );
    }

    @Test
    void transferenciaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transferenciaCriteria = new TransferenciaCriteria();
        setAllFilters(transferenciaCriteria);

        var copy = transferenciaCriteria.copy();

        assertThat(transferenciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transferenciaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transferenciaCriteria = new TransferenciaCriteria();

        assertThat(transferenciaCriteria).hasToString("TransferenciaCriteria{}");
    }

    private static void setAllFilters(TransferenciaCriteria transferenciaCriteria) {
        transferenciaCriteria.id();
        transferenciaCriteria.valor();
        transferenciaCriteria.chaveInterna();
        transferenciaCriteria.tipoChave();
        transferenciaCriteria.situacao();
        transferenciaCriteria.descricao();
        transferenciaCriteria.motivoRejeicao();
        transferenciaCriteria.numeroReferencia();
        transferenciaCriteria.transacaoId();
        transferenciaCriteria.usuarioOrigemId();
        transferenciaCriteria.usuarioDestinoId();
        transferenciaCriteria.carteiraOrigemId();
        transferenciaCriteria.carteiraDestinoId();
        transferenciaCriteria.moedaCarteiraId();
        transferenciaCriteria.distinct();
    }

    private static Condition<TransferenciaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValor()) &&
                condition.apply(criteria.getChaveInterna()) &&
                condition.apply(criteria.getTipoChave()) &&
                condition.apply(criteria.getSituacao()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getMotivoRejeicao()) &&
                condition.apply(criteria.getNumeroReferencia()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioOrigemId()) &&
                condition.apply(criteria.getUsuarioDestinoId()) &&
                condition.apply(criteria.getCarteiraOrigemId()) &&
                condition.apply(criteria.getCarteiraDestinoId()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransferenciaCriteria> copyFiltersAre(
        TransferenciaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValor(), copy.getValor()) &&
                condition.apply(criteria.getChaveInterna(), copy.getChaveInterna()) &&
                condition.apply(criteria.getTipoChave(), copy.getTipoChave()) &&
                condition.apply(criteria.getSituacao(), copy.getSituacao()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getMotivoRejeicao(), copy.getMotivoRejeicao()) &&
                condition.apply(criteria.getNumeroReferencia(), copy.getNumeroReferencia()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioOrigemId(), copy.getUsuarioOrigemId()) &&
                condition.apply(criteria.getUsuarioDestinoId(), copy.getUsuarioDestinoId()) &&
                condition.apply(criteria.getCarteiraOrigemId(), copy.getCarteiraOrigemId()) &&
                condition.apply(criteria.getCarteiraDestinoId(), copy.getCarteiraDestinoId()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
