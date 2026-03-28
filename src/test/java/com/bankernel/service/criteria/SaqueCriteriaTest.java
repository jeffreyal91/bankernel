package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SaqueCriteriaTest {

    @Test
    void newSaqueCriteriaHasAllFiltersNullTest() {
        var saqueCriteria = new SaqueCriteria();
        assertThat(saqueCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void saqueCriteriaFluentMethodsCreatesFiltersTest() {
        var saqueCriteria = new SaqueCriteria();

        setAllFilters(saqueCriteria);

        assertThat(saqueCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void saqueCriteriaCopyCreatesNullFilterTest() {
        var saqueCriteria = new SaqueCriteria();
        var copy = saqueCriteria.copy();

        assertThat(saqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(saqueCriteria)
        );
    }

    @Test
    void saqueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var saqueCriteria = new SaqueCriteria();
        setAllFilters(saqueCriteria);

        var copy = saqueCriteria.copy();

        assertThat(saqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(saqueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var saqueCriteria = new SaqueCriteria();

        assertThat(saqueCriteria).hasToString("SaqueCriteria{}");
    }

    private static void setAllFilters(SaqueCriteria saqueCriteria) {
        saqueCriteria.id();
        saqueCriteria.valorSaque();
        saqueCriteria.valorEnviado();
        saqueCriteria.descricao();
        saqueCriteria.tipoSaque();
        saqueCriteria.situacaoSaque();
        saqueCriteria.numeroReferencia();
        saqueCriteria.motivoRejeicao();
        saqueCriteria.contabilizado();
        saqueCriteria.nomeUsuarioFixo();
        saqueCriteria.transacaoId();
        saqueCriteria.transacaoEstornoId();
        saqueCriteria.carteiraId();
        saqueCriteria.moedaCarteiraId();
        saqueCriteria.contaBancariaDestinoId();
        saqueCriteria.usuarioId();
        saqueCriteria.escritorioId();
        saqueCriteria.saquePixId();
        saqueCriteria.saqueBoletoId();
        saqueCriteria.distinct();
    }

    private static Condition<SaqueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValorSaque()) &&
                condition.apply(criteria.getValorEnviado()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getTipoSaque()) &&
                condition.apply(criteria.getSituacaoSaque()) &&
                condition.apply(criteria.getNumeroReferencia()) &&
                condition.apply(criteria.getMotivoRejeicao()) &&
                condition.apply(criteria.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getTransacaoEstornoId()) &&
                condition.apply(criteria.getCarteiraId()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getContaBancariaDestinoId()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getEscritorioId()) &&
                condition.apply(criteria.getSaquePixId()) &&
                condition.apply(criteria.getSaqueBoletoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SaqueCriteria> copyFiltersAre(SaqueCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValorSaque(), copy.getValorSaque()) &&
                condition.apply(criteria.getValorEnviado(), copy.getValorEnviado()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getTipoSaque(), copy.getTipoSaque()) &&
                condition.apply(criteria.getSituacaoSaque(), copy.getSituacaoSaque()) &&
                condition.apply(criteria.getNumeroReferencia(), copy.getNumeroReferencia()) &&
                condition.apply(criteria.getMotivoRejeicao(), copy.getMotivoRejeicao()) &&
                condition.apply(criteria.getContabilizado(), copy.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo(), copy.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getTransacaoEstornoId(), copy.getTransacaoEstornoId()) &&
                condition.apply(criteria.getCarteiraId(), copy.getCarteiraId()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getContaBancariaDestinoId(), copy.getContaBancariaDestinoId()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getEscritorioId(), copy.getEscritorioId()) &&
                condition.apply(criteria.getSaquePixId(), copy.getSaquePixId()) &&
                condition.apply(criteria.getSaqueBoletoId(), copy.getSaqueBoletoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
