package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumStatusTransferencia;
import com.bankernel.domain.enumeration.EnumTipoChaveTransferencia;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Transferencia} entity. This class is used
 * in {@link com.bankernel.web.rest.TransferenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transferencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransferenciaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoChaveTransferencia
     */
    public static class EnumTipoChaveTransferenciaFilter extends Filter<EnumTipoChaveTransferencia> {

        public EnumTipoChaveTransferenciaFilter() {}

        public EnumTipoChaveTransferenciaFilter(EnumTipoChaveTransferenciaFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoChaveTransferenciaFilter copy() {
            return new EnumTipoChaveTransferenciaFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusTransferencia
     */
    public static class EnumStatusTransferenciaFilter extends Filter<EnumStatusTransferencia> {

        public EnumStatusTransferenciaFilter() {}

        public EnumStatusTransferenciaFilter(EnumStatusTransferenciaFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusTransferenciaFilter copy() {
            return new EnumStatusTransferenciaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valor;

    private StringFilter chaveInterna;

    private EnumTipoChaveTransferenciaFilter tipoChave;

    private EnumStatusTransferenciaFilter situacao;

    private StringFilter descricao;

    private StringFilter motivoRejeicao;

    private StringFilter numeroReferencia;

    private LongFilter transacaoId;

    private LongFilter usuarioOrigemId;

    private LongFilter usuarioDestinoId;

    private LongFilter carteiraOrigemId;

    private LongFilter carteiraDestinoId;

    private LongFilter moedaCarteiraId;

    private Boolean distinct;

    public TransferenciaCriteria() {}

    public TransferenciaCriteria(TransferenciaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valor = other.optionalValor().map(BigDecimalFilter::copy).orElse(null);
        this.chaveInterna = other.optionalChaveInterna().map(StringFilter::copy).orElse(null);
        this.tipoChave = other.optionalTipoChave().map(EnumTipoChaveTransferenciaFilter::copy).orElse(null);
        this.situacao = other.optionalSituacao().map(EnumStatusTransferenciaFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.motivoRejeicao = other.optionalMotivoRejeicao().map(StringFilter::copy).orElse(null);
        this.numeroReferencia = other.optionalNumeroReferencia().map(StringFilter::copy).orElse(null);
        this.transacaoId = other.optionalTransacaoId().map(LongFilter::copy).orElse(null);
        this.usuarioOrigemId = other.optionalUsuarioOrigemId().map(LongFilter::copy).orElse(null);
        this.usuarioDestinoId = other.optionalUsuarioDestinoId().map(LongFilter::copy).orElse(null);
        this.carteiraOrigemId = other.optionalCarteiraOrigemId().map(LongFilter::copy).orElse(null);
        this.carteiraDestinoId = other.optionalCarteiraDestinoId().map(LongFilter::copy).orElse(null);
        this.moedaCarteiraId = other.optionalMoedaCarteiraId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransferenciaCriteria copy() {
        return new TransferenciaCriteria(this);
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

    public StringFilter getChaveInterna() {
        return chaveInterna;
    }

    public Optional<StringFilter> optionalChaveInterna() {
        return Optional.ofNullable(chaveInterna);
    }

    public StringFilter chaveInterna() {
        if (chaveInterna == null) {
            setChaveInterna(new StringFilter());
        }
        return chaveInterna;
    }

    public void setChaveInterna(StringFilter chaveInterna) {
        this.chaveInterna = chaveInterna;
    }

    public EnumTipoChaveTransferenciaFilter getTipoChave() {
        return tipoChave;
    }

    public Optional<EnumTipoChaveTransferenciaFilter> optionalTipoChave() {
        return Optional.ofNullable(tipoChave);
    }

    public EnumTipoChaveTransferenciaFilter tipoChave() {
        if (tipoChave == null) {
            setTipoChave(new EnumTipoChaveTransferenciaFilter());
        }
        return tipoChave;
    }

    public void setTipoChave(EnumTipoChaveTransferenciaFilter tipoChave) {
        this.tipoChave = tipoChave;
    }

    public EnumStatusTransferenciaFilter getSituacao() {
        return situacao;
    }

    public Optional<EnumStatusTransferenciaFilter> optionalSituacao() {
        return Optional.ofNullable(situacao);
    }

    public EnumStatusTransferenciaFilter situacao() {
        if (situacao == null) {
            setSituacao(new EnumStatusTransferenciaFilter());
        }
        return situacao;
    }

    public void setSituacao(EnumStatusTransferenciaFilter situacao) {
        this.situacao = situacao;
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

    public StringFilter getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public Optional<StringFilter> optionalMotivoRejeicao() {
        return Optional.ofNullable(motivoRejeicao);
    }

    public StringFilter motivoRejeicao() {
        if (motivoRejeicao == null) {
            setMotivoRejeicao(new StringFilter());
        }
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(StringFilter motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public StringFilter getNumeroReferencia() {
        return numeroReferencia;
    }

    public Optional<StringFilter> optionalNumeroReferencia() {
        return Optional.ofNullable(numeroReferencia);
    }

    public StringFilter numeroReferencia() {
        if (numeroReferencia == null) {
            setNumeroReferencia(new StringFilter());
        }
        return numeroReferencia;
    }

    public void setNumeroReferencia(StringFilter numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
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

    public LongFilter getUsuarioOrigemId() {
        return usuarioOrigemId;
    }

    public Optional<LongFilter> optionalUsuarioOrigemId() {
        return Optional.ofNullable(usuarioOrigemId);
    }

    public LongFilter usuarioOrigemId() {
        if (usuarioOrigemId == null) {
            setUsuarioOrigemId(new LongFilter());
        }
        return usuarioOrigemId;
    }

    public void setUsuarioOrigemId(LongFilter usuarioOrigemId) {
        this.usuarioOrigemId = usuarioOrigemId;
    }

    public LongFilter getUsuarioDestinoId() {
        return usuarioDestinoId;
    }

    public Optional<LongFilter> optionalUsuarioDestinoId() {
        return Optional.ofNullable(usuarioDestinoId);
    }

    public LongFilter usuarioDestinoId() {
        if (usuarioDestinoId == null) {
            setUsuarioDestinoId(new LongFilter());
        }
        return usuarioDestinoId;
    }

    public void setUsuarioDestinoId(LongFilter usuarioDestinoId) {
        this.usuarioDestinoId = usuarioDestinoId;
    }

    public LongFilter getCarteiraOrigemId() {
        return carteiraOrigemId;
    }

    public Optional<LongFilter> optionalCarteiraOrigemId() {
        return Optional.ofNullable(carteiraOrigemId);
    }

    public LongFilter carteiraOrigemId() {
        if (carteiraOrigemId == null) {
            setCarteiraOrigemId(new LongFilter());
        }
        return carteiraOrigemId;
    }

    public void setCarteiraOrigemId(LongFilter carteiraOrigemId) {
        this.carteiraOrigemId = carteiraOrigemId;
    }

    public LongFilter getCarteiraDestinoId() {
        return carteiraDestinoId;
    }

    public Optional<LongFilter> optionalCarteiraDestinoId() {
        return Optional.ofNullable(carteiraDestinoId);
    }

    public LongFilter carteiraDestinoId() {
        if (carteiraDestinoId == null) {
            setCarteiraDestinoId(new LongFilter());
        }
        return carteiraDestinoId;
    }

    public void setCarteiraDestinoId(LongFilter carteiraDestinoId) {
        this.carteiraDestinoId = carteiraDestinoId;
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
        final TransferenciaCriteria that = (TransferenciaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(chaveInterna, that.chaveInterna) &&
            Objects.equals(tipoChave, that.tipoChave) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(motivoRejeicao, that.motivoRejeicao) &&
            Objects.equals(numeroReferencia, that.numeroReferencia) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(usuarioOrigemId, that.usuarioOrigemId) &&
            Objects.equals(usuarioDestinoId, that.usuarioDestinoId) &&
            Objects.equals(carteiraOrigemId, that.carteiraOrigemId) &&
            Objects.equals(carteiraDestinoId, that.carteiraDestinoId) &&
            Objects.equals(moedaCarteiraId, that.moedaCarteiraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valor,
            chaveInterna,
            tipoChave,
            situacao,
            descricao,
            motivoRejeicao,
            numeroReferencia,
            transacaoId,
            usuarioOrigemId,
            usuarioDestinoId,
            carteiraOrigemId,
            carteiraDestinoId,
            moedaCarteiraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransferenciaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValor().map(f -> "valor=" + f + ", ").orElse("") +
            optionalChaveInterna().map(f -> "chaveInterna=" + f + ", ").orElse("") +
            optionalTipoChave().map(f -> "tipoChave=" + f + ", ").orElse("") +
            optionalSituacao().map(f -> "situacao=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalMotivoRejeicao().map(f -> "motivoRejeicao=" + f + ", ").orElse("") +
            optionalNumeroReferencia().map(f -> "numeroReferencia=" + f + ", ").orElse("") +
            optionalTransacaoId().map(f -> "transacaoId=" + f + ", ").orElse("") +
            optionalUsuarioOrigemId().map(f -> "usuarioOrigemId=" + f + ", ").orElse("") +
            optionalUsuarioDestinoId().map(f -> "usuarioDestinoId=" + f + ", ").orElse("") +
            optionalCarteiraOrigemId().map(f -> "carteiraOrigemId=" + f + ", ").orElse("") +
            optionalCarteiraDestinoId().map(f -> "carteiraDestinoId=" + f + ", ").orElse("") +
            optionalMoedaCarteiraId().map(f -> "moedaCarteiraId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
