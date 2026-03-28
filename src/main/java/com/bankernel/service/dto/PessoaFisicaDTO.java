package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumGenero;
import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.PessoaFisica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PessoaFisicaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 11)
    private String cpf;

    @NotNull
    @Size(max = 200)
    private String nomeCompleto;

    @Size(max = 200)
    private String nomeSocial;

    @NotNull
    private LocalDate dataNascimento;

    private EnumGenero genero;

    @Size(max = 200)
    private String nomeMae;

    @NotNull
    @Size(max = 20)
    private String telefone;

    @NotNull
    private Boolean telefoneVerificado;

    @NotNull
    private EnumNivelRisco nivelRisco;

    @NotNull
    private EnumStatusPessoa situacao;

    @NotNull
    private Boolean bloqueioSaque;

    @NotNull
    private UserDTO usuario;

    private MoedaCarteiraDTO moedaPrincipal;

    private PaisDTO nacionalidade;

    private ProfissaoDTO profissao;

    private PlanoDTO plano;

    private EscritorioDTO escritorio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EnumGenero getGenero() {
        return genero;
    }

    public void setGenero(EnumGenero genero) {
        this.genero = genero;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getTelefoneVerificado() {
        return telefoneVerificado;
    }

    public void setTelefoneVerificado(Boolean telefoneVerificado) {
        this.telefoneVerificado = telefoneVerificado;
    }

    public EnumNivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(EnumNivelRisco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public EnumStatusPessoa getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusPessoa situacao) {
        this.situacao = situacao;
    }

    public Boolean getBloqueioSaque() {
        return bloqueioSaque;
    }

    public void setBloqueioSaque(Boolean bloqueioSaque) {
        this.bloqueioSaque = bloqueioSaque;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public MoedaCarteiraDTO getMoedaPrincipal() {
        return moedaPrincipal;
    }

    public void setMoedaPrincipal(MoedaCarteiraDTO moedaPrincipal) {
        this.moedaPrincipal = moedaPrincipal;
    }

    public PaisDTO getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(PaisDTO nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public ProfissaoDTO getProfissao() {
        return profissao;
    }

    public void setProfissao(ProfissaoDTO profissao) {
        this.profissao = profissao;
    }

    public PlanoDTO getPlano() {
        return plano;
    }

    public void setPlano(PlanoDTO plano) {
        this.plano = plano;
    }

    public EscritorioDTO getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(EscritorioDTO escritorio) {
        this.escritorio = escritorio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PessoaFisicaDTO)) {
            return false;
        }

        PessoaFisicaDTO pessoaFisicaDTO = (PessoaFisicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pessoaFisicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaFisicaDTO{" +
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
            ", usuario=" + getUsuario() +
            ", moedaPrincipal=" + getMoedaPrincipal() +
            ", nacionalidade=" + getNacionalidade() +
            ", profissao=" + getProfissao() +
            ", plano=" + getPlano() +
            ", escritorio=" + getEscritorio() +
            "}";
    }
}
