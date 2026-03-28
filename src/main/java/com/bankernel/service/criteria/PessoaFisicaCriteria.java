package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumGenero;
import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.PessoaFisica} entity. This class is used
 * in {@link com.bankernel.web.rest.PessoaFisicaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pessoa-fisicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PessoaFisicaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumGenero
     */
    public static class EnumGeneroFilter extends Filter<EnumGenero> {

        public EnumGeneroFilter() {}

        public EnumGeneroFilter(EnumGeneroFilter filter) {
            super(filter);
        }

        @Override
        public EnumGeneroFilter copy() {
            return new EnumGeneroFilter(this);
        }
    }

    /**
     * Class for filtering EnumNivelRisco
     */
    public static class EnumNivelRiscoFilter extends Filter<EnumNivelRisco> {

        public EnumNivelRiscoFilter() {}

        public EnumNivelRiscoFilter(EnumNivelRiscoFilter filter) {
            super(filter);
        }

        @Override
        public EnumNivelRiscoFilter copy() {
            return new EnumNivelRiscoFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusPessoa
     */
    public static class EnumStatusPessoaFilter extends Filter<EnumStatusPessoa> {

        public EnumStatusPessoaFilter() {}

        public EnumStatusPessoaFilter(EnumStatusPessoaFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusPessoaFilter copy() {
            return new EnumStatusPessoaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cpf;

    private StringFilter nomeCompleto;

    private StringFilter nomeSocial;

    private LocalDateFilter dataNascimento;

    private EnumGeneroFilter genero;

    private StringFilter nomeMae;

    private StringFilter telefone;

    private BooleanFilter telefoneVerificado;

    private EnumNivelRiscoFilter nivelRisco;

    private EnumStatusPessoaFilter situacao;

    private BooleanFilter bloqueioSaque;

    private LongFilter usuarioId;

    private LongFilter moedaPrincipalId;

    private LongFilter nacionalidadeId;

    private LongFilter profissaoId;

    private LongFilter planoId;

    private LongFilter escritorioId;

    private Boolean distinct;

    public PessoaFisicaCriteria() {}

    public PessoaFisicaCriteria(PessoaFisicaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cpf = other.optionalCpf().map(StringFilter::copy).orElse(null);
        this.nomeCompleto = other.optionalNomeCompleto().map(StringFilter::copy).orElse(null);
        this.nomeSocial = other.optionalNomeSocial().map(StringFilter::copy).orElse(null);
        this.dataNascimento = other.optionalDataNascimento().map(LocalDateFilter::copy).orElse(null);
        this.genero = other.optionalGenero().map(EnumGeneroFilter::copy).orElse(null);
        this.nomeMae = other.optionalNomeMae().map(StringFilter::copy).orElse(null);
        this.telefone = other.optionalTelefone().map(StringFilter::copy).orElse(null);
        this.telefoneVerificado = other.optionalTelefoneVerificado().map(BooleanFilter::copy).orElse(null);
        this.nivelRisco = other.optionalNivelRisco().map(EnumNivelRiscoFilter::copy).orElse(null);
        this.situacao = other.optionalSituacao().map(EnumStatusPessoaFilter::copy).orElse(null);
        this.bloqueioSaque = other.optionalBloqueioSaque().map(BooleanFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.moedaPrincipalId = other.optionalMoedaPrincipalId().map(LongFilter::copy).orElse(null);
        this.nacionalidadeId = other.optionalNacionalidadeId().map(LongFilter::copy).orElse(null);
        this.profissaoId = other.optionalProfissaoId().map(LongFilter::copy).orElse(null);
        this.planoId = other.optionalPlanoId().map(LongFilter::copy).orElse(null);
        this.escritorioId = other.optionalEscritorioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PessoaFisicaCriteria copy() {
        return new PessoaFisicaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public Optional<StringFilter> optionalCpf() {
        return Optional.ofNullable(cpf);
    }

    public StringFilter cpf() {
        if (cpf == null) {
            setCpf(new StringFilter());
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getNomeCompleto() {
        return nomeCompleto;
    }

    public Optional<StringFilter> optionalNomeCompleto() {
        return Optional.ofNullable(nomeCompleto);
    }

    public StringFilter nomeCompleto() {
        if (nomeCompleto == null) {
            setNomeCompleto(new StringFilter());
        }
        return nomeCompleto;
    }

    public void setNomeCompleto(StringFilter nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public StringFilter getNomeSocial() {
        return nomeSocial;
    }

    public Optional<StringFilter> optionalNomeSocial() {
        return Optional.ofNullable(nomeSocial);
    }

    public StringFilter nomeSocial() {
        if (nomeSocial == null) {
            setNomeSocial(new StringFilter());
        }
        return nomeSocial;
    }

    public void setNomeSocial(StringFilter nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public Optional<LocalDateFilter> optionalDataNascimento() {
        return Optional.ofNullable(dataNascimento);
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            setDataNascimento(new LocalDateFilter());
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EnumGeneroFilter getGenero() {
        return genero;
    }

    public Optional<EnumGeneroFilter> optionalGenero() {
        return Optional.ofNullable(genero);
    }

    public EnumGeneroFilter genero() {
        if (genero == null) {
            setGenero(new EnumGeneroFilter());
        }
        return genero;
    }

    public void setGenero(EnumGeneroFilter genero) {
        this.genero = genero;
    }

    public StringFilter getNomeMae() {
        return nomeMae;
    }

    public Optional<StringFilter> optionalNomeMae() {
        return Optional.ofNullable(nomeMae);
    }

    public StringFilter nomeMae() {
        if (nomeMae == null) {
            setNomeMae(new StringFilter());
        }
        return nomeMae;
    }

    public void setNomeMae(StringFilter nomeMae) {
        this.nomeMae = nomeMae;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public Optional<StringFilter> optionalTelefone() {
        return Optional.ofNullable(telefone);
    }

    public StringFilter telefone() {
        if (telefone == null) {
            setTelefone(new StringFilter());
        }
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public BooleanFilter getTelefoneVerificado() {
        return telefoneVerificado;
    }

    public Optional<BooleanFilter> optionalTelefoneVerificado() {
        return Optional.ofNullable(telefoneVerificado);
    }

    public BooleanFilter telefoneVerificado() {
        if (telefoneVerificado == null) {
            setTelefoneVerificado(new BooleanFilter());
        }
        return telefoneVerificado;
    }

    public void setTelefoneVerificado(BooleanFilter telefoneVerificado) {
        this.telefoneVerificado = telefoneVerificado;
    }

    public EnumNivelRiscoFilter getNivelRisco() {
        return nivelRisco;
    }

    public Optional<EnumNivelRiscoFilter> optionalNivelRisco() {
        return Optional.ofNullable(nivelRisco);
    }

    public EnumNivelRiscoFilter nivelRisco() {
        if (nivelRisco == null) {
            setNivelRisco(new EnumNivelRiscoFilter());
        }
        return nivelRisco;
    }

    public void setNivelRisco(EnumNivelRiscoFilter nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public EnumStatusPessoaFilter getSituacao() {
        return situacao;
    }

    public Optional<EnumStatusPessoaFilter> optionalSituacao() {
        return Optional.ofNullable(situacao);
    }

    public EnumStatusPessoaFilter situacao() {
        if (situacao == null) {
            setSituacao(new EnumStatusPessoaFilter());
        }
        return situacao;
    }

    public void setSituacao(EnumStatusPessoaFilter situacao) {
        this.situacao = situacao;
    }

    public BooleanFilter getBloqueioSaque() {
        return bloqueioSaque;
    }

    public Optional<BooleanFilter> optionalBloqueioSaque() {
        return Optional.ofNullable(bloqueioSaque);
    }

    public BooleanFilter bloqueioSaque() {
        if (bloqueioSaque == null) {
            setBloqueioSaque(new BooleanFilter());
        }
        return bloqueioSaque;
    }

    public void setBloqueioSaque(BooleanFilter bloqueioSaque) {
        this.bloqueioSaque = bloqueioSaque;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public Optional<LongFilter> optionalUsuarioId() {
        return Optional.ofNullable(usuarioId);
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            setUsuarioId(new LongFilter());
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getMoedaPrincipalId() {
        return moedaPrincipalId;
    }

    public Optional<LongFilter> optionalMoedaPrincipalId() {
        return Optional.ofNullable(moedaPrincipalId);
    }

    public LongFilter moedaPrincipalId() {
        if (moedaPrincipalId == null) {
            setMoedaPrincipalId(new LongFilter());
        }
        return moedaPrincipalId;
    }

    public void setMoedaPrincipalId(LongFilter moedaPrincipalId) {
        this.moedaPrincipalId = moedaPrincipalId;
    }

    public LongFilter getNacionalidadeId() {
        return nacionalidadeId;
    }

    public Optional<LongFilter> optionalNacionalidadeId() {
        return Optional.ofNullable(nacionalidadeId);
    }

    public LongFilter nacionalidadeId() {
        if (nacionalidadeId == null) {
            setNacionalidadeId(new LongFilter());
        }
        return nacionalidadeId;
    }

    public void setNacionalidadeId(LongFilter nacionalidadeId) {
        this.nacionalidadeId = nacionalidadeId;
    }

    public LongFilter getProfissaoId() {
        return profissaoId;
    }

    public Optional<LongFilter> optionalProfissaoId() {
        return Optional.ofNullable(profissaoId);
    }

    public LongFilter profissaoId() {
        if (profissaoId == null) {
            setProfissaoId(new LongFilter());
        }
        return profissaoId;
    }

    public void setProfissaoId(LongFilter profissaoId) {
        this.profissaoId = profissaoId;
    }

    public LongFilter getPlanoId() {
        return planoId;
    }

    public Optional<LongFilter> optionalPlanoId() {
        return Optional.ofNullable(planoId);
    }

    public LongFilter planoId() {
        if (planoId == null) {
            setPlanoId(new LongFilter());
        }
        return planoId;
    }

    public void setPlanoId(LongFilter planoId) {
        this.planoId = planoId;
    }

    public LongFilter getEscritorioId() {
        return escritorioId;
    }

    public Optional<LongFilter> optionalEscritorioId() {
        return Optional.ofNullable(escritorioId);
    }

    public LongFilter escritorioId() {
        if (escritorioId == null) {
            setEscritorioId(new LongFilter());
        }
        return escritorioId;
    }

    public void setEscritorioId(LongFilter escritorioId) {
        this.escritorioId = escritorioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PessoaFisicaCriteria that = (PessoaFisicaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(nomeCompleto, that.nomeCompleto) &&
            Objects.equals(nomeSocial, that.nomeSocial) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(genero, that.genero) &&
            Objects.equals(nomeMae, that.nomeMae) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(telefoneVerificado, that.telefoneVerificado) &&
            Objects.equals(nivelRisco, that.nivelRisco) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(bloqueioSaque, that.bloqueioSaque) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(moedaPrincipalId, that.moedaPrincipalId) &&
            Objects.equals(nacionalidadeId, that.nacionalidadeId) &&
            Objects.equals(profissaoId, that.profissaoId) &&
            Objects.equals(planoId, that.planoId) &&
            Objects.equals(escritorioId, that.escritorioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cpf,
            nomeCompleto,
            nomeSocial,
            dataNascimento,
            genero,
            nomeMae,
            telefone,
            telefoneVerificado,
            nivelRisco,
            situacao,
            bloqueioSaque,
            usuarioId,
            moedaPrincipalId,
            nacionalidadeId,
            profissaoId,
            planoId,
            escritorioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaFisicaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCpf().map(f -> "cpf=" + f + ", ").orElse("") +
            optionalNomeCompleto().map(f -> "nomeCompleto=" + f + ", ").orElse("") +
            optionalNomeSocial().map(f -> "nomeSocial=" + f + ", ").orElse("") +
            optionalDataNascimento().map(f -> "dataNascimento=" + f + ", ").orElse("") +
            optionalGenero().map(f -> "genero=" + f + ", ").orElse("") +
            optionalNomeMae().map(f -> "nomeMae=" + f + ", ").orElse("") +
            optionalTelefone().map(f -> "telefone=" + f + ", ").orElse("") +
            optionalTelefoneVerificado().map(f -> "telefoneVerificado=" + f + ", ").orElse("") +
            optionalNivelRisco().map(f -> "nivelRisco=" + f + ", ").orElse("") +
            optionalSituacao().map(f -> "situacao=" + f + ", ").orElse("") +
            optionalBloqueioSaque().map(f -> "bloqueioSaque=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalMoedaPrincipalId().map(f -> "moedaPrincipalId=" + f + ", ").orElse("") +
            optionalNacionalidadeId().map(f -> "nacionalidadeId=" + f + ", ").orElse("") +
            optionalProfissaoId().map(f -> "profissaoId=" + f + ", ").orElse("") +
            optionalPlanoId().map(f -> "planoId=" + f + ", ").orElse("") +
            optionalEscritorioId().map(f -> "escritorioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
