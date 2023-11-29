package com.inq.wishhair.wesharewishhair.global.config.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.zaxxer.hikari.HikariDataSource;

@Profile("dev")
@Configuration
public class DataSourceConfig {

	private static final String MASTER_DATASOURCE = "masterDataSource";
	private static final String SLAVE_DATASOURCE = "slaveDataSource";
	private static final String ROUTING_DATASOURCE = "slaveDataSource";

	@Bean(MASTER_DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.master.hikari")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create()
			.type(HikariDataSource.class)
			.build();
	}

	@Bean(SLAVE_DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
	public DataSource slaveDataSource() {
		return DataSourceBuilder.create()
			.type(HikariDataSource.class)
			.build();
	}

	@Bean(ROUTING_DATASOURCE)
	public DataSource routingDataSource(
		@Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
		@Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource
	) {
		RoutingDataSource routingDataSource = new RoutingDataSource();

		Map<Object, Object> datasourceMap = new HashMap<>();
		datasourceMap.put("master", masterDataSource);
		datasourceMap.put("slave", slaveDataSource);

		routingDataSource.setTargetDataSources(datasourceMap);
		routingDataSource.setDefaultTargetDataSource(masterDataSource);

		return routingDataSource;
	}

	@Bean
	@Primary
	public LazyConnectionDataSourceProxy dataSource(
		@Qualifier(ROUTING_DATASOURCE) DataSource routingDataSource
	){
		return new LazyConnectionDataSourceProxy(routingDataSource);
	}
}
