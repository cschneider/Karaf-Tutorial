package org.example.utils.vaadinbridge.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;


class HttpServiceTracker extends ServiceTracker {
	
	HttpServiceTracker(BundleContext context) {
		super(context, HttpService.class.getName(), null);
	}
	
	@Override
	public Object addingService(ServiceReference reference) {
		HttpService httpService = (HttpService) context.getService(reference);
		try {
			httpService.registerResources("/VAADIN", "/VAADIN", new TargetBundleHttpContext(context, "com.vaadin"));
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
		ApplicationFactoryTracker bridge = new ApplicationFactoryTracker(httpService, context);
		bridge.open();
		return bridge;
	}
	@Override
	public void removedService(ServiceReference reference, Object service) {
		ApplicationFactoryTracker bridge = (ApplicationFactoryTracker) service;
		bridge.close();
		context.ungetService(reference);
	}
}
