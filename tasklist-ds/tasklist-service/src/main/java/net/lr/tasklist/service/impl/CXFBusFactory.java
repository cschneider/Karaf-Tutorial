package net.lr.tasklist.service.impl;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.blueprint.BlueprintBus;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.management.counters.CounterRepository;
import org.apache.cxf.management.jmx.InstrumentationManagerImpl;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.container.BlueprintContainer;

@Singleton
public class CXFBusFactory {
    private BlueprintBus bus;

    @Inject
    BundleContext bundleContext;

    @Inject
    BlueprintContainer blueprintContainer;

    public Bus create() {
        if (bus == null) {
            bus = new BlueprintBus();
            bus.setBundleContext(bundleContext);
            bus.setBlueprintContainer(blueprintContainer);
            bus.initialize();
            InstrumentationManagerImpl im = new InstrumentationManagerImpl(bus);
            im.setEnabled(true);
            im.setUsePlatformMBeanServer(true);
            im.init();
            CounterRepository repo = new CounterRepository();
            repo.setBus(bus);
        }
        return bus;
    }

    @PreDestroy
    public void destroyBus() {
        if (bus != null) {
            ((ExtensionManagerBus)bus).shutdown();
        }
    }
}
