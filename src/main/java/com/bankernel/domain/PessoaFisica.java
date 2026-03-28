package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumGenero;
import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PessoaFisica.
 */
@Entity
@Table(name = "pes_pessoa_fisica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PessoaFisica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    private String cpf;

    @NotNull
    @Size(max = 200)
    @Column(name = "nome_completo", length = 200, nullable = false)
    private String nomeCompleto;

    @Size(max = 200)
    @Column(name = "nome_social", length = 200)
    private String nomeSocial;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private EnumGenero genero;

    @Size(max = 200)
    @Column(name = "nome_mae", length = 200)
    private String nomeMae;

    @NotNull
    @Size(max = 20)
    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @NotNull
    @Column(name = "telefone_verificado", nullable = false)
    private Boolean telefoneVerificado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_risco", nullable = false)
    private EnumNivelRisco nivelRisco;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EnumStatusPessoa situacao;

    @NotNull
    @Column(name = "bloqueio_saque", nullable = false)
    private Boolean bloqueioSaque;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User usuario;

    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MoedaCarteira moedaPrincipal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pais nacionalidade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profissao profissao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plano plano;

    @ManyToOne(fetch = FetchType.LAZY)
    private Escritorio escritorio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PessoaFisica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return this.cpf;
    }

    public PessoaFisica cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return this.nomeCompleto;
    }

    public PessoaFisica nomeCompleto(String nomeCompleto) {
        this.setNomeCompleto(nomeCompleto);
        return this;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeSocial() {
        return this.nomeSocial;
    }

    public PessoaFisica nomeSocial(String nomeSocial) {
        this.setNomeSocial(nomeSocial);
        return this;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public PessoaFisica dataNascimento(LocalDate dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EnumGenero getGenero() {
        return this.genero;
    }

    public PessoaFisica genero(EnumGenero genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(EnumGenero genero) {
        this.genero = genero;
    }

    public String getNomeMae() {
        return this.nomeMae;
    }

    public PessoaFisica nomeMae(String nomeMae) {
        this.setNomeMae(nomeMae);
        return this;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public PessoaFisica telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getTelefoneVerificado() {
        return this.telefoneVerificado;
    }

    public PessoaFisica telefoneVerificado(Boolean telefoneVerificado) {
        this.setTelefoneVerificado(telefoneVerificado);
        return this;
    }

    public void setTelefoneVerificado(Boolean telefoneVerificado) {
        this.telefoneVerificado = telefoneVerificado;
    }

    public EnumNivelRisco getNivelRisco() {
        return this.nivelRisco;
    }

    public PessoaFisica nivelRisco(EnumNivelRisco nivelRisco) {
        this.setNivelRisco(nivelRisco);
        return this;
    }

    public void setNivelRisco(EnumNivelRisco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public EnumStatusPessoa getSituacao() {
        return this.situacao;
    }

    public PessoaFisica situacao(EnumStatusPessoa situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusPessoa situacao) {
        this.situacao = situacao;
    }

    public Boolean getBloqueioSaque() {
        return this.bloqueioSaque;
    }

    public PessoaFisica bloqueioSaque(Boolean bloqueioSaque) {
        this.setBloqueioSaque(bloqueioSaque);
        return this;
    }

    public void setBloqueioSaque(Boolean bloqueioSaque) {
        this.bloqueioSaque = bloqueioSaque;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public PessoaFisica usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public MoedaCarteira getMoedaPrincipal() {
        return this.moedaPrincipal;
    }

    public void setMoedaPrincipal(MoedaCarteira moedaCarteira) {
        this.moedaPrincipal = moedaCarteira;
    }

    public PessoaFisica moedaPrincipal(MoedaCarteira moedaCarteira) {
        this.setMoedaPrincipal(moedaCarteira);
        return this;
    }

    public Pais getNacionalidade() {
        return this.nacionalidade;
    }

    public void setNacionalidade(Pais pais) {
        this.nacionalidade = pais;
    }

    public PessoaFisica nacionalidade(Pais pais) {
        this.setNacionalidade(pais);
        return this;
    }

    public Profissao getProfissao() {
        return this.profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public PessoaFisica profissao(Profissao profissao) {
        this.setProfissao(profissao);
        return this;
    }

    public Plano getPlano() {
        return this.plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public PessoaFisica plano(Plano plano) {
        this.setPlano(plano);
        return this;
    }

    public Escritorio getEscritorio() {
        return this.escritorio;
    }

    public void setEscritorio(Escritorio escritorio) {
        this.escritorio = escritorio;
    }

    public PessoaFisica escritorio(Escritorio escritorio) {
        this.setEscritorio(escritorio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PessoaFisica)) {
            return false;
        }
        return getId() != null && getId().equals(((PessoaFisica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaFisica{" +
            "id=" + getId() +
            ", cpf='" + getCpf() + "'" +
            ", nomeCompleto='" + getNomeCompleto() + "'" +
            ", nomeSocial='" + getNomeSocial() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", genero='" + getGenero() + "'" +
            ", nomeMae='" + getNomeMae() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", telefoneVerificado='" + getTelefoneVerificado() + "'" +
            ", nivelRisco='" + getNivelRisco() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", bloqueioSaque='" + getBloqueioSaque() + "'" +
            "}";
    }
}
