package datasource;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.managed.DataSourceXAConnectionFactory;
import org.apache.commons.dbcp2.managed.ManagedDataSource;
import org.apache.commons.dbcp2.managed.PoolableManagedConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.derby.jdbc.EmbeddedXADataSource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


public class Activator implements BundleActivator {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void start(final BundleContext context) throws Exception {
        ServiceTrackerCustomizer customizer = new ServiceTrackerCustomizer() {

            @Override
            public Object addingService(ServiceReference reference) {
                TransactionManager tm = (TransactionManager)context.getService(reference);
                DataSource poolDs = createPoolDs(tm);
                Dictionary<String, String> properties = new Hashtable<>();
                properties.put("osgi.jndi.service.name", "tasklist");
                context.registerService(DataSource.class, poolDs, properties );
                return tm;
            }

            @Override
            public void modifiedService(ServiceReference reference, Object service) {
            }

            @Override
            public void removedService(ServiceReference reference, Object service) {
            }
        };
        ServiceTracker tracker = new ServiceTracker(context, TransactionManager.class.getName(), customizer);
        tracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private  DataSource createPoolDs(TransactionManager tm) {
        EmbeddedXADataSource ds = new EmbeddedXADataSource();
        ds.setDatabaseName("tasklist;create=true");
        DataSourceXAConnectionFactory cf = new DataSourceXAConnectionFactory(tm, ds);
        PoolableManagedConnectionFactory pcf = new PoolableManagedConnectionFactory(cf, null);
        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>(pcf);
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        pool.setConfig(conf);
        return new ManagedDataSource(pool, cf.getTransactionRegistry());
    }

}
