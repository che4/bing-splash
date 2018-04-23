package io.github.che4.touchpoint;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.EclipseTouchpoint;
import org.eclipse.equinox.internal.provisional.frameworkadmin.ConfigData;
import org.eclipse.equinox.internal.provisional.frameworkadmin.Manipulator;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;

@SuppressWarnings("restriction")
public class RestoreSplashAction extends ProvisioningAction {
	
	@Override
	public IStatus execute(Map<String, Object> parameters) {
		Manipulator manipulator = (Manipulator) parameters.get(EclipseTouchpoint.PARM_MANIPULATOR);
		ConfigData data = manipulator.getConfigData();
		String previous = data.getProperty(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS);
		if(previous != null) {
			data.setProperty(TouchpointConstants.SPLASH_PROPERTY_NAME, previous);
			//data.getProperties().remove(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS);
			data.setProperty(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS, null);
		}
		String previousArg = data.getProperty(TouchpointConstants.SPLASH_PROGRAM_ARG_PREVIOUS);
		if(previousArg!=null) {
			manipulator.getLauncherData().addProgramArg(TouchpointConstants.SPLASH_PROGRAM_ARG);
			manipulator.getLauncherData().addProgramArg(previousArg);
			data.setProperty(TouchpointConstants.SPLASH_PROGRAM_ARG_PREVIOUS, null);
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		return Status.OK_STATUS;
	}

}
