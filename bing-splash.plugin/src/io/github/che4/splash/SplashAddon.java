package io.github.che4.splash;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.workbench.IModelResourceHandler;
import org.osgi.framework.FrameworkUtil;

public class SplashAddon {
	//"io.github.che4.splash.menu"
	private final String bundleName = FrameworkUtil.getBundle(getClass()).getSymbolicName();
	
	@PreDestroy
	public void destroy(final MApplication application, IModelResourceHandler xmi) {
		
		//remove menu contribution
		boolean menuChanged = application.getMenuContributions().removeIf( this :: hasApplicationElement );
		boolean addonChanged = application.getAddons().removeIf( this :: hasApplicationElement );		
		boolean handlerChanged = application.getHandlers().removeIf( this :: hasApplicationElement );		
		boolean descriptorChanged = application.getDescriptors().removeIf( this :: hasApplicationElement );		
		boolean changed = menuChanged || addonChanged || handlerChanged || descriptorChanged;		
		if(changed) try {
			xmi.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasApplicationElement(MApplicationElement elem) {
		String contributor = elem.getContributorURI();
		if(contributor != null) {
			return contributor.equals("platform:/plugin/"+bundleName);
		}
		return false;	
	}

}
