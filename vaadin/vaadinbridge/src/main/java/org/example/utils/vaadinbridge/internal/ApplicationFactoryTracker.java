package org.example.utils.vaadinbridge.internal;

import javax.servlet.ServletException;

import org.example.utils.vaadinbridge.ApplicationFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

class ApplicationFactoryTracker extends ServiceTracker {
	
	private final HttpService httpService;
	
	ApplicationFactoryTracker(HttpService httpService, BundleContext context) {
		super(context, ApplicationFactory.class.getName(), null);
		this.httpService = httpService;
	}
	
	@Override
	public Object addingService(ServiceReference ref) {
		
		Object aliasObj = ref.getProperty(ApplicationFactory.ALIAS_NAME);
		if(aliasObj instanceof String) {
			String alias = (String) aliasObj;
			BundleContentHttpContext httpContext = new BundleContentHttpContext(ref.getBundle());
			
			ApplicationFactory factory = (ApplicationFactory) context.getService(ref);
			ApplicationFactoryServlet servlet = new ApplicationFactoryServlet(factory);
			
			try {
				httpService.registerServlet(alias, servlet, null, httpContext);
				return alias;
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamespaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	@Override
	public void removedService(ServiceReference ref, Object service) {
		String alias = (String) service;
		httpService.unregister(alias);
		
		context.ungetService(ref);
	}
}
