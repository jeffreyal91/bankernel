package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import com.bankernel.domain.enumeration.EnumTipoDocumentoPJ;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.PessoaJuridica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PessoaJuridicaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 14)
    private String cnpj;

    @NotNull
    @Size(max = 200)
    private String razaoSocial;

    @Size(max = 200)
    private String nomeFantasia;

    @NotNull
    @Size(max = 20)
    private String telefone;

    @Size(max = 500)
    private String sitioWeb;

    @Size(max = 2000)
    private String descricao;

    private LocalDate dataFundacao;

    private BigDecimal capitalSocial;

    private BigDecimal faturamentoAnual;

    private BigDecimal mediaMovimentacaoMensal;

    private EnumTipoDocumentoPJ tipoDocumento;

    @Size(max = 100)
    private String regimeTributario;

    @Size(max = 20)
    private String codigoNaturezaJuridica;

    @Size(max = 200)
    private String atividadePrincipal;

    @NotNull
    private Boolean empresaAtiva;

    @NotNull
    private EnumNivelRisco nivelRisco;

    @NotNull
    private EnumStatusPessoa situacao;

    @NotNull
    private Boolean bloqueioSaque;

    @Size(max = 11)
    private String cpfRepresentanteLegal;

    @Size(max = 50)
    private String numeroRegistro;

    @NotNull
    private UserDTO usuario;

    private MoedaCarteiraDTO moedaPrincipal;

    private DocumentoDTO contratoSocial;

    private PaisDTO nacionalidade;

    private TipoNegocioDTO tipoNegocio;

    private PlanoDTO plano;

    private EscritorioDTO escritorio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(LocalDate dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public BigDecimal getCapitalSocial() {
        return capitalSocial;
    }

    public void setCapitalSocial(BigDecimal capitalSocial) {
        this.capitalSocial = capitalSocial;
    }

    public BigDecimal getFaturamentoAnual() {
        return faturamentoAnual;
    }

    public void setFaturamentoAnual(BigDecimal faturamentoAnual) {
        this.faturamentoAnual = faturamentoAnual;
    }

    public BigDecimal getMediaMovimentacaoMensal() {
        return mediaMovimentacaoMensal;
    }

    public void setMediaMovimentacaoMensal(BigDecimal mediaMovimentacaoMensal) {
        this.mediaMovimentacaoMensal = mediaMovimentacaoMensal;
    }

    public EnumTipoDocumentoPJ getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(EnumTipoDocumentoPJ tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(String regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public String getCodigoNaturezaJuridica() {
        return codigoNaturezaJuridica;
    }

    public void setCodigoNaturezaJuridica(String codigoNaturezaJuridica) {
        this.codigoNaturezaJuridica = codigoNaturezaJuridica;
    }

    public String getAtividadePrincipal() {
        return atividadePrincipal;
    }

    public void setAtividadePrincipal(String atividadePrincipal) {
        this.atividadePrincipal = atividadePrincipal;
    }

    public Boolean getEmpresaAtiva() {
        return empresaAtiva;
    }

    public void setEmpresaAtiva(Boolean empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
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

    public String getCpfRepresentanteLegal() {
        return cpfRepresentanteLegal;
    }

    public void setCpfRepresentanteLegal(String cpfRepresentanteLegal) {
        this.cpfRepresentanteLegal = cpfRepresentanteLegal;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
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

    public DocumentoDTO getContratoSocial() {
        return contratoSocial;
    }

    public void setContratoSocial(DocumentoDTO contratoSocial) {
        this.contratoSocial = contratoSocial;
    }

    public PaisDTO getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(PaisDTO nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public TipoNegocioDTO getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(TipoNegocioDTO tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
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
        if (!(o instanceof PessoaJuridicaDTO)) {
            return false;
        }

        PessoaJuridicaDTO pessoaJuridicaDTO = (PessoaJuridicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pessoaJuridicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaJuridicaDTO{" +
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
            ", usuario=" + getUsuario() +
            ", moedaPrincipal=" + getMoedaPrincipal() +
            ", contratoSocial=" + getContratoSocial() +
            ", nacionalidade=" + getNacionalidade() +
            ", tipoNegocio=" + getTipoNegocio() +
            ", plano=" + getPlano() +
            ", escritorio=" + getEscritorio() +
            "}";
    }
}
