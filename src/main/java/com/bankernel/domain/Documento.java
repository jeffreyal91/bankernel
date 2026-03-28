package com.bankernel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Documento.
 */
@Entity
@Table(name = "ref_documento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @Size(max = 50)
    @Column(name = "tipo_arquivo", length = 50)
    private String tipoArquivo;

    @Size(max = 500)
    @Column(name = "endereco", length = 500)
    private String endereco;

    @Column(name = "tamanho")
    private Long tamanho;

    @JsonIgnoreProperties(
        value = { "usuario", "moedaPrincipal", "contratoSocial", "nacionalidade", "tipoNegocio", "plano", "escritorio" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contratoSocial")
    private PessoaJuridica pessoaJuridica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Documento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Documento nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoArquivo() {
        return this.tipoArquivo;
    }

    public Documento tipoArquivo(String tipoArquivo) {
        this.setTipoArquivo(tipoArquivo);
        return this;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Documento endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getTamanho() {
        return this.tamanho;
    }

    public Documento tamanho(Long tamanho) {
        this.setTamanho(tamanho);
        return this;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public PessoaJuridica getPessoaJuridica() {
        return this.pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        if (this.pessoaJuridica != null) {
            this.pessoaJuridica.setContratoSocial(null);
        }
        if (pessoaJuridica != null) {
            pessoaJuridica.setContratoSocial(this);
        }
        this.pessoaJuridica = pessoaJuridica;
    }

    public Documento pessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.setPessoaJuridica(pessoaJuridica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Documento)) {
            return false;
        }
        return getId() != null && getId().equals(((Documento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Documento{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipoArquivo='" + getTipoArquivo() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", tamanho=" + getTamanho() +
            "}";
    }
}
