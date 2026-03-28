package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CheckoutCriteriaTest {

    @Test
    void newCheckoutCriteriaHasAllFiltersNullTest() {
        var checkoutCriteria = new CheckoutCriteria();
        assertThat(checkoutCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void checkoutCriteriaFluentMethodsCreatesFiltersTest() {
        var checkoutCriteria = new CheckoutCriteria();

        setAllFilters(checkoutCriteria);

        assertThat(checkoutCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void checkoutCriteriaCopyCreatesNullFilterTest() {
        var checkoutCriteria = new CheckoutCriteria();
        var copy = checkoutCriteria.copy();

        assertThat(checkoutCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(checkoutCriteria)
        );
    }

    @Test
    void checkoutCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var checkoutCriteria = new CheckoutCriteria();
        setAllFilters(checkoutCriteria);

        var copy = checkoutCriteria.copy();

        assertThat(checkoutCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(checkoutCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var checkoutCriteria = new CheckoutCriteria();

        assertThat(checkoutCriteria).hasToString("CheckoutCriteria{}");
    }

    private static void setAllFilters(CheckoutCriteria checkoutCriteria) {
        checkoutCriteria.id();
        checkoutCriteria.valor();
        checkoutCriteria.valorCreditado();
        checkoutCriteria.valorCreditadoCarteira();
        checkoutCriteria.idProdutoExterno();
        checkoutCriteria.nomeProdutoExterno();
        checkoutCriteria.status();
        checkoutCriteria.tipo();
        checkoutCriteria.descontoGeral();
        checkoutCriteria.tipoDesconto();
        checkoutCriteria.contabilizado();
        checkoutCriteria.nomeUsuarioFixo();
        checkoutCriteria.tokenCheckout();
        checkoutCriteria.idExterno();
        checkoutCriteria.webhookNotificado();
        checkoutCriteria.transacaoId();
        checkoutCriteria.usuarioId();
        checkoutCriteria.carteiraId();
        checkoutCriteria.carteiraCreditadaId();
        checkoutCriteria.moedaCarteiraId();
        checkoutCriteria.linkPagamentoId();
        checkoutCriteria.distinct();
    }

    private static Condition<CheckoutCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValor()) &&
                condition.apply(criteria.getValorCreditado()) &&
                condition.apply(criteria.getValorCreditadoCarteira()) &&
                condition.apply(criteria.getIdProdutoExterno()) &&
                condition.apply(criteria.getNomeProdutoExterno()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getDescontoGeral()) &&
                condition.apply(criteria.getTipoDesconto()) &&
                condition.apply(criteria.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getTokenCheckout()) &&
                condition.apply(criteria.getIdExterno()) &&
                condition.apply(criteria.getWebhookNotificado()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getCarteiraId()) &&
                condition.apply(criteria.getCarteiraCreditadaId()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getLinkPagamentoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CheckoutCriteria> copyFiltersAre(CheckoutCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValor(), copy.getValor()) &&
                condition.apply(criteria.getValorCreditado(), copy.getValorCreditado()) &&
                condition.apply(criteria.getValorCreditadoCarteira(), copy.getValorCreditadoCarteira()) &&
                condition.apply(criteria.getIdProdutoExterno(), copy.getIdProdutoExterno()) &&
                condition.apply(criteria.getNomeProdutoExterno(), copy.getNomeProdutoExterno()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getDescontoGeral(), copy.getDescontoGeral()) &&
                condition.apply(criteria.getTipoDesconto(), copy.getTipoDesconto()) &&
                condition.apply(criteria.getContabilizado(), copy.getContabilizado()) &&
                condition.apply(criteria.getNomeUsuarioFixo(), copy.getNomeUsuarioFixo()) &&
                condition.apply(criteria.getTokenCheckout(), copy.getTokenCheckout()) &&
                condition.apply(criteria.getIdExterno(), copy.getIdExterno()) &&
                condition.apply(criteria.getWebhookNotificado(), copy.getWebhookNotificado()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getCarteiraId(), copy.getCarteiraId()) &&
                condition.apply(criteria.getCarteiraCreditadaId(), copy.getCarteiraCreditadaId()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getLinkPagamentoId(), copy.getLinkPagamentoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
