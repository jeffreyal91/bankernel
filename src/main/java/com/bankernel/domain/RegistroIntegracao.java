package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumTipoIntegracao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RegistroIntegracao.
 */
@Entity
@Table(name = "itg_registro_integracao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistroIntegracao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "fornecedor", length = 50, nullable = false)
    private String fornecedor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_integracao", nullable = false)
    private EnumTipoIntegracao tipoIntegracao;

    @NotNull
    @Size(max = 50)
    @Column(name = "operacao", length = 50, nullable = false)
    private String operacao;

    @Lob
    @Column(name = "corpo_requisicao")
    private String corpoRequisicao;

    @Lob
    @Column(name = "corpo_resposta")
    private String corpoResposta;

    @Column(name = "codigo_http")
    private Integer codigoHttp;

    @NotNull
    @Column(name = "sucesso", nullable = false)
    private Boolean sucesso;

    @Size(max = 1000)
    @Column(name = "mensagem_erro", length = 1000)
    private String mensagemErro;

    @Column(name = "duracao_milissegundos")
    private Long duracaoMilissegundos;

    @Size(max = 50)
    @Column(name = "tipo_entidade_origem", length = 50)
    private String tipoEntidadeOrigem;

    @Column(name = "id_entidade_origem")
    private Long idEntidadeOrigem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RegistroIntegracao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFornecedor() {
        return this.fornecedor;
    }

    public RegistroIntegracao fornecedor(String fornecedor) {
        this.setFornecedor(fornecedor);
        return this;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public EnumTipoIntegracao getTipoIntegracao() {
        return this.tipoIntegracao;
    }

    public RegistroIntegracao tipoIntegracao(EnumTipoIntegracao tipoIntegracao) {
        this.setTipoIntegracao(tipoIntegracao);
        return this;
    }

    public void setTipoIntegracao(EnumTipoIntegracao tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
    }

    public String getOperacao() {
        return this.operacao;
    }

    public RegistroIntegracao operacao(String operacao) {
        this.setOperacao(operacao);
        return this;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getCorpoRequisicao() {
        return this.corpoRequisicao;
    }

    public RegistroIntegracao corpoRequisicao(String corpoRequisicao) {
        this.setCorpoRequisicao(corpoRequisicao);
        return this;
    }

    public void setCorpoRequisicao(String corpoRequisicao) {
        this.corpoRequisicao = corpoRequisicao;
    }

    public String getCorpoResposta() {
        return this.corpoResposta;
    }

    public RegistroIntegracao corpoResposta(String corpoResposta) {
        this.setCorpoResposta(corpoResposta);
        return this;
    }

    public void setCorpoResposta(String corpoResposta) {
        this.corpoResposta = corpoResposta;
    }

    public Integer getCodigoHttp() {
        return this.codigoHttp;
    }

    public RegistroIntegracao codigoHttp(Integer codigoHttp) {
        this.setCodigoHttp(codigoHttp);
        return this;
    }

    public void setCodigoHttp(Integer codigoHttp) {
        this.codigoHttp = codigoHttp;
    }

    public Boolean getSucesso() {
        return this.sucesso;
    }

    public RegistroIntegracao sucesso(Boolean sucesso) {
        this.setSucesso(sucesso);
        return this;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagemErro() {
        return this.mensagemErro;
    }

    public RegistroIntegracao mensagemErro(String mensagemErro) {
        this.setMensagemErro(mensagemErro);
        return this;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public Long getDuracaoMilissegundos() {
        return this.duracaoMilissegundos;
    }

    public RegistroIntegracao duracaoMilissegundos(Long duracaoMilissegundos) {
        this.setDuracaoMilissegundos(duracaoMilissegundos);
        return this;
    }

    public void setDuracaoMilissegundos(Long duracaoMilissegundos) {
        this.duracaoMilissegundos = duracaoMilissegundos;
    }

    public String getTipoEntidadeOrigem() {
        return this.tipoEntidadeOrigem;
    }

    public RegistroIntegracao tipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.setTipoEntidadeOrigem(tipoEntidadeOrigem);
        return this;
    }

    public void setTipoEntidadeOrigem(String tipoEntidadeOrigem) {
        this.tipoEntidadeOrigem = tipoEntidadeOrigem;
    }

    public Long getIdEntidadeOrigem() {
        return this.idEntidadeOrigem;
    }

    public RegistroIntegracao idEntidadeOrigem(Long idEntidadeOrigem) {
        this.setIdEntidadeOrigem(idEntidadeOrigem);
        return this;
    }

    public void setIdEntidadeOrigem(Long idEntidadeOrigem) {
        this.idEntidadeOrigem = idEntidadeOrigem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroIntegracao)) {
            return false;
        }
        return getId() != null && getId().equals(((RegistroIntegracao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroIntegracao{" +
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
