package com.soc.network.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class DatasourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.master")
    fun masterDatasourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties("spring.datasource.replica")
    fun replicaDatasourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    fun masterDatasource(): DataSource {
        return masterDatasourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }

    @Bean
    fun replicaDatasource(): DataSource {
        return replicaDatasourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }

    @Bean
    @Primary
    fun jdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(masterDatasource())
    }

    @Bean
    fun roJdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(replicaDatasource())
    }
}