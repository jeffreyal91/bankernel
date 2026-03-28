package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumTipoIntegracao;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.RegistroIntegracao} entity. This class is used
 * in {@link com.bankernel.web.rest.RegistroIntegracaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /registro-integracaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistroIntegracaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoIntegracao
     */
    public static class EnumTipoIntegracaoFilter extends Filter<EnumTipoIntegracao> {

        public EnumTipoIntegracaoFilter() {}

        public EnumTipoIntegracaoFilter(EnumTipoIntegracaoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoIntegracaoFilter copy() {
            return new EnumTipoIntegracaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fornecedor;

    private EnumTipoIntegracaoFilter tipoIntegracao;

    private StringFilter operacao;

    private IntegerFilter codigoHttp;

    private BooleanFilter sucesso;

    private StringFilter mensagemErro;

    private LongFilter duracaoMilissegundos;

    private StringFilter tipoEntidadeOrigem;

    private LongFilter idEntidadeOrigem;

    private Boolean distinct;

    public RegistroIntegracaoCriteria() {}

    public RegistroIntegracaoCriteria(RegistroIntegracaoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fornecedor = other.optionalFornecedor().map(StringFilter::copy).orElse(null);
        this.tipoIntegracao = other.optionalTipoIntegracao().map(EnumTipoIntegracaoFilter::copy).orElse(null);
        this.operacao = other.optionalOperacao().map(StringFilter::copy).orElse(null);
        this.codigoHttp = other.optionalCodigoHttp().map(IntegerFilter::copy).orElse(null);
        this.sucesso = other.optionalSucesso().map(BooleanFilter::copy).orElse(null);
        this.mensagemErro = other.optionalMensagemErro().map(StringFilter::copy).orElse(null);
        this.duracaoMilissegundos = other.optionalDuracaoMilissegundos().map(LongFilter::copy).orElse(null);
        this.tipoEntidadeOrigem = other.optionalTipoEntidadeOrigem().map(StringFilter::copy).orElse(null);
        this.idEntidadeOrigem = other.optionalIdEntidadeOrigem().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RegistroIntegracaoCriteria copy() {
        return new RegistroIntegracaoCriteria(this);
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

    public StringFilter getFornecedor() {
        return fornecedor;
    }

    public Optional<StringFilter> optionalFornecedor() {
        return Optional.ofNullable(fornecedor);
    }

    public StringFilter fornecedor() {
        if (fornecedor == null) {
            setFornecedor(new StringFilter());
        }
        return fornecedor;
    }

    public void setFornecedor(StringFilter fornecedor) {
        this.fornecedor = fornecedor;
    }

    public EnumTipoIntegracaoFilter getTipoIntegracao() {
        return tipoIntegracao;
    }

    public Optional<EnumTipoIntegracaoFilter> optionalTipoIntegracao() {
        return Optional.ofNullable(tipoIntegracao);
    }

    public EnumTipoIntegracaoFilter tipoIntegracao() {
        if (tipoIntegracao == null) {
            setTipoIntegracao(new EnumTipoIntegracaoFilter());
        }
        return tipoIntegracao;
    }

    public void setTipoIntegracao(EnumTipoIntegracaoFilter tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
    }

    public StringFilter getOperacao() {
        return operacao;
    }

    public Optional<StringFilter> optionalOperacao() {
        return Optional.ofNullable(operacao);
    }

    public StringFilter operacao() {
        if (operacao == null) {
            setOperacao(new StringFilter());
        }
        return operacao;
    }

    public void setOperacao(StringFilter operacao) {
        this.operacao = operacao;
    }

    public IntegerFilter getCodigoHttp() {
        return codigoHttp;
    }

    public Optional<IntegerFilter> optionalCodigoHttp() {
        return Optional.ofNullable(codigoHttp);
    }

    public IntegerFilter codigoHttp() {
        if (codigoHttp == null) {
            setCodigoHttp(new IntegerFilter());
        }
        return codigoHttp;
    }

    public void setCodigoHttp(IntegerFilter codigoHttp) {
        this.codigoHttp = codigoHttp;
    }

    public BooleanFilter getSucesso() {
        return sucesso;
    }

    public Optional<BooleanFilter> optionalSucesso() {
        return Optional.ofNullable(sucesso);
    }

    public BooleanFilter sucesso() {
        if (sucesso == null) {
            setSucesso(new BooleanFilter());
        }
        return sucesso;
    }

    public void setSucesso(BooleanFilter sucesso) {
        this.sucesso = sucesso;
    }

    public StringFilter getMensagemErro() {
        return mensagemErro;
    }

    public Optional<StringFilter> optionalMensagemErro() {
        return Optional.ofNullable(mensagemErro);
    }

    public StringFilter mensagemErro() {
        if (mensagemErro == null) {
            setMensagemErro(new StringFilter());
        }
        return mensagemErro;
    }

    public void setMensagemErro(StringFilter mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public LongFilter getDuracaoMilissegundos() {
        return duracaoMilissegundos;
    }

    public Optional<LongFilter> optionalDuracaoMilissegundos() {
        return Optional.ofNullable(duracaoMilissegundos);
    }

    public LongFilter duracaoMilissegundos() {
        if (duracaoMilissegundos == null) {
            setDuracaoMilissegundos(new LongFilter());
        }
        return duracaoMilissegundos;
    }

    public void setDuracaoMilissegundos(LongFilter duracaoMilissegundos) {
        this.duracaoMilissegundos = duracaoMilissegundos;
    }

    public StringFilter getTipoEntidadeOrigem() {
        return tipoEntidadeOrigem;
    }

    public Optional<StringFilter> optionalTipoEntidadeOrigem() {
        return Optional.ofNullable(tipoEntidadeOrigem);
    }

    public StringFilter tipoEntidadeOrigem() {
        if (tipoEntidadeOrigem == null) {
            setTipoEntidadeOrigem(new StringFilter());
        }
        return tipoEntidadeOrigem;
    }

    public void setTipoEntidadeOrigem(StringFilter tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public LongFilter getIdEntidadeOrigem() {
        return idEntidadeOrigem;
    }

    public Optional<LongFilter> optionalIdEntidadeOrigem() {
        return Optional.ofNullable(idEntidadeOrigem);
    }

    public LongFilter idEntidadeOrigem() {
        if (idEntidadeOrigem == null) {
            setIdEntidadeOrigem(new LongFilter());
        }
        return idEntidadeOrigem;
    }

    public void setIdEntidadeOrigem(LongFilter idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
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
        final RegistroIntegracaoCriteria that = (RegistroIntegracaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fornecedor, that.fornecedor) &&
            Objects.equals(tipoIntegracao, that.tipoIntegracao) &&
            Objects.equals(operacao, that.operacao) &&
            Objects.equals(codigoHttp, that.codigoHttp) &&
            Objects.equals(sucesso, that.sucesso) &&
            Objects.equals(mensagemErro, that.mensagemErro) &&
            Objects.equals(duracaoMilissegundos, that.duracaoMilissegundos) &&
            Objects.equals(tipoEntidadeOrigem, that.tipoEntidadeOrigem) &&
            Objects.equals(idEntidadeOrigem, that.idEntidadeOrigem) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fornecedor,
            tipoIntegracao,
            operacao,
            codigoHttp,
            sucesso,
            mensagemErro,
            duracaoMilissegundos,
            tipoEntidadeOrigem,
            idEntidadeOrigem,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroIntegracaoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFornecedor().map(f -> "fornecedor=" + f + ", ").orElse("") +
            optionalTipoIntegracao().map(f -> "tipoIntegracao=" + f + ", ").orElse("") +
            optionalOperacao().map(f -> "operacao=" + f + ", ").orElse("") +
            optionalCodigoHttp().map(f -> "codigoHttp=" + f + ", ").orElse("") +
            optionalSucesso().map(f -> "sucesso=" + f + ", ").orElse("") +
            optionalMensagemErro().map(f -> "mensagemErro=" + f + ", ").orElse("") +
            optionalDuracaoMilissegundos().map(f -> "duracaoMilissegundos=" + f + ", ").orElse("") +
            optionalTipoEntidadeOrigem().map(f -> "tipoEntidadeOrigem=" + f + ", ").orElse("") +
            optionalIdEntidadeOrigem().map(f -> "idEntidadeOrigem=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
