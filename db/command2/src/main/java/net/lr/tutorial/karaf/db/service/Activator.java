package net.lr.tutorial.karaf.db.service;

import java.util.Hashtable;

import net.lr.tutorial.karaf.db.command.SelectCommand;

import org.apache.felix.gogo.commands.Action;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        DbAccess dbAccess = new DbAccess();
        DbSelect dbSelect = new DbSelect();
        dbSelect.setDbAccess(dbAccess);
        dbSelect.setContext(context);
        SelectCommand selectCommand = new SelectCommand();
        selectCommand.setDbSelect(dbSelect);
        context.registerService(Action.class.getName(), selectCommand, new Hashtable<String, String>());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

}
