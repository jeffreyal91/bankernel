package com.bankernel.domain;

import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import com.bankernel.domain.enumeration.EnumTipoDocumentoPJ;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PessoaJuridica.
 */
@Entity
@Table(name = "pes_pessoa_juridica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PessoaJuridica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 14)
    @Column(name = "cnpj", length = 14, nullable = false, unique = true)
    private String cnpj;

    @NotNull
    @Size(max = 200)
    @Column(name = "razao_social", length = 200, nullable = false)
    private String razaoSocial;

    @Size(max = 200)
    @Column(name = "nome_fantasia", length = 200)
    private String nomeFantasia;

    @NotNull
    @Size(max = 20)
    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @Size(max = 500)
    @Column(name = "sitio_web", length = 500)
    private String sitioWeb;

    @Size(max = 2000)
    @Column(name = "descricao", length = 2000)
    private String descricao;

    @Column(name = "data_fundacao")
    private LocalDate dataFundacao;

    @Column(name = "capital_social", precision = 21, scale = 2)
    private BigDecimal capitalSocial;

    @Column(name = "faturamento_anual", precision = 21, scale = 2)
    private BigDecimal faturamentoAnual;

    @Column(name = "media_movimentacao_mensal", precision = 21, scale = 2)
    private BigDecimal mediaMovimentacaoMensal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private EnumTipoDocumentoPJ tipoDocumento;

    @Size(max = 100)
    @Column(name = "regime_tributario", length = 100)
    private String regimeTributario;

    @Size(max = 20)
    @Column(name = "codigo_natureza_juridica", length = 20)
    private String codigoNaturezaJuridica;

    @Size(max = 200)
    @Column(name = "atividade_principal", length = 200)
    private String atividadePrincipal;

    @NotNull
    @Column(name = "empresa_ativa", nullable = false)
    private Boolean empresaAtiva;

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

    @Size(max = 11)
    @Column(name = "cpf_representante_legal", length = 11)
    private String cpfRepresentanteLegal;

    @Size(max = 50)
    @Column(name = "numero_registro", length = 50)
    private String numeroRegistro;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User usuario;

    @JsonIgnoreProperties(value = { "moeda", "pessoaFisica", "pessoaJuridica" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MoedaCarteira moedaPrincipal;

    @JsonIgnoreProperties(value = { "pessoaJuridica" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Documento contratoSocial;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pais nacionalidade;

    @ManyToOne(fetch = FetchType.LAZY)
    private TipoNegocio tipoNegocio;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plano plano;

    @ManyToOne(fetch = FetchType.LAZY)
    private Escritorio escritorio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PessoaJuridica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public PessoaJuridica cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return this.razaoSocial;
    }

    public PessoaJuridica razaoSocial(String razaoSocial) {
        this.setRazaoSocial(razaoSocial);
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return this.nomeFantasia;
    }

    public PessoaJuridica nomeFantasia(String nomeFantasia) {
        this.setNomeFantasia(nomeFantasia);
        return this;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public PessoaJuridica telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSitioWeb() {
        return this.sitioWeb;
    }

    public PessoaJuridica sitioWeb(String sitioWeb) {
        this.setSitioWeb(sitioWeb);
        return this;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public PessoaJuridica descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataFundacao() {
        return this.dataFundacao;
    }

    public PessoaJuridica dataFundacao(LocalDate dataFundacao) {
        this.setDataFundacao(dataFundacao);
        return this;
    }

    public void setDataFundacao(LocalDate dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public BigDecimal getCapitalSocial() {
        return this.capitalSocial;
    }

    public PessoaJuridica capitalSocial(BigDecimal capitalSocial) {
        this.setCapitalSocial(capitalSocial);
        return this;
    }

    public void setCapitalSocial(BigDecimal capitalSocial) {
        this.capitalSocial = capitalSocial;
    }

    public BigDecimal getFaturamentoAnual() {
        return this.faturamentoAnual;
    }

    public PessoaJuridica faturamentoAnual(BigDecimal faturamentoAnual) {
        this.setFaturamentoAnual(faturamentoAnual);
        return this;
    }

    public void setFaturamentoAnual(BigDecimal faturamentoAnual) {
        this.faturamentoAnual = faturamentoAnual;
    }

    public BigDecimal getMediaMovimentacaoMensal() {
        return this.mediaMovimentacaoMensal;
    }

    public PessoaJuridica mediaMovimentacaoMensal(BigDecimal mediaMovimentacaoMensal) {
        this.setMediaMovimentacaoMensal(mediaMovimentacaoMensal);
        return this;
    }

    public void setMediaMovimentacaoMensal(BigDecimal mediaMovimentacaoMensal) {
        this.mediaMovimentacaoMensal = mediaMovimentacaoMensal;
    }

    public EnumTipoDocumentoPJ getTipoDocumento() {
        return this.tipoDocumento;
    }

    public PessoaJuridica tipoDocumento(EnumTipoDocumentoPJ tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(EnumTipoDocumentoPJ tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRegimeTributario() {
        return this.regimeTributario;
    }

    public PessoaJuridica regimeTributario(String regimeTributario) {
        this.setRegimeTributario(regimeTributario);
        return this;
    }

    public void setRegimeTributario(String regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public String getCodigoNaturezaJuridica() {
        return this.codigoNaturezaJuridica;
    }

    public PessoaJuridica codigoNaturezaJuridica(String codigoNaturezaJuridica) {
        this.setCodigoNaturezaJuridica(codigoNaturezaJuridica);
        return this;
    }

    public void setCodigoNaturezaJuridica(String codigoNaturezaJuridica) {
        this.codigoNaturezaJuridica = codigoNaturezaJuridica;
    }

    public String getAtividadePrincipal() {
        return this.atividadePrincipal;
    }

    public PessoaJuridica atividadePrincipal(String atividadePrincipal) {
        this.setAtividadePrincipal(atividadePrincipal);
        return this;
    }

    public void setAtividadePrincipal(String atividadePrincipal) {
        this.atividadePrincipal = atividadePrincipal;
    }

    public Boolean getEmpresaAtiva() {
        return this.empresaAtiva;
    }

    public PessoaJuridica empresaAtiva(Boolean empresaAtiva) {
        this.setEmpresaAtiva(empresaAtiva);
        return this;
    }

    public void setEmpresaAtiva(Boolean empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
    }

    public EnumNivelRisco getNivelRisco() {
        return this.nivelRisco;
    }

    public PessoaJuridica nivelRisco(EnumNivelRisco nivelRisco) {
        this.setNivelRisco(nivelRisco);
        return this;
    }

    public void setNivelRisco(EnumNivelRisco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public EnumStatusPessoa getSituacao() {
        return this.situacao;
    }

    public PessoaJuridica situacao(EnumStatusPessoa situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(EnumStatusPessoa situacao) {
        this.situacao = situacao;
    }

    public Boolean getBloqueioSaque() {
        return this.bloqueioSaque;
    }

    public PessoaJuridica bloqueioSaque(Boolean bloqueioSaque) {
        this.setBloqueioSaque(bloqueioSaque);
        return this;
    }

    public void setBloqueioSaque(Boolean bloqueioSaque) {
        this.bloqueioSaque = bloqueioSaque;
    }

    public String getCpfRepresentanteLegal() {
        return this.cpfRepresentanteLegal;
    }

    public PessoaJuridica cpfRepresentanteLegal(String cpfRepresentanteLegal) {
        this.setCpfRepresentanteLegal(cpfRepresentanteLegal);
        return this;
    }

    public void setCpfRepresentanteLegal(String cpfRepresentanteLegal) {
        this.cpfRepresentanteLegal = cpfRepresentanteLegal;
    }

    public String getNumeroRegistro() {
        return this.numeroRegistro;
    }

    public PessoaJuridica numeroRegistro(String numeroRegistro) {
        this.setNumeroRegistro(numeroRegistro);
        return this;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public PessoaJuridica usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public MoedaCarteira getMoedaPrincipal() {
        return this.moedaPrincipal;
    }

    public void setMoedaPrincipal(MoedaCarteira moedaCarteira) {
        this.moedaPrincipal = moedaCarteira;
    }

    public PessoaJuridica moedaPrincipal(MoedaCarteira moedaCarteira) {
        this.setMoedaPrincipal(moedaCarteira);
        return this;
    }

    public Documento getContratoSocial() {
        return this.contratoSocial;
    }

    public void setContratoSocial(Documento documento) {
        this.contratoSocial = documento;
    }

    public PessoaJuridica contratoSocial(Documento documento) {
        this.setContratoSocial(documento);
        return this;
    }

    public Pais getNacionalidade() {
        return this.nacionalidade;
    }

    public void setNacionalidade(Pais pais) {
        this.nacionalidade = pais;
    }

    public PessoaJuridica nacionalidade(Pais pais) {
        this.setNacionalidade(pais);
        return this;
    }

    public TipoNegocio getTipoNegocio() {
        return this.tipoNegocio;
    }

    public void setTipoNegocio(TipoNegocio tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public PessoaJuridica tipoNegocio(TipoNegocio tipoNegocio) {
        this.setTipoNegocio(tipoNegocio);
        return this;
    }

    public Plano getPlano() {
        return this.plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public PessoaJuridica plano(Plano plano) {
        this.setPlano(plano);
        return this;
    }

    public Escritorio getEscritorio() {
        return this.escritorio;
    }

    public void setEscritorio(Escritorio escritorio) {
        this.escritorio = escritorio;
    }

    public PessoaJuridica escritorio(Escritorio escritorio) {
        this.setEscritorio(escritorio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PessoaJuridica)) {
            return false;
        }
        return getId() != null && getId().equals(((PessoaJuridica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaJuridica{" +
            "id=" + getId() +
            ", cnpj='" + getCnpj() + "'" +
            ", razaoSocial='" + getRazaoSocial() + "'" +
            ", nomeFantasia='" + getNomeFantasia() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", sitioWeb='" + getSitioWeb() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataFundacao='" + getDataFundacao() + "'" +
            ", capitalSocial=" + getCapitalSocial() +
            ", faturamentoAnual=" + getFaturamentoAnual() +
            ", mediaMovimentacaoMensal=" + getMediaMovimentacaoMensal() +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", regimeTributario='" + getRegimeTributario() + "'" +
            ", codigoNaturezaJuridica='" + getCodigoNaturezaJuridica() + "'" +
            ", atividadePrincipal='" + getAtividadePrincipal() + "'" +
            ", empresaAtiva='" + getEmpresaAtiva() + "'" +
            ", nivelRisco='" + getNivelRisco() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", bloqueioSaque='" + getBloqueioSaque() + "'" +
            ", cpfRepresentanteLegal='" + getCpfRepresentanteLegal() + "'" +
            ", numeroRegistro='" + getNumeroRegistro() + "'" +
            "}";
    }
}
