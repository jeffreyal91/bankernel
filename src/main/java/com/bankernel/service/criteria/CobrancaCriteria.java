package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumStatusCobranca;
import com.bankernel.domain.enumeration.EnumTipoCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Cobranca} entity. This class is used
 * in {@link com.bankernel.web.rest.CobrancaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cobrancas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CobrancaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumStatusCobranca
     */
    public static class EnumStatusCobrancaFilter extends Filter<EnumStatusCobranca> {

        public EnumStatusCobrancaFilter() {}

        public EnumStatusCobrancaFilter(EnumStatusCobrancaFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusCobrancaFilter copy() {
            return new EnumStatusCobrancaFilter(this);
        }
    }

    /**
     * Class for filtering EnumTipoCobranca
     */
    public static class EnumTipoCobrancaFilter extends Filter<EnumTipoCobranca> {

        public EnumTipoCobrancaFilter() {}

        public EnumTipoCobrancaFilter(EnumTipoCobrancaFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoCobrancaFilter copy() {
            return new EnumTipoCobrancaFilter(this);
        }
    }

    /**
     * Class for filtering EnumTipoDesconto
     */
    public static class EnumTipoDescontoFilter extends Filter<EnumTipoDesconto> {

        public EnumTipoDescontoFilter() {}

        public EnumTipoDescontoFilter(EnumTipoDescontoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoDescontoFilter copy() {
            return new EnumTipoDescontoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valor;

    private BigDecimalFilter valorCreditado;

    private BigDecimalFilter valorCreditadoCarteira;

    private StringFilter idProdutoExterno;

    private StringFilter nomeProdutoExterno;

    private EnumStatusCobrancaFilter situacao;

    private EnumTipoCobrancaFilter tipo;

    private BigDecimalFilter descontoGeral;

    private EnumTipoDescontoFilter tipoDesconto;

    private BooleanFilter contabilizado;

    private StringFilter nomeUsuarioFixo;

    private StringFilter chaveCobranca;

    private StringFilter identificadorExterno;

    private BooleanFilter retornoNotificado;

    private LongFilter transacaoId;

    private LongFilter usuarioId;

    private LongFilter carteiraId;

    private LongFilter carteiraCreditadaId;

    private LongFilter moedaCarteiraId;

    private LongFilter linkCobrancaId;

    private Boolean distinct;

    public CobrancaCriteria() {}

    public CobrancaCriteria(CobrancaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valor = other.optionalValor().map(BigDecimalFilter::copy).orElse(null);
        this.valorCreditado = other.optionalValorCreditado().map(BigDecimalFilter::copy).orElse(null);
        this.valorCreditadoCarteira = other.optionalValorCreditadoCarteira().map(BigDecimalFilter::copy).orElse(null);
        this.idProdutoExterno = other.optionalIdProdutoExterno().map(StringFilter::copy).orElse(null);
        this.nomeProdutoExterno = other.optionalNomeProdutoExterno().map(StringFilter::copy).orElse(null);
        this.situacao = other.optionalSituacao().map(EnumStatusCobrancaFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(EnumTipoCobrancaFilter::copy).orElse(null);
        this.descontoGeral = other.optionalDescontoGeral().map(BigDecimalFilter::copy).orElse(null);
        this.tipoDesconto = other.optionalTipoDesconto().map(EnumTipoDescontoFilter::copy).orElse(null);
        this.contabilizado = other.optionalContabilizado().map(BooleanFilter::copy).orElse(null);
        this.nomeUsuarioFixo = other.optionalNomeUsuarioFixo().map(StringFilter::copy).orElse(null);
        this.chaveCobranca = other.optionalChaveCobranca().map(StringFilter::copy).orElse(null);
        this.identificadorExterno = other.optionalIdentificadorExterno().map(StringFilter::copy).orElse(null);
        this.retornoNotificado = other.optionalRetornoNotificado().map(BooleanFilter::copy).orElse(null);
        this.transacaoId = other.optionalTransacaoId().map(LongFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.carteiraId = other.optionalCarteiraId().map(LongFilter::copy).orElse(null);
        this.carteiraCreditadaId = other.optionalCarteiraCreditadaId().map(LongFilter::copy).orElse(null);
        this.moedaCarteiraId = other.optionalMoedaCarteiraId().map(LongFilter::copy).orElse(null);
        this.linkCobrancaId = other.optionalLinkCobrancaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CobrancaCriteria copy() {
        return new CobrancaCriteria(this);
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

    public BigDecimalFilter getValor() {
        return valor;
    }

    public Optional<BigDecimalFilter> optionalValor() {
        return Optional.ofNullable(valor);
    }

    public BigDecimalFilter valor() {
        if (valor == null) {
            setValor(new BigDecimalFilter());
        }
        return valor;
    }

    public void setValor(BigDecimalFilter valor) {
        this.valor = valor;
    }

    public BigDecimalFilter getValorCreditado() {
        return valorCreditado;
    }

    public Optional<BigDecimalFilter> optionalValorCreditado() {
        return Optional.ofNullable(valorCreditado);
    }

    public BigDecimalFilter valorCreditado() {
        if (valorCreditado == null) {
            setValorCreditado(new BigDecimalFilter());
        }
        return valorCreditado;
    }

    public void setValorCreditado(BigDecimalFilter valorCreditado) {
        this.valorCreditado = valorCreditado;
    }

    public BigDecimalFilter getValorCreditadoCarteira() {
        return valorCreditadoCarteira;
    }

    public Optional<BigDecimalFilter> optionalValorCreditadoCarteira() {
        return Optional.ofNullable(valorCreditadoCarteira);
    }

    public BigDecimalFilter valorCreditadoCarteira() {
        if (valorCreditadoCarteira == null) {
            setValorCreditadoCarteira(new BigDecimalFilter());
        }
        return valorCreditadoCarteira;
    }

    public void setValorCreditadoCarteira(BigDecimalFilter valorCreditadoCarteira) {
        this.valorCreditadoCarteira = valorCreditadoCarteira;
    }

    public StringFilter getIdProdutoExterno() {
        return idProdutoExterno;
    }

    public Optional<StringFilter> optionalIdProdutoExterno() {
        return Optional.ofNullable(idProdutoExterno);
    }

    public StringFilter idProdutoExterno() {
        if (idProdutoExterno == null) {
            setIdProdutoExterno(new StringFilter());
        }
        return idProdutoExterno;
    }

    public void setIdProdutoExterno(StringFilter idProdutoExterno) {
        this.idProdutoExterno = idProdutoExterno;
    }

    public StringFilter getNomeProdutoExterno() {
        return nomeProdutoExterno;
    }

    public Optional<StringFilter> optionalNomeProdutoExterno() {
        return Optional.ofNullable(nomeProdutoExterno);
    }

    public StringFilter nomeProdutoExterno() {
        if (nomeProdutoExterno == null) {
            setNomeProdutoExterno(new StringFilter());
        }
        return nomeProdutoExterno;
    }

    public void setNomeProdutoExterno(StringFilter nomeProdutoExterno) {
        this.nomeProdutoExterno = nomeProdutoExterno;
    }

    public EnumStatusCobrancaFilter getSituacao() {
        return situacao;
    }

    public Optional<EnumStatusCobrancaFilter> optionalSituacao() {
        return Optional.ofNullable(situacao);
    }

    public EnumStatusCobrancaFilter situacao() {
        if (situacao == null) {
            setSituacao(new EnumStatusCobrancaFilter());
        }
        return situacao;
    }

    public void setSituacao(EnumStatusCobrancaFilter situacao) {
        this.situacao = situacao;
    }

    public EnumTipoCobrancaFilter getTipo() {
        return tipo;
    }

    public Optional<EnumTipoCobrancaFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public EnumTipoCobrancaFilter tipo() {
        if (tipo == null) {
            setTipo(new EnumTipoCobrancaFilter());
        }
        return tipo;
    }

    public void setTipo(EnumTipoCobrancaFilter tipo) {
        this.tipo = tipo;
    }

    public BigDecimalFilter getDescontoGeral() {
        return descontoGeral;
    }

    public Optional<BigDecimalFilter> optionalDescontoGeral() {
        return Optional.ofNullable(descontoGeral);
    }

    public BigDecimalFilter descontoGeral() {
        if (descontoGeral == null) {
            setDescontoGeral(new BigDecimalFilter());
        }
        return descontoGeral;
    }

    public void setDescontoGeral(BigDecimalFilter descontoGeral) {
        this.descontoGeral = descontoGeral;
    }

    public EnumTipoDescontoFilter getTipoDesconto() {
        return tipoDesconto;
    }

    public Optional<EnumTipoDescontoFilter> optionalTipoDesconto() {
        return Optional.ofNullable(tipoDesconto);
    }

    public EnumTipoDescontoFilter tipoDesconto() {
        if (tipoDesconto == null) {
            setTipoDesconto(new EnumTipoDescontoFilter());
        }
        return tipoDesconto;
    }

    public void setTipoDesconto(EnumTipoDescontoFilter tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public BooleanFilter getContabilizado() {
        return contabilizado;
    }

    public Optional<BooleanFilter> optionalContabilizado() {
        return Optional.ofNullable(contabilizado);
    }

    public BooleanFilter contabilizado() {
        if (contabilizado == null) {
            setContabilizado(new BooleanFilter());
        }
        return contabilizado;
    }

    public void setContabilizado(BooleanFilter contabilizado) {
        this.contabilizado = contabilizado;
    }

    public StringFilter getNomeUsuarioFixo() {
        return nomeUsuarioFixo;
    }

    public Optional<StringFilter> optionalNomeUsuarioFixo() {
        return Optional.ofNullable(nomeUsuarioFixo);
    }

    public StringFilter nomeUsuarioFixo() {
        if (nomeUsuarioFixo == null) {
            setNomeUsuarioFixo(new StringFilter());
        }
        return nomeUsuarioFixo;
    }

    public void setNomeUsuarioFixo(StringFilter nomeUsuarioFixo) {
        this.nomeUsuarioFixo = nomeUsuarioFixo;
    }

    public StringFilter getChaveCobranca() {
        return chaveCobranca;
    }

    public Optional<StringFilter> optionalChaveCobranca() {
        return Optional.ofNullable(chaveCobranca);
    }

    public StringFilter chaveCobranca() {
        if (chaveCobranca == null) {
            setChaveCobranca(new StringFilter());
        }
        return chaveCobranca;
    }

    public void setChaveCobranca(StringFilter chaveCobranca) {
        this.chaveCobranca = chaveCobranca;
    }

    public StringFilter getIdentificadorExterno() {
        return identificadorExterno;
    }

    public Optional<StringFilter> optionalIdentificadorExterno() {
        return Optional.ofNullable(identificadorExterno);
    }

    public StringFilter identificadorExterno() {
        if (identificadorExterno == null) {
            setIdentificadorExterno(new StringFilter());
        }
        return identificadorExterno;
    }

    public void setIdentificadorExterno(StringFilter identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public BooleanFilter getRetornoNotificado() {
        return retornoNotificado;
    }

    public Optional<BooleanFilter> optionalRetornoNotificado() {
        return Optional.ofNullable(retornoNotificado);
    }

    public BooleanFilter retornoNotificado() {
        if (retornoNotificado == null) {
            setRetornoNotificado(new BooleanFilter());
        }
        return retornoNotificado;
    }

    public void setRetornoNotificado(BooleanFilter retornoNotificado) {
        this.retornoNotificado = retornoNotificado;
    }

    public LongFilter getTransacaoId() {
        return transacaoId;
    }

    public Optional<LongFilter> optionalTransacaoId() {
        return Optional.ofNullable(transacaoId);
    }

    public LongFilter transacaoId() {
        if (transacaoId == null) {
            setTransacaoId(new LongFilter());
        }
        return transacaoId;
    }

    public void setTransacaoId(LongFilter transacaoId) {
        this.transacaoId = transacaoId;
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

    public LongFilter getCarteiraId() {
        return carteiraId;
    }

    public Optional<LongFilter> optionalCarteiraId() {
        return Optional.ofNullable(carteiraId);
    }

    public LongFilter carteiraId() {
        if (carteiraId == null) {
            setCarteiraId(new LongFilter());
        }
        return carteiraId;
    }

    public void setCarteiraId(LongFilter carteiraId) {
        this.carteiraId = carteiraId;
    }

    public LongFilter getCarteiraCreditadaId() {
        return carteiraCreditadaId;
    }

    public Optional<LongFilter> optionalCarteiraCreditadaId() {
        return Optional.ofNullable(carteiraCreditadaId);
    }

    public LongFilter carteiraCreditadaId() {
        if (carteiraCreditadaId == null) {
            setCarteiraCreditadaId(new LongFilter());
        }
        return carteiraCreditadaId;
    }

    public void setCarteiraCreditadaId(LongFilter carteiraCreditadaId) {
        this.carteiraCreditadaId = carteiraCreditadaId;
    }

    public LongFilter getMoedaCarteiraId() {
        return moedaCarteiraId;
    }

    public Optional<LongFilter> optionalMoedaCarteiraId() {
        return Optional.ofNullable(moedaCarteiraId);
    }

    public LongFilter moedaCarteiraId() {
        if (moedaCarteiraId == null) {
            setMoedaCarteiraId(new LongFilter());
        }
        return moedaCarteiraId;
    }

    public void setMoedaCarteiraId(LongFilter moedaCarteiraId) {
        this.moedaCarteiraId = moedaCarteiraId;
    }

    public LongFilter getLinkCobrancaId() {
        return linkCobrancaId;
    }

    public Optional<LongFilter> optionalLinkCobrancaId() {
        return Optional.ofNullable(linkCobrancaId);
    }

    public LongFilter linkCobrancaId() {
        if (linkCobrancaId == null) {
            setLinkCobrancaId(new LongFilter());
        }
        return linkCobrancaId;
    }

    public void setLinkCobrancaId(LongFilter linkCobrancaId) {
        this.linkCobrancaId = linkCobrancaId;
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
        final CobrancaCriteria that = (CobrancaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(valorCreditado, that.valorCreditado) &&
            Objects.equals(valorCreditadoCarteira, that.valorCreditadoCarteira) &&
            Objects.equals(idProdutoExterno, that.idProdutoExterno) &&
            Objects.equals(nomeProdutoExterno, that.nomeProdutoExterno) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(descontoGeral, that.descontoGeral) &&
            Objects.equals(tipoDesconto, that.tipoDesconto) &&
            Objects.equals(contabilizado, that.contabilizado) &&
            Objects.equals(nomeUsuarioFixo, that.nomeUsuarioFixo) &&
            Objects.equals(chaveCobranca, that.chaveCobranca) &&
            Objects.equals(identificadorExterno, that.identificadorExterno) &&
            Objects.equals(retornoNotificado, that.retornoNotificado) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(carteiraId, that.carteiraId) &&
            Objects.equals(carteiraCreditadaId, that.carteiraCreditadaId) &&
            Objects.equals(moedaCarteiraId, that.moedaCarteiraId) &&
            Objects.equals(linkCobrancaId, that.linkCobrancaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valor,
            valorCreditado,
            valorCreditadoCarteira,
            idProdutoExterno,
            nomeProdutoExterno,
            situacao,
            tipo,
            descontoGeral,
            tipoDesconto,
            contabilizado,
            nomeUsuarioFixo,
            chaveCobranca,
            identificadorExterno,
            retornoNotificado,
            transacaoId,
            usuarioId,
            carteiraId,
            carteiraCreditadaId,
            moedaCarteiraId,
            linkCobrancaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CobrancaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValor().map(f -> "valor=" + f + ", ").orElse("") +
            optionalValorCreditado().map(f -> "valorCreditado=" + f + ", ").orElse("") +
            optionalValorCreditadoCarteira().map(f -> "valorCreditadoCarteira=" + f + ", ").orElse("") +
            optionalIdProdutoExterno().map(f -> "idProdutoExterno=" + f + ", ").orElse("") +
            optionalNomeProdutoExterno().map(f -> "nomeProdutoExterno=" + f + ", ").orElse("") +
            optionalSituacao().map(f -> "situacao=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalDescontoGeral().map(f -> "descontoGeral=" + f + ", ").orElse("") +
            optionalTipoDesconto().map(f -> "tipoDesconto=" + f + ", ").orElse("") +
            optionalContabilizado().map(f -> "contabilizado=" + f + ", ").orElse("") +
            optionalNomeUsuarioFixo().map(f -> "nomeUsuarioFixo=" + f + ", ").orElse("") +
            optionalChaveCobranca().map(f -> "chaveCobranca=" + f + ", ").orElse("") +
            optionalIdentificadorExterno().map(f -> "identificadorExterno=" + f + ", ").orElse("") +
            optionalRetornoNotificado().map(f -> "retornoNotificado=" + f + ", ").orElse("") +
            optionalTransacaoId().map(f -> "transacaoId=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalCarteiraId().map(f -> "carteiraId=" + f + ", ").orElse("") +
            optionalCarteiraCreditadaId().map(f -> "carteiraCreditadaId=" + f + ", ").orElse("") +
            optionalMoedaCarteiraId().map(f -> "moedaCarteiraId=" + f + ", ").orElse("") +
            optionalLinkCobrancaId().map(f -> "linkCobrancaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
