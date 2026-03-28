package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import com.bankernel.domain.enumeration.EnumTipoDocumentoPJ;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.PessoaJuridica} entity. This class is used
 * in {@link com.bankernel.web.rest.PessoaJuridicaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pessoa-juridicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PessoaJuridicaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoDocumentoPJ
     */
    public static class EnumTipoDocumentoPJFilter extends Filter<EnumTipoDocumentoPJ> {

        public EnumTipoDocumentoPJFilter() {}

        public EnumTipoDocumentoPJFilter(EnumTipoDocumentoPJFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoDocumentoPJFilter copy() {
            return new EnumTipoDocumentoPJFilter(this);
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

    private StringFilter cnpj;

    private StringFilter razaoSocial;

    private StringFilter nomeFantasia;

    private StringFilter telefone;

    private StringFilter sitioWeb;

    private StringFilter descricao;

    private LocalDateFilter dataFundacao;

    private BigDecimalFilter capitalSocial;

    private BigDecimalFilter faturamentoAnual;

    private BigDecimalFilter mediaMovimentacaoMensal;

    private EnumTipoDocumentoPJFilter tipoDocumento;

    private StringFilter regimeTributario;

    private StringFilter codigoNaturezaJuridica;

    private StringFilter atividadePrincipal;

    private BooleanFilter empresaAtiva;

    private EnumNivelRiscoFilter nivelRisco;

    private EnumStatusPessoaFilter situacao;

    private BooleanFilter bloqueioSaque;

    private StringFilter cpfRepresentanteLegal;

    private StringFilter numeroRegistro;

    private LongFilter usuarioId;

    private LongFilter moedaPrincipalId;

    private LongFilter contratoSocialId;

    private LongFilter nacionalidadeId;

    private LongFilter tipoNegocioId;

    private LongFilter planoId;

    private LongFilter escritorioId;

    private Boolean distinct;

    public PessoaJuridicaCriteria() {}

    public PessoaJuridicaCriteria(PessoaJuridicaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cnpj = other.optionalCnpj().map(StringFilter::copy).orElse(null);
        this.razaoSocial = other.optionalRazaoSocial().map(StringFilter::copy).orElse(null);
        this.nomeFantasia = other.optionalNomeFantasia().map(StringFilter::copy).orElse(null);
        this.telefone = other.optionalTelefone().map(StringFilter::copy).orElse(null);
        this.sitioWeb = other.optionalSitioWeb().map(StringFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.dataFundacao = other.optionalDataFundacao().map(LocalDateFilter::copy).orElse(null);
        this.capitalSocial = other.optionalCapitalSocial().map(BigDecimalFilter::copy).orElse(null);
        this.faturamentoAnual = other.optionalFaturamentoAnual().map(BigDecimalFilter::copy).orElse(null);
        this.mediaMovimentacaoMensal = other.optionalMediaMovimentacaoMensal().map(BigDecimalFilter::copy).orElse(null);
        this.tipoDocumento = other.optionalTipoDocumento().map(EnumTipoDocumentoPJFilter::copy).orElse(null);
        this.regimeTributario = other.optionalRegimeTributario().map(StringFilter::copy).orElse(null);
        this.codigoNaturezaJuridica = other.optionalCodigoNaturezaJuridica().map(StringFilter::copy).orElse(null);
        this.atividadePrincipal = other.optionalAtividadePrincipal().map(StringFilter::copy).orElse(null);
        this.empresaAtiva = other.optionalEmpresaAtiva().map(BooleanFilter::copy).orElse(null);
        this.nivelRisco = other.optionalNivelRisco().map(EnumNivelRiscoFilter::copy).orElse(null);
        this.situacao = other.optionalSituacao().map(EnumStatusPessoaFilter::copy).orElse(null);
        this.bloqueioSaque = other.optionalBloqueioSaque().map(BooleanFilter::copy).orElse(null);
        this.cpfRepresentanteLegal = other.optionalCpfRepresentanteLegal().map(StringFilter::copy).orElse(null);
        this.numeroRegistro = other.optionalNumeroRegistro().map(StringFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.moedaPrincipalId = other.optionalMoedaPrincipalId().map(LongFilter::copy).orElse(null);
        this.contratoSocialId = other.optionalContratoSocialId().map(LongFilter::copy).orElse(null);
        this.nacionalidadeId = other.optionalNacionalidadeId().map(LongFilter::copy).orElse(null);
        this.tipoNegocioId = other.optionalTipoNegocioId().map(LongFilter::copy).orElse(null);
        this.planoId = other.optionalPlanoId().map(LongFilter::copy).orElse(null);
        this.escritorioId = other.optionalEscritorioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PessoaJuridicaCriteria copy() {
        return new PessoaJuridicaCriteria(this);
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

    public StringFilter getCnpj() {
        return cnpj;
    }

    public Optional<StringFilter> optionalCnpj() {
        return Optional.ofNullable(cnpj);
    }

    public StringFilter cnpj() {
        if (cnpj == null) {
            setCnpj(new StringFilter());
        }
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getRazaoSocial() {
        return razaoSocial;
    }

    public Optional<StringFilter> optionalRazaoSocial() {
        return Optional.ofNullable(razaoSocial);
    }

    public StringFilter razaoSocial() {
        if (razaoSocial == null) {
            setRazaoSocial(new StringFilter());
        }
        return razaoSocial;
    }

    public void setRazaoSocial(StringFilter razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public StringFilter getNomeFantasia() {
        return nomeFantasia;
    }

    public Optional<StringFilter> optionalNomeFantasia() {
        return Optional.ofNullable(nomeFantasia);
    }

    public StringFilter nomeFantasia() {
        if (nomeFantasia == null) {
            setNomeFantasia(new StringFilter());
        }
        return nomeFantasia;
    }

    public void setNomeFantasia(StringFilter nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
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

    public StringFilter getSitioWeb() {
        return sitioWeb;
    }

    public Optional<StringFilter> optionalSitioWeb() {
        return Optional.ofNullable(sitioWeb);
    }

    public StringFilter sitioWeb() {
        if (sitioWeb == null) {
            setSitioWeb(new StringFilter());
        }
        return sitioWeb;
    }

    public void setSitioWeb(StringFilter sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public Optional<StringFilter> optionalDescricao() {
        return Optional.ofNullable(descricao);
    }

    public StringFilter descricao() {
        if (descricao == null) {
            setDescricao(new StringFilter());
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public LocalDateFilter getDataFundacao() {
        return dataFundacao;
    }

    public Optional<LocalDateFilter> optionalDataFundacao() {
        return Optional.ofNullable(dataFundacao);
    }

    public LocalDateFilter dataFundacao() {
        if (dataFundacao == null) {
            setDataFundacao(new LocalDateFilter());
        }
        return dataFundacao;
    }

    public void setDataFundacao(LocalDateFilter dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public BigDecimalFilter getCapitalSocial() {
        return capitalSocial;
    }

    public Optional<BigDecimalFilter> optionalCapitalSocial() {
        return Optional.ofNullable(capitalSocial);
    }

    public BigDecimalFilter capitalSocial() {
        if (capitalSocial == null) {
            setCapitalSocial(new BigDecimalFilter());
        }
        return capitalSocial;
    }

    public void setCapitalSocial(BigDecimalFilter capitalSocial) {
        this.capitalSocial = capitalSocial;
    }

    public BigDecimalFilter getFaturamentoAnual() {
        return faturamentoAnual;
    }

    public Optional<BigDecimalFilter> optionalFaturamentoAnual() {
        return Optional.ofNullable(faturamentoAnual);
    }

    public BigDecimalFilter faturamentoAnual() {
        if (faturamentoAnual == null) {
            setFaturamentoAnual(new BigDecimalFilter());
        }
        return faturamentoAnual;
    }

    public void setFaturamentoAnual(BigDecimalFilter faturamentoAnual) {
        this.faturamentoAnual = faturamentoAnual;
    }

    public BigDecimalFilter getMediaMovimentacaoMensal() {
        return mediaMovimentacaoMensal;
    }

    public Optional<BigDecimalFilter> optionalMediaMovimentacaoMensal() {
        return Optional.ofNullable(mediaMovimentacaoMensal);
    }

    public BigDecimalFilter mediaMovimentacaoMensal() {
        if (mediaMovimentacaoMensal == null) {
            setMediaMovimentacaoMensal(new BigDecimalFilter());
        }
        return mediaMovimentacaoMensal;
    }

    public void setMediaMovimentacaoMensal(BigDecimalFilter mediaMovimentacaoMensal) {
        this.mediaMovimentacaoMensal = mediaMovimentacaoMensal;
    }

    public EnumTipoDocumentoPJFilter getTipoDocumento() {
        return tipoDocumento;
    }

    public Optional<EnumTipoDocumentoPJFilter> optionalTipoDocumento() {
        return Optional.ofNullable(tipoDocumento);
    }

    public EnumTipoDocumentoPJFilter tipoDocumento() {
        if (tipoDocumento == null) {
            setTipoDocumento(new EnumTipoDocumentoPJFilter());
        }
        return tipoDocumento;
    }

    public void setTipoDocumento(EnumTipoDocumentoPJFilter tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public StringFilter getRegimeTributario() {
        return regimeTributario;
    }

    public Optional<StringFilter> optionalRegimeTributario() {
        return Optional.ofNullable(regimeTributario);
    }

    public StringFilter regimeTributario() {
        if (regimeTributario == null) {
            setRegimeTributario(new StringFilter());
        }
        return regimeTributario;
    }

    public void setRegimeTributario(StringFilter regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public StringFilter getCodigoNaturezaJuridica() {
        return codigoNaturezaJuridica;
    }

    public Optional<StringFilter> optionalCodigoNaturezaJuridica() {
        return Optional.ofNullable(codigoNaturezaJuridica);
    }

    public StringFilter codigoNaturezaJuridica() {
        if (codigoNaturezaJuridica == null) {
            setCodigoNaturezaJuridica(new StringFilter());
        }
        return codigoNaturezaJuridica;
    }

    public void setCodigoNaturezaJuridica(StringFilter codigoNaturezaJuridica) {
        this.codigoNaturezaJuridica = codigoNaturezaJuridica;
    }

    public StringFilter getAtividadePrincipal() {
        return atividadePrincipal;
    }

    public Optional<StringFilter> optionalAtividadePrincipal() {
        return Optional.ofNullable(atividadePrincipal);
    }

    public StringFilter atividadePrincipal() {
        if (atividadePrincipal == null) {
            setAtividadePrincipal(new StringFilter());
        }
        return atividadePrincipal;
    }

    public void setAtividadePrincipal(StringFilter atividadePrincipal) {
        this.atividadePrincipal = atividadePrincipal;
    }

    public BooleanFilter getEmpresaAtiva() {
        return empresaAtiva;
    }

    public Optional<BooleanFilter> optionalEmpresaAtiva() {
        return Optional.ofNullable(empresaAtiva);
    }

    public BooleanFilter empresaAtiva() {
        if (empresaAtiva == null) {
            setEmpresaAtiva(new BooleanFilter());
        }
        return empresaAtiva;
    }

    public void setEmpresaAtiva(BooleanFilter empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
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

    public StringFilter getCpfRepresentanteLegal() {
        return cpfRepresentanteLegal;
    }

    public Optional<StringFilter> optionalCpfRepresentanteLegal() {
        return Optional.ofNullable(cpfRepresentanteLegal);
    }

    public StringFilter cpfRepresentanteLegal() {
        if (cpfRepresentanteLegal == null) {
            setCpfRepresentanteLegal(new StringFilter());
        }
        return cpfRepresentanteLegal;
    }

    public void setCpfRepresentanteLegal(StringFilter cpfRepresentanteLegal) {
        this.cpfRepresentanteLegal = cpfRepresentanteLegal;
    }

    public StringFilter getNumeroRegistro() {
        return numeroRegistro;
    }

    public Optional<StringFilter> optionalNumeroRegistro() {
        return Optional.ofNullable(numeroRegistro);
    }

    public StringFilter numeroRegistro() {
        if (numeroRegistro == null) {
            setNumeroRegistro(new StringFilter());
        }
        return numeroRegistro;
    }

    public void setNumeroRegistro(StringFilter numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
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

    public LongFilter getContratoSocialId() {
        return contratoSocialId;
    }

    public Optional<LongFilter> optionalContratoSocialId() {
        return Optional.ofNullable(contratoSocialId);
    }

    public LongFilter contratoSocialId() {
        if (contratoSocialId == null) {
            setContratoSocialId(new LongFilter());
        }
        return contratoSocialId;
    }

    public void setContratoSocialId(LongFilter contratoSocialId) {
        this.contratoSocialId = contratoSocialId;
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

    public LongFilter getTipoNegocioId() {
        return tipoNegocioId;
    }

    public Optional<LongFilter> optionalTipoNegocioId() {
        return Optional.ofNullable(tipoNegocioId);
    }

    public LongFilter tipoNegocioId() {
        if (tipoNegocioId == null) {
            setTipoNegocioId(new LongFilter());
        }
        return tipoNegocioId;
    }

    public void setTipoNegocioId(LongFilter tipoNegocioId) {
        this.tipoNegocioId = tipoNegocioId;
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
        final PessoaJuridicaCriteria that = (PessoaJuridicaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(razaoSocial, that.razaoSocial) &&
            Objects.equals(nomeFantasia, that.nomeFantasia) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(sitioWeb, that.sitioWeb) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(dataFundacao, that.dataFundacao) &&
            Objects.equals(capitalSocial, that.capitalSocial) &&
            Objects.equals(faturamentoAnual, that.faturamentoAnual) &&
            Objects.equals(mediaMovimentacaoMensal, that.mediaMovimentacaoMensal) &&
            Objects.equals(tipoDocumento, that.tipoDocumento) &&
            Objects.equals(regimeTributario, that.regimeTributario) &&
            Objects.equals(codigoNaturezaJuridica, that.codigoNaturezaJuridica) &&
            Objects.equals(atividadePrincipal, that.atividadePrincipal) &&
            Objects.equals(empresaAtiva, that.empresaAtiva) &&
            Objects.equals(nivelRisco, that.nivelRisco) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(bloqueioSaque, that.bloqueioSaque) &&
            Objects.equals(cpfRepresentanteLegal, that.cpfRepresentanteLegal) &&
            Objects.equals(numeroRegistro, that.numeroRegistro) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(moedaPrincipalId, that.moedaPrincipalId) &&
            Objects.equals(contratoSocialId, that.contratoSocialId) &&
            Objects.equals(nacionalidadeId, that.nacionalidadeId) &&
            Objects.equals(tipoNegocioId, that.tipoNegocioId) &&
            Objects.equals(planoId, that.planoId) &&
            Objects.equals(escritorioId, that.escritorioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cnpj,
            razaoSocial,
            nomeFantasia,
            telefone,
            sitioWeb,
            descricao,
            dataFundacao,
            capitalSocial,
            faturamentoAnual,
            mediaMovimentacaoMensal,
            tipoDocumento,
            regimeTributario,
            codigoNaturezaJuridica,
            atividadePrincipal,
            empresaAtiva,
            nivelRisco,
            situacao,
            bloqueioSaque,
            cpfRepresentanteLegal,
            numeroRegistro,
            usuarioId,
            moedaPrincipalId,
            contratoSocialId,
            nacionalidadeId,
            tipoNegocioId,
            planoId,
            escritorioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaJuridicaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCnpj().map(f -> "cnpj=" + f + ", ").orElse("") +
            optionalRazaoSocial().map(f -> "razaoSocial=" + f + ", ").orElse("") +
            optionalNomeFantasia().map(f -> "nomeFantasia=" + f + ", ").orElse("") +
            optionalTelefone().map(f -> "telefone=" + f + ", ").orElse("") +
            optionalSitioWeb().map(f -> "sitioWeb=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalDataFundacao().map(f -> "dataFundacao=" + f + ", ").orElse("") +
            optionalCapitalSocial().map(f -> "capitalSocial=" + f + ", ").orElse("") +
            optionalFaturamentoAnual().map(f -> "faturamentoAnual=" + f + ", ").orElse("") +
            optionalMediaMovimentacaoMensal().map(f -> "mediaMovimentacaoMensal=" + f + ", ").orElse("") +
            optionalTipoDocumento().map(f -> "tipoDocumento=" + f + ", ").orElse("") +
            optionalRegimeTributario().map(f -> "regimeTributario=" + f + ", ").orElse("") +
            optionalCodigoNaturezaJuridica().map(f -> "codigoNaturezaJuridica=" + f + ", ").orElse("") +
            optionalAtividadePrincipal().map(f -> "atividadePrincipal=" + f + ", ").orElse("") +
            optionalEmpresaAtiva().map(f -> "empresaAtiva=" + f + ", ").orElse("") +
            optionalNivelRisco().map(f -> "nivelRisco=" + f + ", ").orElse("") +
            optionalSituacao().map(f -> "situacao=" + f + ", ").orElse("") +
            optionalBloqueioSaque().map(f -> "bloqueioSaque=" + f + ", ").orElse("") +
            optionalCpfRepresentanteLegal().map(f -> "cpfRepresentanteLegal=" + f + ", ").orElse("") +
            optionalNumeroRegistro().map(f -> "numeroRegistro=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalMoedaPrincipalId().map(f -> "moedaPrincipalId=" + f + ", ").orElse("") +
            optionalContratoSocialId().map(f -> "contratoSocialId=" + f + ", ").orElse("") +
            optionalNacionalidadeId().map(f -> "nacionalidadeId=" + f + ", ").orElse("") +
            optionalTipoNegocioId().map(f -> "tipoNegocioId=" + f + ", ").orElse("") +
            optionalPlanoId().map(f -> "planoId=" + f + ", ").orElse("") +
            optionalEscritorioId().map(f -> "escritorioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
