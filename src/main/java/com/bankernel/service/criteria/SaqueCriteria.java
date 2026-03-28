package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumStatusSaque;
import com.bankernel.domain.enumeration.EnumTipoSaque;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Saque} entity. This class is used
 * in {@link com.bankernel.web.rest.SaqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /saques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaqueCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoSaque
     */
    public static class EnumTipoSaqueFilter extends Filter<EnumTipoSaque> {

        public EnumTipoSaqueFilter() {}

        public EnumTipoSaqueFilter(EnumTipoSaqueFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoSaqueFilter copy() {
            return new EnumTipoSaqueFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusSaque
     */
    public static class EnumStatusSaqueFilter extends Filter<EnumStatusSaque> {

        public EnumStatusSaqueFilter() {}

        public EnumStatusSaqueFilter(EnumStatusSaqueFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusSaqueFilter copy() {
            return new EnumStatusSaqueFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter valorSaque;

    private BigDecimalFilter valorEnviado;

    private StringFilter descricao;

    private EnumTipoSaqueFilter tipoSaque;

    private EnumStatusSaqueFilter situacaoSaque;

    private StringFilter numeroReferencia;

    private StringFilter motivoRejeicao;

    private BooleanFilter contabilizado;

    private StringFilter nomeUsuarioFixo;

    private LongFilter transacaoId;

    private LongFilter transacaoEstornoId;

    private LongFilter carteiraId;

    private LongFilter moedaCarteiraId;

    private LongFilter contaBancariaDestinoId;

    private LongFilter usuarioId;

    private LongFilter escritorioId;

    private LongFilter saquePixId;

    private LongFilter saqueBoletoId;

    private Boolean distinct;

    public SaqueCriteria() {}

    public SaqueCriteria(SaqueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valorSaque = other.optionalValorSaque().map(BigDecimalFilter::copy).orElse(null);
        this.valorEnviado = other.optionalValorEnviado().map(BigDecimalFilter::copy).orElse(null);
        this.descricao = other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.tipoSaque = other.optionalTipoSaque().map(EnumTipoSaqueFilter::copy).orElse(null);
        this.situacaoSaque = other.optionalSituacaoSaque().map(EnumStatusSaqueFilter::copy).orElse(null);
        this.numeroReferencia = other.optionalNumeroReferencia().map(StringFilter::copy).orElse(null);
        this.motivoRejeicao = other.optionalMotivoRejeicao().map(StringFilter::copy).orElse(null);
        this.contabilizado = other.optionalContabilizado().map(BooleanFilter::copy).orElse(null);
        this.nomeUsuarioFixo = other.optionalNomeUsuarioFixo().map(StringFilter::copy).orElse(null);
        this.transacaoId = other.optionalTransacaoId().map(LongFilter::copy).orElse(null);
        this.transacaoEstornoId = other.optionalTransacaoEstornoId().map(LongFilter::copy).orElse(null);
        this.carteiraId = other.optionalCarteiraId().map(LongFilter::copy).orElse(null);
        this.moedaCarteiraId = other.optionalMoedaCarteiraId().map(LongFilter::copy).orElse(null);
        this.contaBancariaDestinoId = other.optionalContaBancariaDestinoId().map(LongFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.escritorioId = other.optionalEscritorioId().map(LongFilter::copy).orElse(null);
        this.saquePixId = other.optionalSaquePixId().map(LongFilter::copy).orElse(null);
        this.saqueBoletoId = other.optionalSaqueBoletoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SaqueCriteria copy() {
        return new SaqueCriteria(this);
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

    public BigDecimalFilter getValorSaque() {
        return valorSaque;
    }

    public Optional<BigDecimalFilter> optionalValorSaque() {
        return Optional.ofNullable(valorSaque);
    }

    public BigDecimalFilter valorSaque() {
        if (valorSaque == null) {
            setValorSaque(new BigDecimalFilter());
        }
        return valorSaque;
    }

    public void setValorSaque(BigDecimalFilter valorSaque) {
        this.valorSaque = valorSaque;
    }

    public BigDecimalFilter getValorEnviado() {
        return valorEnviado;
    }

    public Optional<BigDecimalFilter> optionalValorEnviado() {
        return Optional.ofNullable(valorEnviado);
    }

    public BigDecimalFilter valorEnviado() {
        if (valorEnviado == null) {
            setValorEnviado(new BigDecimalFilter());
        }
        return valorEnviado;
    }

    public void setValorEnviado(BigDecimalFilter valorEnviado) {
        this.valorEnviado = valorEnviado;
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

    public EnumTipoSaqueFilter getTipoSaque() {
        return tipoSaque;
    }

    public Optional<EnumTipoSaqueFilter> optionalTipoSaque() {
        return Optional.ofNullable(tipoSaque);
    }

    public EnumTipoSaqueFilter tipoSaque() {
        if (tipoSaque == null) {
            setTipoSaque(new EnumTipoSaqueFilter());
        }
        return tipoSaque;
    }

    public void setTipoSaque(EnumTipoSaqueFilter tipoSaque) {
        this.tipoSaque = tipoSaque;
    }

    public EnumStatusSaqueFilter getSituacaoSaque() {
        return situacaoSaque;
    }

    public Optional<EnumStatusSaqueFilter> optionalSituacaoSaque() {
        return Optional.ofNullable(situacaoSaque);
    }

    public EnumStatusSaqueFilter situacaoSaque() {
        if (situacaoSaque == null) {
            setSituacaoSaque(new EnumStatusSaqueFilter());
        }
        return situacaoSaque;
    }

    public void setSituacaoSaque(EnumStatusSaqueFilter situacaoSaque) {
        this.situacaoSaque = situacaoSaque;
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

    public LongFilter getTransacaoEstornoId() {
        return transacaoEstornoId;
    }

    public Optional<LongFilter> optionalTransacaoEstornoId() {
        return Optional.ofNullable(transacaoEstornoId);
    }

    public LongFilter transacaoEstornoId() {
        if (transacaoEstornoId == null) {
            setTransacaoEstornoId(new LongFilter());
        }
        return transacaoEstornoId;
    }

    public void setTransacaoEstornoId(LongFilter transacaoEstornoId) {
        this.transacaoEstornoId = transacaoEstornoId;
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

    public LongFilter getContaBancariaDestinoId() {
        return contaBancariaDestinoId;
    }

    public Optional<LongFilter> optionalContaBancariaDestinoId() {
        return Optional.ofNullable(contaBancariaDestinoId);
    }

    public LongFilter contaBancariaDestinoId() {
        if (contaBancariaDestinoId == null) {
            setContaBancariaDestinoId(new LongFilter());
        }
        return contaBancariaDestinoId;
    }

    public void setContaBancariaDestinoId(LongFilter contaBancariaDestinoId) {
        this.contaBancariaDestinoId = contaBancariaDestinoId;
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

    public LongFilter getSaquePixId() {
        return saquePixId;
    }

    public Optional<LongFilter> optionalSaquePixId() {
        return Optional.ofNullable(saquePixId);
    }

    public LongFilter saquePixId() {
        if (saquePixId == null) {
            setSaquePixId(new LongFilter());
        }
        return saquePixId;
    }

    public void setSaquePixId(LongFilter saquePixId) {
        this.saquePixId = saquePixId;
    }

    public LongFilter getSaqueBoletoId() {
        return saqueBoletoId;
    }

    public Optional<LongFilter> optionalSaqueBoletoId() {
        return Optional.ofNullable(saqueBoletoId);
    }

    public LongFilter saqueBoletoId() {
        if (saqueBoletoId == null) {
            setSaqueBoletoId(new LongFilter());
        }
        return saqueBoletoId;
    }

    public void setSaqueBoletoId(LongFilter saqueBoletoId) {
        this.saqueBoletoId = saqueBoletoId;
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
        final SaqueCriteria that = (SaqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valorSaque, that.valorSaque) &&
            Objects.equals(valorEnviado, that.valorEnviado) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(tipoSaque, that.tipoSaque) &&
            Objects.equals(situacaoSaque, that.situacaoSaque) &&
            Objects.equals(numeroReferencia, that.numeroReferencia) &&
            Objects.equals(motivoRejeicao, that.motivoRejeicao) &&
            Objects.equals(contabilizado, that.contabilizado) &&
            Objects.equals(nomeUsuarioFixo, that.nomeUsuarioFixo) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(transacaoEstornoId, that.transacaoEstornoId) &&
            Objects.equals(carteiraId, that.carteiraId) &&
            Objects.equals(moedaCarteiraId, that.moedaCarteiraId) &&
            Objects.equals(contaBancariaDestinoId, that.contaBancariaDestinoId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(escritorioId, that.escritorioId) &&
            Objects.equals(saquePixId, that.saquePixId) &&
            Objects.equals(saqueBoletoId, that.saqueBoletoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valorSaque,
            valorEnviado,
            descricao,
            tipoSaque,
            situacaoSaque,
            numeroReferencia,
            motivoRejeicao,
            contabilizado,
            nomeUsuarioFixo,
            transacaoId,
            transacaoEstornoId,
            carteiraId,
            moedaCarteiraId,
            contaBancariaDestinoId,
            usuarioId,
            escritorioId,
            saquePixId,
            saqueBoletoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaqueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValorSaque().map(f -> "valorSaque=" + f + ", ").orElse("") +
            optionalValorEnviado().map(f -> "valorEnviado=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalTipoSaque().map(f -> "tipoSaque=" + f + ", ").orElse("") +
            optionalSituacaoSaque().map(f -> "situacaoSaque=" + f + ", ").orElse("") +
            optionalNumeroReferencia().map(f -> "numeroReferencia=" + f + ", ").orElse("") +
            optionalMotivoRejeicao().map(f -> "motivoRejeicao=" + f + ", ").orElse("") +
            optionalContabilizado().map(f -> "contabilizado=" + f + ", ").orElse("") +
            optionalNomeUsuarioFixo().map(f -> "nomeUsuarioFixo=" + f + ", ").orElse("") +
            optionalTransacaoId().map(f -> "transacaoId=" + f + ", ").orElse("") +
            optionalTransacaoEstornoId().map(f -> "transacaoEstornoId=" + f + ", ").orElse("") +
            optionalCarteiraId().map(f -> "carteiraId=" + f + ", ").orElse("") +
            optionalMoedaCarteiraId().map(f -> "moedaCarteiraId=" + f + ", ").orElse("") +
            optionalContaBancariaDestinoId().map(f -> "contaBancariaDestinoId=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalEscritorioId().map(f -> "escritorioId=" + f + ", ").orElse("") +
            optionalSaquePixId().map(f -> "saquePixId=" + f + ", ").orElse("") +
            optionalSaqueBoletoId().map(f -> "saqueBoletoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
