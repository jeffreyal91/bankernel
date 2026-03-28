package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumCanalNotificacao;
import com.bankernel.domain.enumeration.EnumStatusNotificacao;
import com.bankernel.domain.enumeration.EnumTipoNotificacao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Notificacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacaoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String titulo;

    @NotNull
    @Size(max = 2000)
    private String mensagem;

    @NotNull
    private EnumTipoNotificacao tipo;

    @NotNull
    private EnumStatusNotificacao situacao;

    @NotNull
    private EnumCanalNotificacao canal;

    @NotNull
    private Boolean lida;

    @NotNull
    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public EnumTipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public EnumStatusNotificacao getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusNotificacao situacao) {
        this.situacao = situacao;
    }

    public EnumCanalNotificacao getCanal() {
        return canal;
    }

    public void setCanal(EnumCanalNotificacao canal) {
        this.canal = canal;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificacaoDTO)) {
            return false;
        }

        NotificacaoDTO notificacaoDTO = (NotificacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacaoDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", mensagem='" + getMensagem() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", canal='" + getCanal() + "'" +
            ", lida='" + getLida() + "'" +
            ", usuario=" + getUsuario() +
            "}";
    }
}
