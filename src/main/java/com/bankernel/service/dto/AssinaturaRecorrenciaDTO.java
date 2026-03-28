package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumStatusAssinatura;
import com.bankernel.domain.enumeration.EnumTipoAssinatura;
import com.bankernel.domain.enumeration.EnumTipoPagamentoAssinatura;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.AssinaturaRecorrencia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssinaturaRecorrenciaDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String numeroOrdem;

    @Size(max = 50)
    private String numeroReferencia;

    @Size(max = 200)
    private String devedorEmail;

    @Size(max = 14)
    private String devedorDocumento;

    @Size(max = 200)
    private String devedorNome;

    @Size(max = 20)
    private String devedorTelefone;

    @Size(max = 8)
    private String devedorCep;

    @Size(max = 2)
    private String devedorUf;

    @Size(max = 100)
    private String devedorCidade;

    @Size(max = 100)
    private String devedorBairro;

    @Size(max = 200)
    private String devedorEndereco;

    @NotNull
    private Boolean ativo;

    @Size(max = 100)
    private String idProdutoExterno;

    @Size(max = 100)
    private String identificadorExterno;

    @Size(max = 200)
    private String nomeProdutoExterno;

    @NotNull
    private EnumTipoAssinatura tipo;

    @NotNull
    private EnumTipoPagamentoAssinatura tipoPagamento;

    @NotNull
    private EnumStatusAssinatura situacao;

    @NotNull
    private PlanoRecorrenciaDTO planoRecorrencia;

    @NotNull
    private UserDTO usuario;

    private LinkCobrancaDTO linkCobranca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrdem() {
        return numeroOrdem;
    }

    public void setNumeroOrdem(String numeroOrdem) {
        this.numeroOrdem = numeroOrdem;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getDevedorEmail() {
        return devedorEmail;
    }

    public void setDevedorEmail(String devedorEmail) {
        this.devedorEmail = devedorEmail;
    }

    public String getDevedorDocumento() {
        return devedorDocumento;
    }

    public void setDevedorDocumento(String devedorDocumento) {
        this.devedorDocumento = devedorDocumento;
    }

    public String getDevedorNome() {
        return devedorNome;
    }

    public void setDevedorNome(String devedorNome) {
        this.devedorNome = devedorNome;
    }

    public String getDevedorTelefone() {
        return devedorTelefone;
    }

    public void setDevedorTelefone(String devedorTelefone) {
        this.devedorTelefone = devedorTelefone;
    }

    public String getDevedorCep() {
        return devedorCep;
    }

    public void setDevedorCep(String devedorCep) {
        this.devedorCep = devedorCep;
    }

    public String getDevedorUf() {
        return devedorUf;
    }

    public void setDevedorUf(String devedorUf) {
        this.devedorUf = devedorUf;
    }

    public String getDevedorCidade() {
        return devedorCidade;
    }

    public void setDevedorCidade(String devedorCidade) {
        this.devedorCidade = devedorCidade;
    }

    public String getDevedorBairro() {
        return devedorBairro;
    }

    public void setDevedorBairro(String devedorBairro) {
        this.devedorBairro = devedorBairro;
    }

    public String getDevedorEndereco() {
        return devedorEndereco;
    }

    public void setDevedorEndereco(String devedorEndereco) {
        this.devedorEndereco = devedorEndereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getIdProdutoExterno() {
        return idProdutoExterno;
    }

    public void setIdProdutoExterno(String idProdutoExterno) {
        this.idProdutoExterno = idProdutoExterno;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getNomeProdutoExterno() {
        return nomeProdutoExterno;
    }

    public void setNomeProdutoExterno(String nomeProdutoExterno) {
        this.nomeProdutoExterno = nomeProdutoExterno;
    }

    public EnumTipoAssinatura getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoAssinatura tipo) {
        this.tipo = tipo;
    }

    public EnumTipoPagamentoAssinatura getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(EnumTipoPagamentoAssinatura tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public EnumStatusAssinatura getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumStatusAssinatura situacao) {
        this.situacao = situacao;
    }

    public PlanoRecorrenciaDTO getPlanoRecorrencia() {
        return planoRecorrencia;
    }

    public void setPlanoRecorrencia(PlanoRecorrenciaDTO planoRecorrencia) {
        this.planoRecorrencia = planoRecorrencia;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    public LinkCobrancaDTO getLinkCobranca() {
        return linkCobranca;
    }

    public void setLinkCobranca(LinkCobrancaDTO linkCobranca) {
        this.linkCobranca = linkCobranca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssinaturaRecorrenciaDTO)) {
            return false;
        }

        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = (AssinaturaRecorrenciaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assinaturaRecorrenciaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssinaturaRecorrenciaDTO{" +
            "id=" + getId() +
            ", numeroOrdem='" + getNumeroOrdem() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", devedorEmail='" + getDevedorEmail() + "'" +
            ", devedorDocumento='" + getDevedorDocumento() + "'" +
            ", devedorNome='" + getDevedorNome() + "'" +
            ", devedorTelefone='" + getDevedorTelefone() + "'" +
            ", devedorCep='" + getDevedorCep() + "'" +
            ", devedorUf='" + getDevedorUf() + "'" +
            ", devedorCidade='" + getDevedorCidade() + "'" +
            ", devedorBairro='" + getDevedorBairro() + "'" +
            ", devedorEndereco='" + getDevedorEndereco() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", idProdutoExterno='" + getIdProdutoExterno() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", nomeProdutoExterno='" + getNomeProdutoExterno() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", tipoPagamento='" + getTipoPagamento() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", planoRecorrencia=" + getPlanoRecorrencia() +
            ", usuario=" + getUsuario() +
            ", linkCobranca=" + getLinkCobranca() +
            "}";
    }
}
