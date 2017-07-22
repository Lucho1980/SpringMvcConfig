package springmvc.java.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springmvc.java.service.BlogPostService;
import springmvc.java.service.impl.BlogPostServiceImpl;

@EnableJpaRepositories(basePackages={"springmvc.java.dao"})
@EnableTransactionManagement
@Configuration
public class ApplicationContext {

	@Autowired
	private Environment environment;
	
	@Bean
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getProperty("jdbc.driverClass"));
		dataSource.setUrl(environment.getProperty("jdbc.url"));
		dataSource.setUsername(environment.getProperty("jdbc.username"));
		dataSource.setPassword(environment.getProperty("jdbc.password"));
	return dataSource;
	}
	
	@Bean
	public BlogPostService blogPostService(){
		
		return new BlogPostServiceImpl();
	}
	
	@Bean
	@Qualifier("embedded")
	public DataSource dataSourceEmbedded() {

		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase embeddedDatabase = builder
												  .setType(EmbeddedDatabaseType.HSQL)
												  .addScript("dbschema.sql")
												  .addScript("test-data.sql")
												  .build();
		return embeddedDatabase;
	}
	
	@Bean                        //Era JpaTransactionManager
	public JpaTransactionManager transactionManager(EntityManagerFactory  entityManagerFactory){
		
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();		
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);		
		return jpaTransactionManager;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
		
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		
		jpaVendorAdapter.setDatabase(Database.MYSQL);
		
		jpaVendorAdapter.setShowSql(true);
		
		return jpaVendorAdapter;		
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
		
		entityManagerFactory.setPackagesToScan("springmvc.java.domain");//Escanea todo lo que esta ac� adentro
		
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto","create-drop");
		
		entityManagerFactory.setJpaProperties(jpaProperties);
		
		return entityManagerFactory;
		
	}
}
