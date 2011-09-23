package net.lr.tutorial.configadmin;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class Activator implements BundleActivator {
	private static final String CONFIG_PID = "ConfigApp";
	private ServiceRegistration serviceReg;
	private MyApp app;

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, CONFIG_PID);
		serviceReg = context.registerService(ManagedService.class.getName(), new ConfigUpdater() , properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceReg.unregister();
	}

	/**
	 * Updates the configuration in the application. Of course your class can also directly implement ManagedService but this
	 * way you can work with pojos
	 */
	private final class ConfigUpdater implements ManagedService {
		@SuppressWarnings("rawtypes")
		@Override
		public void updated(Dictionary config) throws ConfigurationException {
			if (config == null) {
				return;
			}
			if (app == null) {
				app = new MyApp();
			}
			app.setTitle((String)config.get("title"));
			app.refresh();
		}
	}
	
}
