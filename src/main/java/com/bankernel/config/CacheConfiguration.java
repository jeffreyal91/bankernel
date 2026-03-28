package com.bankernel.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.bankernel.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.bankernel.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.bankernel.domain.User.class.getName());
            createCache(cm, com.bankernel.domain.Authority.class.getName());
            createCache(cm, com.bankernel.domain.User.class.getName() + ".authorities");
            createCache(cm, com.bankernel.domain.Pais.class.getName());
            createCache(cm, com.bankernel.domain.Profissao.class.getName());
            createCache(cm, com.bankernel.domain.TipoNegocio.class.getName());
            createCache(cm, com.bankernel.domain.BancoReferencia.class.getName());
            createCache(cm, com.bankernel.domain.Documento.class.getName());
            createCache(cm, com.bankernel.domain.PessoaFisica.class.getName());
            createCache(cm, com.bankernel.domain.PessoaJuridica.class.getName());
            createCache(cm, com.bankernel.domain.Endereco.class.getName());
            createCache(cm, com.bankernel.domain.ColaboradorPJ.class.getName());
            createCache(cm, com.bankernel.domain.PermissaoColaborador.class.getName());
            createCache(cm, com.bankernel.domain.Moeda.class.getName());
            createCache(cm, com.bankernel.domain.MoedaCarteira.class.getName());
            createCache(cm, com.bankernel.domain.Carteira.class.getName());
            createCache(cm, com.bankernel.domain.TipoOperacao.class.getName());
            createCache(cm, com.bankernel.domain.Transacao.class.getName());
            createCache(cm, com.bankernel.domain.HistoricoOperacao.class.getName());
            createCache(cm, com.bankernel.domain.Deposito.class.getName());
            createCache(cm, com.bankernel.domain.DepositoPix.class.getName());
            createCache(cm, com.bankernel.domain.DepositoBoleto.class.getName());
            createCache(cm, com.bankernel.domain.Saque.class.getName());
            createCache(cm, com.bankernel.domain.SaquePix.class.getName());
            createCache(cm, com.bankernel.domain.SaqueBoleto.class.getName());
            createCache(cm, com.bankernel.domain.ContaBancaria.class.getName());
            createCache(cm, com.bankernel.domain.Transferencia.class.getName());
            createCache(cm, com.bankernel.domain.LinkCobranca.class.getName());
            createCache(cm, com.bankernel.domain.Cobranca.class.getName());
            createCache(cm, com.bankernel.domain.PlanoRecorrencia.class.getName());
            createCache(cm, com.bankernel.domain.AssinaturaRecorrencia.class.getName());
            createCache(cm, com.bankernel.domain.ContaContabil.class.getName());
            createCache(cm, com.bankernel.domain.LancamentoContabil.class.getName());
            createCache(cm, com.bankernel.domain.Plano.class.getName());
            createCache(cm, com.bankernel.domain.ConfiguracaoSistema.class.getName());
            createCache(cm, com.bankernel.domain.BloqueioOperacao.class.getName());
            createCache(cm, com.bankernel.domain.Feriado.class.getName());
            createCache(cm, com.bankernel.domain.Escritorio.class.getName());
            createCache(cm, com.bankernel.domain.Administrador.class.getName());
            createCache(cm, com.bankernel.domain.Notificacao.class.getName());
            createCache(cm, com.bankernel.domain.RegistroIntegracao.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
