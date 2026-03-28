package com.bankernel.service.criteria;

import com.bankernel.domain.enumeration.EnumCanalNotificacao;
import com.bankernel.domain.enumeration.EnumStatusNotificacao;
import com.bankernel.domain.enumeration.EnumTipoNotificacao;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bankernel.domain.Notificacao} entity. This class is used
 * in {@link com.bankernel.web.rest.NotificacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notificacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTipoNotificacao
     */
    public static class EnumTipoNotificacaoFilter extends Filter<EnumTipoNotificacao> {

        public EnumTipoNotificacaoFilter() {}

        public EnumTipoNotificacaoFilter(EnumTipoNotificacaoFilter filter) {
            super(filter);
        }

        @Override
        public EnumTipoNotificacaoFilter copy() {
            return new EnumTipoNotificacaoFilter(this);
        }
    }

    /**
     * Class for filtering EnumStatusNotificacao
     */
    public static class EnumStatusNotificacaoFilter extends Filter<EnumStatusNotificacao> {

        public EnumStatusNotificacaoFilter() {}

        public EnumStatusNotificacaoFilter(EnumStatusNotificacaoFilter filter) {
            super(filter);
        }

        @Override
        public EnumStatusNotificacaoFilter copy() {
            return new EnumStatusNotificacaoFilter(this);
        }
    }

    /**
     * Class for filtering EnumCanalNotificacao
     */
    public static class EnumCanalNotificacaoFilter extends Filter<EnumCanalNotificacao> {

        public EnumCanalNotificacaoFilter() {}

        public EnumCanalNotificacaoFilter(EnumCanalNotificacaoFilter filter) {
            super(filter);
        }

        @Override
        public EnumCanalNotificacaoFilter copy() {
            return new EnumCanalNotificacaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titulo;

    private StringFilter mensagem;

    private EnumTipoNotificacaoFilter tipo;

    private EnumStatusNotificacaoFilter situacao;

    private EnumCanalNotificacaoFilter canal;

    private BooleanFilter lida;

    private LongFilter usuarioId;

    private Boolean distinct;

    public NotificacaoCriteria() {}

    public NotificacaoCriteria(NotificacaoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titulo = other.optionalTitulo().map(StringFilter::copy).orElse(null);
        this.mensagem = other.optionalMensagem().map(StringFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(EnumTipoNotificacaoFilter::copy).orElse(null);
        this.situacao = other.optionalSituacao().map(EnumStatusNotificacaoFilter::copy).orElse(null);
        this.canal = other.optionalCanal().map(EnumCanalNotificacaoFilter::copy).orElse(null);
        this.lida = other.optionalLida().map(BooleanFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificacaoCriteria copy() {
        return new NotificacaoCriteria(this);
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

    public StringFilter getTitulo() {
        return titulo;
    }

    public Optional<StringFilter> optionalTitulo() {
        return Optional.ofNullable(titulo);
    }

    public StringFilter titulo() {
        if (titulo == null) {
            setTitulo(new StringFilter());
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public StringFilter getMensagem() {
        return mensagem;
    }

    public Optional<StringFilter> optionalMensagem() {
        return Optional.ofNullable(mensagem);
    }

    public StringFilter mensagem() {
        if (mensagem == null) {
            setMensagem(new StringFilter());
        }
        return mensagem;
    }

    public void setMensagem(StringFilter mensagem) {
        this.mensagem = mensagem;
    }

    public EnumTipoNotificacaoFilter getTipo() {
        return tipo;
    }

    public Optional<EnumTipoNotificacaoFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public EnumTipoNotificacaoFilter tipo() {
        if (tipo == null) {
            setTipo(new EnumTipoNotificacaoFilter());
        }
        return tipo;
    }

    public void setTipo(EnumTipoNotificacaoFilter tipo) {
        this.tipo = tipo;
    }

    public EnumStatusNotificacaoFilter getSituacao() {
        return situacao;
    }

    public Optional<EnumStatusNotificacaoFilter> optionalSituacao() {
        return Optional.ofNullable(situacao);
    }

    public EnumStatusNotificacaoFilter situacao() {
        if (situacao == null) {
            setSituacao(new EnumStatusNotificacaoFilter());
        }
        return situacao;
    }

    public void setSituacao(EnumStatusNotificacaoFilter situacao) {
        this.situacao = situacao;
    }

    public EnumCanalNotificacaoFilter getCanal() {
        return canal;
    }

    public Optional<EnumCanalNotificacaoFilter> optionalCanal() {
        return Optional.ofNullable(canal);
    }

    public EnumCanalNotificacaoFilter canal() {
        if (canal == null) {
            setCanal(new EnumCanalNotificacaoFilter());
        }
        return canal;
    }

    public void setCanal(EnumCanalNotificacaoFilter canal) {
        this.canal = canal;
    }

    public BooleanFilter getLida() {
        return lida;
    }

    public Optional<BooleanFilter> optionalLida() {
        return Optional.ofNullable(lida);
    }

    public BooleanFilter lida() {
        if (lida == null) {
            setLida(new BooleanFilter());
        }
        return lida;
    }

    public void setLida(BooleanFilter lida) {
        this.lida = lida;
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
        final NotificacaoCriteria that = (NotificacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(mensagem, that.mensagem) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(canal, that.canal) &&
            Objects.equals(lida, that.lida) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, mensagem, tipo, situacao, canal, lida, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacaoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitulo().map(f -> "titulo=" + f + ", ").orElse("") +
            optionalMensagem().map(f -> "mensagem=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalSituacao().map(f -> "situacao=" + f + ", ").orElse("") +
            optionalCanal().map(f -> "canal=" + f + ", ").orElse("") +
            optionalLida().map(f -> "lida=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
