package org.hibernate.test.query.parser.criteria.select;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.jpa.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by johara on 22/02/16.
 */
public class CriteriaQueryBuilderAbstractTest {
    protected static final String[] NO_MAPPINGS = new String[0];

    protected HibernatePersistenceProvider persistenceProvider;
    protected Map settings;

    protected Dialect getDialect() {
        return Dialect.getDialect();
//		return dialect;
    }


    public CriteriaQueryBuilderAbstractTest() {

        String persistenceUnitName = "TEST";
        List<String> entityClassNames = new ArrayList<String>(  );
        Properties properties = new Properties(  );

        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(
                persistenceUnitName,
                entityClassNames,
                properties
        );

        settings = new HashMap();

        persistenceProvider = new HibernatePersistenceProvider();
        persistenceProvider.createContainerEntityManagerFactory(
                persistenceUnitInfo,
                buildSettings()
        );


    }

    @SuppressWarnings("unchecked")
    protected Map buildSettings() {
        Map settings = getConfig();
        addMappings( settings );

        settings.put( org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO, "create-drop" );
        settings.put( org.hibernate.cfg.AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true" );
//        settings.put( org.hibernate.cfg.AvailableSettings.DIALECT, getDialect().getClass().getName() );
        settings.put( org.hibernate.cfg.AvailableSettings.DIALECT, "H2" );
        return settings;
    }

    protected void addMappings(Map settings) {
        String[] mappings = getMappings();
        if ( mappings != null ) {
            settings.put( AvailableSettings.HBXML_FILES, StringHelper.join( ",", mappings ) );
        }
    }

    protected String[] getMappings() {
        return NO_MAPPINGS;
    }

    protected Map getConfig() {
        Map<Object, Object> config = Environment.getProperties();
        ArrayList<Class> classes = new ArrayList<Class>();

        classes.addAll( Arrays.asList( new Class[0] ) );
        config.put( AvailableSettings.LOADED_CLASSES, classes );
        for (Map.Entry<Class, String> entry : new HashMap<Class, String>().entrySet()) {
            config.put( AvailableSettings.CLASS_CACHE_PREFIX + "." + entry.getKey().getName(), entry.getValue() );
        }
        for (Map.Entry<String, String> entry : new HashMap<String, String>().entrySet()) {
            config.put( AvailableSettings.COLLECTION_CACHE_PREFIX + "." + entry.getKey(), entry.getValue() );
        }

        return config;
    }






    public class PersistenceUnitInfoImpl implements PersistenceUnitInfo {

        private final String persistenceUnitName;

        private PersistenceUnitTransactionType transactionType =
                PersistenceUnitTransactionType.RESOURCE_LOCAL;

        private final List<String> managedClassNames;

        private final Properties properties;

        private DataSource jtaDataSource;

        private DataSource nonJtaDataSource;

        public PersistenceUnitInfoImpl(
                String persistenceUnitName,
                List<String> managedClassNames,
                Properties properties) {
            this.persistenceUnitName = persistenceUnitName;
            this.managedClassNames = managedClassNames;
            this.properties = properties;
        }

        @Override
        public String getPersistenceUnitName() {
            return persistenceUnitName;
        }

        @Override
        public String getPersistenceProviderClassName() {
            return HibernatePersistenceProvider.class.getName();
        }

        @Override
        public PersistenceUnitTransactionType getTransactionType() {
            return transactionType;
        }

        @Override
        public DataSource getJtaDataSource() {
            return jtaDataSource;
        }

        public PersistenceUnitInfoImpl setJtaDataSource(DataSource jtaDataSource) {
            this.jtaDataSource = jtaDataSource;
            this.nonJtaDataSource = null;
            transactionType = PersistenceUnitTransactionType.JTA;
            return this;
        }

        @Override
        public DataSource getNonJtaDataSource() {
            return nonJtaDataSource;
        }

        public PersistenceUnitInfoImpl setNonJtaDataSource(DataSource nonJtaDataSource) {
            this.nonJtaDataSource = nonJtaDataSource;
            this.jtaDataSource = null;
            transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
            return this;
        }

        @Override
        public List<String> getMappingFileNames() {
            return null;
        }

        @Override
        public List<URL> getJarFileUrls() {
            return Collections.emptyList();
        }

        @Override
        public URL getPersistenceUnitRootUrl() {
            return null;
        }

        @Override
        public List<String> getManagedClassNames() {
            return managedClassNames;
        }

        @Override
        public boolean excludeUnlistedClasses() {
            return false;
        }

        @Override
        public SharedCacheMode getSharedCacheMode() {
            return SharedCacheMode.UNSPECIFIED;
        }

        @Override
        public ValidationMode getValidationMode() {
            return ValidationMode.AUTO;
        }

        public Properties getProperties() {
            return properties;
        }

        @Override
        public String getPersistenceXMLSchemaVersion() {
            return "2.1";
        }

        @Override
        public ClassLoader getClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }

        @Override
        public void addTransformer(ClassTransformer transformer) {

        }

        @Override
        public ClassLoader getNewTempClassLoader() {
            return null;
        }
    }

}
