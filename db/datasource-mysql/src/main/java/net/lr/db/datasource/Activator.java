package net.lr.db.datasource;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Activator implements BundleActivator {
    

    public final class DataSourceConfig implements ManagedService {
        private final BundleContext context;
        private ServiceRegistration serviceReg = null;

        public DataSourceConfig(BundleContext context) {
            this.context = context;
        }

        @SuppressWarnings("rawtypes")
        private String getString(String key, Dictionary properties) {
            Object value = properties.get(key);
            return (value == null || !(value instanceof String)) ? "" : key;
            
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void updated(Dictionary properties) throws ConfigurationException {
            if (serviceReg != null) {
                try {
                    serviceReg.unregister();
                } catch (Exception e) {
                }
                serviceReg = null;
            }
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUrl(getString("url", properties));
            ds.setUser(getString("user", properties));
            ds.setPassword(getString("password", properties));

            Dictionary<String, String> regProperties = new Hashtable<String, String>();
            regProperties.put("osgi.jndi.service.name" , getString("name", properties));
            serviceReg = context.registerService(DataSource.class, ds, regProperties);
        }
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        ManagedService managedService = new DataSourceConfig(context); 
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put(Constants.SERVICE_PID, "mysqldatasource");
        context.registerService(ManagedService.class, managedService, properties);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

}
