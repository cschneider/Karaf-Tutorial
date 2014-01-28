package net.lr.tasklist.webservice;

import javax.inject.Inject;

import org.apache.cxf.Bus;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

//@WebServlet(urlPatterns="/tasklist1")
@OsgiServiceProvider
@Properties({@Property(name="alias", value="/tasklist1")})
public class TasklistServlet extends CXFNonSpringServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TasklistServlet() {
        super();
    }

    @Inject
    public void setBus(Bus bus) {
        super.setBus(bus);
    }

}
