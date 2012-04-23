package org.example.utils.vaadinbridge.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class VaadinBridgeActivator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		new HttpServiceTracker(context).open();
	}
	public void stop(BundleContext context) throws Exception {
	}
}
