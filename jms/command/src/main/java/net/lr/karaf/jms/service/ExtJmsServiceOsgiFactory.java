package net.lr.karaf.jms.service;

import javax.jms.Connection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ExtJmsServiceOsgiFactory implements ExtJmsServiceFactory {

    BundleContext bundleContext;

    @Override
    public ExtJmsService create(Connection connection) {
        ServiceReference[] refs;
        try {
            refs = bundleContext.getServiceReferences(ExtJmsServiceFactory.class.getName(), null);
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        for (ServiceReference ref : refs) {
            ExtJmsServiceFactory factory = (ExtJmsServiceFactory)bundleContext.getService(ref);
            if (factory.canHandle(connection)) {
                    return factory.create(connection);
            }
        }
            return new DummyExtJmsService();
    }

    @Override
    public boolean canHandle(Connection connection) {
        return true;
    }

}
