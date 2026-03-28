package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumCanalNotificacao;
import com.bankernel.domain.enumeration.EnumStatusNotificacao;
import com.bankernel.domain.enumeration.EnumTipoNotificacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notificacao.
 */
@Entity
@Table(name = "ntf_notificacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notificacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "titulo", length = 200, nullable = false)
    private String titulo;

    @NotNull
    @Size(max = 2000)
    @Column(name = "mensagem", length = 2000, nullable = false)
    private String mensagem;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoNotificacao tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusNotificacao situacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false)
    private EnumCanalNotificacao canal;

    @NotNull
    @Column(name = "lida", nullable = false)
    private Boolean lida;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notificacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Notificacao titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public Notificacao mensagem(String mensagem) {
        this.setMensagem(mensagem);
        return this;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public EnumTipoNotificacao getTipo() {
        return this.tipo;
    }

    public Notificacao tipo(EnumTipoNotificacao tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public EnumStatusNotificacao getSituacao() {
        return this.situacao;
    }

    public Notificacao situacao(EnumStatusNotificacao situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusNotificacao situacao) {
        this.situacao = situacao;
    }

    public EnumCanalNotificacao getCanal() {
        return this.canal;
    }

    public Notificacao canal(EnumCanalNotificacao canal) {
        this.setCanal(canal);
        return this;
    }

    public void setCanal(EnumCanalNotificacao canal) {
        this.canal = canal;
    }

    public Boolean getLida() {
        return this.lida;
    }

    public Notificacao lida(Boolean lida) {
        this.setLida(lida);
        return this;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Notificacao usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Notificacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notificacao{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", mensagem='" + getMensagem() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", canal='" + getCanal() + "'" +
            ", lida='" + getLida() + "'" +
            "}";
    }
}
