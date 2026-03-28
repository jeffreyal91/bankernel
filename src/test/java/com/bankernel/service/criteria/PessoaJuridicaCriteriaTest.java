package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PessoaJuridicaCriteriaTest {

    @Test
    void newPessoaJuridicaCriteriaHasAllFiltersNullTest() {
        var pessoaJuridicaCriteria = new PessoaJuridicaCriteria();
        assertThat(pessoaJuridicaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pessoaJuridicaCriteriaFluentMethodsCreatesFiltersTest() {
        var pessoaJuridicaCriteria = new PessoaJuridicaCriteria();

        setAllFilters(pessoaJuridicaCriteria);

        assertThat(pessoaJuridicaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pessoaJuridicaCriteriaCopyCreatesNullFilterTest() {
        var pessoaJuridicaCriteria = new PessoaJuridicaCriteria();
        var copy = pessoaJuridicaCriteria.copy();

        assertThat(pessoaJuridicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pessoaJuridicaCriteria)
        );
    }

    @Test
    void pessoaJuridicaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pessoaJuridicaCriteria = new PessoaJuridicaCriteria();
        setAllFilters(pessoaJuridicaCriteria);

        var copy = pessoaJuridicaCriteria.copy();

        assertThat(pessoaJuridicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pessoaJuridicaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pessoaJuridicaCriteria = new PessoaJuridicaCriteria();

        assertThat(pessoaJuridicaCriteria).hasToString("PessoaJuridicaCriteria{}");
    }

    private static void setAllFilters(PessoaJuridicaCriteria pessoaJuridicaCriteria) {
        pessoaJuridicaCriteria.id();
        pessoaJuridicaCriteria.cnpj();
        pessoaJuridicaCriteria.razaoSocial();
        pessoaJuridicaCriteria.nomeFantasia();
        pessoaJuridicaCriteria.telefone();
        pessoaJuridicaCriteria.sitioWeb();
        pessoaJuridicaCriteria.descricao();
        pessoaJuridicaCriteria.dataFundacao();
        pessoaJuridicaCriteria.capitalSocial();
        pessoaJuridicaCriteria.faturamentoAnual();
        pessoaJuridicaCriteria.mediaMovimentacaoMensal();
        pessoaJuridicaCriteria.tipoDocumento();
        pessoaJuridicaCriteria.regimeTributario();
        pessoaJuridicaCriteria.codigoNaturezaJuridica();
        pessoaJuridicaCriteria.atividadePrincipal();
        pessoaJuridicaCriteria.empresaAtiva();
        pessoaJuridicaCriteria.nivelRisco();
        pessoaJuridicaCriteria.situacao();
        pessoaJuridicaCriteria.bloqueioSaque();
        pessoaJuridicaCriteria.cpfRepresentanteLegal();
        pessoaJuridicaCriteria.numeroRegistro();
        pessoaJuridicaCriteria.usuarioId();
        pessoaJuridicaCriteria.moedaPrincipalId();
        pessoaJuridicaCriteria.contratoSocialId();
        pessoaJuridicaCriteria.nacionalidadeId();
        pessoaJuridicaCriteria.tipoNegocioId();
        pessoaJuridicaCriteria.planoId();
        pessoaJuridicaCriteria.escritorioId();
        pessoaJuridicaCriteria.distinct();
    }

    private static Condition<PessoaJuridicaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCnpj()) &&
                condition.apply(criteria.getRazaoSocial()) &&
                condition.apply(criteria.getNomeFantasia()) &&
                condition.apply(criteria.getTelefone()) &&
                condition.apply(criteria.getSitioWeb()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getDataFundacao()) &&
                condition.apply(criteria.getCapitalSocial()) &&
                condition.apply(criteria.getFaturamentoAnual()) &&
                condition.apply(criteria.getMediaMovimentacaoMensal()) &&
                condition.apply(criteria.getTipoDocumento()) &&
                condition.apply(criteria.getRegimeTributario()) &&
                condition.apply(criteria.getCodigoNaturezaJuridica()) &&
                condition.apply(criteria.getAtividadePrincipal()) &&
                condition.apply(criteria.getEmpresaAtiva()) &&
                condition.apply(criteria.getNivelRisco()) &&
                condition.apply(criteria.getSituacao()) &&
                condition.apply(criteria.getBloqueioSaque()) &&
                condition.apply(criteria.getCpfRepresentanteLegal()) &&
                condition.apply(criteria.getNumeroRegistro()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getMoedaPrincipalId()) &&
                condition.apply(criteria.getContratoSocialId()) &&
                condition.apply(criteria.getNacionalidadeId()) &&
                condition.apply(criteria.getTipoNegocioId()) &&
                condition.apply(criteria.getPlanoId()) &&
                condition.apply(criteria.getEscritorioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PessoaJuridicaCriteria> copyFiltersAre(
        PessoaJuridicaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCnpj(), copy.getCnpj()) &&
                condition.apply(criteria.getRazaoSocial(), copy.getRazaoSocial()) &&
                condition.apply(criteria.getNomeFantasia(), copy.getNomeFantasia()) &&
                condition.apply(criteria.getTelefone(), copy.getTelefone()) &&
                condition.apply(criteria.getSitioWeb(), copy.getSitioWeb()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getDataFundacao(), copy.getDataFundacao()) &&
                condition.apply(criteria.getCapitalSocial(), copy.getCapitalSocial()) &&
                condition.apply(criteria.getFaturamentoAnual(), copy.getFaturamentoAnual()) &&
                condition.apply(criteria.getMediaMovimentacaoMensal(), copy.getMediaMovimentacaoMensal()) &&
                condition.apply(criteria.getTipoDocumento(), copy.getTipoDocumento()) &&
                condition.apply(criteria.getRegimeTributario(), copy.getRegimeTributario()) &&
                condition.apply(criteria.getCodigoNaturezaJuridica(), copy.getCodigoNaturezaJuridica()) &&
                condition.apply(criteria.getAtividadePrincipal(), copy.getAtividadePrincipal()) &&
                condition.apply(criteria.getEmpresaAtiva(), copy.getEmpresaAtiva()) &&
                condition.apply(criteria.getNivelRisco(), copy.getNivelRisco()) &&
                condition.apply(criteria.getSituacao(), copy.getSituacao()) &&
                condition.apply(criteria.getBloqueioSaque(), copy.getBloqueioSaque()) &&
                condition.apply(criteria.getCpfRepresentanteLegal(), copy.getCpfRepresentanteLegal()) &&
                condition.apply(criteria.getNumeroRegistro(), copy.getNumeroRegistro()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getMoedaPrincipalId(), copy.getMoedaPrincipalId()) &&
                condition.apply(criteria.getContratoSocialId(), copy.getContratoSocialId()) &&
                condition.apply(criteria.getNacionalidadeId(), copy.getNacionalidadeId()) &&
                condition.apply(criteria.getTipoNegocioId(), copy.getTipoNegocioId()) &&
                condition.apply(criteria.getPlanoId(), copy.getPlanoId()) &&
                condition.apply(criteria.getEscritorioId(), copy.getEscritorioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
