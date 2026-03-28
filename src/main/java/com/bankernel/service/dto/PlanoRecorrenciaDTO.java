package com.bankernel.service.dto;

import com.bankernel.domain.enumeration.EnumMetodoPagamentoRecorrencia;
import com.bankernel.domain.enumeration.EnumTipoPlanoRecorrencia;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.PlanoRecorrencia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanoRecorrenciaDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String identificadorExterno;

    @Size(max = 50)
    private String numeroReferencia;

    @NotNull
    @Size(max = 200)
    private String nome;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private BigDecimal valor;

    private Integer diasTeste;

    @NotNull
    private Integer intervalo;

    private Integer parcelas;

    @NotNull
    private Integer tentativas;

    @NotNull
    private Boolean ativo;

    @NotNull
    private EnumMetodoPagamentoRecorrencia metodoPagamento;

    @NotNull
    private EnumTipoPlanoRecorrencia tipoPlano;

    @NotNull
    private UserDTO usuario;

    private LinkCobrancaDTO linkCobranca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDiasTeste() {
        return diasTeste;
    }

    public void setDiasTeste(Integer diasTeste) {
        this.diasTeste = diasTeste;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public Integer getTentativas() {
        return tentativas;
    }

    public void setTentativas(Integer tentativas) {
        this.tentativas = tentativas;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnumMetodoPagamentoRecorrencia getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(EnumMetodoPagamentoRecorrencia metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public EnumTipoPlanoRecorrencia getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(EnumTipoPlanoRecorrencia tipoPlano) {
        this.tipoPlano = tipoPlano;
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
        if (!(o instanceof PlanoRecorrenciaDTO)) {
            return false;
        }

        PlanoRecorrenciaDTO planoRecorrenciaDTO = (PlanoRecorrenciaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planoRecorrenciaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanoRecorrenciaDTO{" +
            "id=" + getId() +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", numeroReferencia='" + getNumeroReferencia() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", diasTeste=" + getDiasTeste() +
            ", intervalo=" + getIntervalo() +
            ", parcelas=" + getParcelas() +
            ", tentativas=" + getTentativas() +
            ", ativo='" + getAtivo() + "'" +
            ", metodoPagamento='" + getMetodoPagamento() + "'" +
            ", tipoPlano='" + getTipoPlano() + "'" +
            ", usuario=" + getUsuario() +
            ", linkCobranca=" + getLinkCobranca() +
            "}";
    }
}
