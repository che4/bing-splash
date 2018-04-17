package io.github.che4.splash;

import java.util.Optional;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		//org.eclipse.e4.core.di.InjectorFactory.getDefault().addBinding(LanguagesMenu.class).implementedBy(arg0);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public static Optional<IProvisioningAgent> getProvisioningAgent(){
		BundleContext bundleContext = Activator.getContext();
		ServiceReference<IProvisioningAgent> s1 = bundleContext.getServiceReference(IProvisioningAgent.class);
		return Optional.ofNullable(bundleContext.getService(s1));
	}
	
	public static Bundle getThisBundle(){
		return context.getBundle();
	}

}
