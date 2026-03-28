package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumTipoIntegracao;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.RegistroIntegracao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistroIntegracaoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String fornecedor;

    @NotNull
    private EnumTipoIntegracao tipoIntegracao;

    @NotNull
    @Size(max = 50)
    private String operacao;

    @Lob
    private String corpoRequisicao;

    @Lob
    private String corpoResposta;

    private Integer codigoHttp;

    @NotNull
    private Boolean sucesso;

    @Size(max = 1000)
    private String mensagemErro;

    private Long duracaoMilissegundos;

    @Size(max = 50)
    private String tipoEntidadeOrigem;

    private Long idEntidadeOrigem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public EnumTipoIntegracao getTipoIntegracao() {
        return tipoIntegracao;
    }

    public void setTipoIntegracao(EnumTipoIntegracao tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getCorpoRequisicao() {
        return corpoRequisicao;
    }

    public void setCorpoRequisicao(String corpoRequisicao) {
        this.corpoRequisicao = corpoRequisicao;
    }

    public String getCorpoResposta() {
        return corpoResposta;
    }

    public void setCorpoResposta(String corpoResposta) {
        this.corpoResposta = corpoResposta;
    }

    public Integer getCodigoHttp() {
        return codigoHttp;
    }

    public void setCodigoHttp(Integer codigoHttp) {
        this.codigoHttp = codigoHttp;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public Long getDuracaoMilissegundos() {
        return duracaoMilissegundos;
    }

    public void setDuracaoMilissegundos(Long duracaoMilissegundos) {
        this.duracaoMilissegundos = duracaoMilissegundos;
    }

    public String getTipoEntidadeOrigem() {
        return tipoEntidadeOrigem;
    }

    public void setTipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public Long getIdEntidadeOrigem() {
        return idEntidadeOrigem;
    }

    public void setIdEntidadeOrigem(Long idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroIntegracaoDTO)) {
            return false;
        }

        RegistroIntegracaoDTO registroIntegracaoDTO = (RegistroIntegracaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, registroIntegracaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroIntegracaoDTO{" +
            "id=" + getId() +
            ", fornecedor='" + getFornecedor() + "'" +
            ", tipoIntegracao='" + getTipoIntegracao() + "'" +
            ", operacao='" + getOperacao() + "'" +
            ", corpoRequisicao='" + getCorpoRequisicao() + "'" +
            ", corpoResposta='" + getCorpoResposta() + "'" +
            ", codigoHttp=" + getCodigoHttp() +
            ", sucesso='" + getSucesso() + "'" +
            ", mensagemErro='" + getMensagemErro() + "'" +
            ", duracaoMilissegundos=" + getDuracaoMilissegundos() +
            ", tipoEntidadeOrigem='" + getTipoEntidadeOrigem() + "'" +
            ", idEntidadeOrigem=" + getIdEntidadeOrigem() +
            "}";
    }
}
