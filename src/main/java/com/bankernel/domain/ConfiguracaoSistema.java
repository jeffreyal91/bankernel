package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumTipoConfiguracao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConfiguracaoSistema.
 */
@Entity
@Table(name = "conf_configuracao_sistema")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracaoSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "chave", length = 100, nullable = false, unique = true)
    private String chave;

    @NotNull
    @Size(max = 500)
    @Column(name = "valor", length = 500, nullable = false)
    private String valor;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoConfiguracao tipo;

    @NotNull
    @Size(max = 50)
    @Column(name = "modulo", length = 50, nullable = false)
    private String modulo;

    @NotNull
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConfiguracaoSistema id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return this.chave;
    }

    public ConfiguracaoSistema chave(String chave) {
        this.setChave(chave);
        return this;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return this.valor;
    }

    public ConfiguracaoSistema valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ConfiguracaoSistema descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumTipoConfiguracao getTipo() {
        return this.tipo;
    }

    public ConfiguracaoSistema tipo(EnumTipoConfiguracao tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(EnumTipoConfiguracao tipo) {
        this.tipo = tipo;
    }

    public String getModulo() {
        return this.modulo;
    }

    public ConfiguracaoSistema modulo(String modulo) {
        this.setModulo(modulo);
        return this;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Boolean getAtiva() {
        return this.ativa;
    }

    public ConfiguracaoSistema ativa(Boolean ativa) {
        this.setAtiva(ativa);
        return this;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfiguracaoSistema)) {
            return false;
        }
        return getId() != null && getId().equals(((ConfiguracaoSistema) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfiguracaoSistema{" +
            "id=" + getId() +
            ", chave='" + getChave() + "'" +
            ", valor='" + getValor() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", modulo='" + getModulo() + "'" +
            ", ativa='" + getAtiva() + "'" +
            "}";
    }
}
