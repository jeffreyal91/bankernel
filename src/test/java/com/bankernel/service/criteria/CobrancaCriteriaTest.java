package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CobrancaCriteriaTest {

    @Test
    void newCobrancaCriteriaHasAllFiltersNullTest() {
        var cobrancaCriteria = new CobrancaCriteria();
        assertThat(cobrancaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cobrancaCriteriaFluentMethodsCreatesFiltersTest() {
        var cobrancaCriteria = new CobrancaCriteria();

        setAllFilters(cobrancaCriteria);

        assertThat(cobrancaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cobrancaCriteriaCopyCreatesNullFilterTest() {
        var cobrancaCriteria = new CobrancaCriteria();
        var copy = cobrancaCriteria.copy();

        assertThat(cobrancaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cobrancaCriteria)
        );
    }

    @Test
    void cobrancaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cobrancaCriteria = new CobrancaCriteria();
        setAllFilters(cobrancaCriteria);

        var copy = cobrancaCriteria.copy();

        assertThat(cobrancaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cobrancaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cobrancaCriteria = new CobrancaCriteria();

        assertThat(cobrancaCriteria).hasToString("CobrancaCriteria{}");
    }

    private static void setAllFilters(CobrancaCriteria cobrancaCriteria) {
        cobrancaCriteria.id();
        cobrancaCriteria.valor();
        cobrancaCriteria.valorCreditado();
        cobrancaCriteria.valorCreditadoCarteira();
        cobrancaCriteria.idProdutoExterno();
        cobrancaCriteria.nomeProdutoExterno();
        cobrancaCriteria.situacao();
        cobrancaCriteria.tipo();
        cobrancaCriteria.descontoGeral();
        cobrancaCriteria.tipoDesconto();
        cobrancaCriteria.contabilizado();
        cobrancaCriteria.nomeUsuarioFixo();
        cobrancaCriteria.chaveCobranca();
        cobrancaCriteria.identificadorExterno();
        cobrancaCriteria.retornoNotificado();
        cobrancaCriteria.transacaoId();
        cobrancaCriteria.usuarioId();
        cobrancaCriteria.carteiraId();
        cobrancaCriteria.carteiraCreditadaId();
        cobrancaCriteria.moedaCarteiraId();
        cobrancaCriteria.linkCobrancaId();
        cobrancaCriteria.distinct();
    }

    private static Condition<CobrancaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValor()) &&
                condition.apply(criteria.getValorCreditado()) &&
                condition.apply(criteria.getValorCreditadoCarteira()) &&
                condition.apply(criteria.getIdProdutoExterno()) &&
                condition.apply(criteria.getNomeProdutoExterno()) &&
                condition.apply(criteria.getSituacao()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getDescontoGeral()) &&
                condition.apply(criteria.getTipoDesconto()) &&
                condition.apply(criteria.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getChaveCobranca()) &&
                condition.apply(criteria.getIdentificadorExterno()) &&
                condition.apply(criteria.getRetornoNotificado()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getCarteiraId()) &&
                condition.apply(criteria.getCarteiraCreditadaId()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getLinkCobrancaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CobrancaCriteria> copyFiltersAre(CobrancaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValor(), copy.getValor()) &&
                condition.apply(criteria.getValorCreditado(), copy.getValorCreditado()) &&
                condition.apply(criteria.getValorCreditadoCarteira(), copy.getValorCreditadoCarteira()) &&
                condition.apply(criteria.getIdProdutoExterno(), copy.getIdProdutoExterno()) &&
                condition.apply(criteria.getNomeProdutoExterno(), copy.getNomeProdutoExterno()) &&
                condition.apply(criteria.getSituacao(), copy.getSituacao()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getDescontoGeral(), copy.getDescontoGeral()) &&
                condition.apply(criteria.getTipoDesconto(), copy.getTipoDesconto()) &&
                condition.apply(criteria.getContabilizado(), copy.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo(), copy.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getChaveCobranca(), copy.getChaveCobranca()) &&
                condition.apply(criteria.getIdentificadorExterno(), copy.getIdentificadorExterno()) &&
                condition.apply(criteria.getRetornoNotificado(), copy.getRetornoNotificado()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getCarteiraId(), copy.getCarteiraId()) &&
                condition.apply(criteria.getCarteiraCreditadaId(), copy.getCarteiraCreditadaId()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getLinkCobrancaId(), copy.getLinkCobrancaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
