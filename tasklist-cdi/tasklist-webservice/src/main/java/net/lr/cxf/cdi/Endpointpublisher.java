package net.lr.cxf.cdi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class Endpointpublisher {
    
    @Inject @Any @CXFEndpoint
    Instance<Object> services;
    
    @Inject Bus bus;
    
    List<Server> servers = new ArrayList<Server>();
    
    @PostConstruct
    public void initService() {
        Iterator<Object> it = services.iterator();
        System.out.println("Creating CXF Endpoints for " + services.isUnsatisfied() );
        while (it.hasNext()) {
            Object service = it.next();
            System.out.println("Creating CXF Endpoint for " + service.getClass().getName());
            CXFEndpoint cxfEndpoint = service.getClass().getAnnotation(CXFEndpoint.class);
            JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
            factory.setBus(bus);
            Class<?> iface = service.getClass().getInterfaces()[0];
            factory.setServiceClass(iface);
            factory.setAddress(cxfEndpoint.url());
            factory.setServiceBean(service);
            servers.add(factory.create());
        }
    }
    
    @PreDestroy
    public void destroy() {
        for (Server server : servers) {
            server.destroy();
        }
    }
    
}
