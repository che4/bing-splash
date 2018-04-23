package io.github.che4.touchpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.EclipseTouchpoint;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.Util;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.actions.ActionConstants;
import org.eclipse.equinox.internal.provisional.frameworkadmin.ConfigData;
import org.eclipse.equinox.internal.provisional.frameworkadmin.LauncherData;
import org.eclipse.equinox.internal.provisional.frameworkadmin.Manipulator;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;


@SuppressWarnings("restriction")
public class BingSplashAction extends ProvisioningAction {
	
	@Override
	public IStatus execute(Map<String, Object> parameters) {
		//org.eclipse.equinox.p2.core.IProvisioningAgent agent = (IProvisioningAgent) parameters.get(ActionConstants.PARM_AGENT);
		IProfile profile = (IProfile) parameters.get(ActionConstants.PARM_PROFILE);
		IInstallableUnit iu = (IInstallableUnit) parameters.get(EclipseTouchpoint.PARM_IU);
		
		IQuery<IInstallableUnit> query = QueryUtil.createQuery("latest(f|f.providedCapabilities.exists(cap|cap.namespace==$0 && cap.name==$1)"+
			" && everything.exists(iu|iu.id==$2 && iu.requirements.exists(rc| f~=rc)))",
				TouchpointConstants.NAMESPACE_ECLIPSE_TYPE, TouchpointConstants.TYPE_ECLIPSE_FEATURE, iu.getId());
		
		IQueryResult<IInstallableUnit> result = profile.query(query, null);
		if(result.isEmpty()) {
			return Util.createError("Failed to find feature.jar for " + iu.getId());
		}
		IInstallableUnit featureJar = result.iterator().next();
		Collection<IProvidedCapability> caps = featureJar.getProvidedCapabilities();
		Iterator<IProvidedCapability> iter = caps.iterator();
		String featureName = null;
		while(iter.hasNext()) {
			IProvidedCapability cap = iter.next();
			String namespace = cap.getNamespace();
			if(namespace != null && namespace.equals(TouchpointConstants.CAPABILITY_NS_UPDATE_FEATURE)) {
				featureName = cap.getName();
				break;
			}
		}
		if(featureName == null) {
			return Util.createError("Failed to find capability " + TouchpointConstants.CAPABILITY_NS_UPDATE_FEATURE + " in " + featureJar.getId());
		}
		
		/* add program property to platform:/base/configuration/configuration.ini */
		Manipulator manipulator = (Manipulator) parameters.get(EclipseTouchpoint.PARM_MANIPULATOR);
		ConfigData data = manipulator.getConfigData();
		String previous = data.getProperty(TouchpointConstants.SPLASH_PROPERTY_NAME);
		/* make a backup - even if it is null */ 
		//getMemento().put(ActionConstants.PARM_PREVIOUS_VALUE, previous);
		data.setProperty(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS, previous);
		data.setProperty(TouchpointConstants.SPLASH_PROPERTY_NAME, TouchpointConstants.SPLASH_PREFIX + featureName);
		
		LauncherData launcherData = manipulator.getLauncherData();
		String[] programArgs = launcherData.getProgramArgs();
		//List<String> newArgs = new ArrayList<>();
		TxtFile log = TxtFile.get();
		try {
			log.appendLine("Program arguments");
		for( int i=0; i<programArgs.length; i++) {
			
			if(programArgs[i].equalsIgnoreCase(TouchpointConstants.SPLASH_PROGRAM_ARG)) {
				int valueIdx = i + 1;
				if(valueIdx < programArgs.length) {
					if(!programArgs[valueIdx].startsWith("-")) {
						data.setProperty(TouchpointConstants.SPLASH_PROGRAM_ARG_PREVIOUS, programArgs[valueIdx]);
						//skip next argument and continue
						i++;
						log.appendLine("Removing arguments -showsplash " + programArgs[valueIdx]);
						launcherData.removeProgramArg(TouchpointConstants.SPLASH_PROGRAM_ARG);
						launcherData.removeProgramArg(programArgs[valueIdx]);
						continue;
					}
				}
			}
			log.appendLine(programArgs[i]);
			//newArgs.add(programArgs[i]);
		}
		//launcherData.setProgramArgs(newArgs.toArray(new String[] {}));
		} catch (Exception e) {}
		//TODO also handle eclipse.ini:
		/*
		 * -showsplash org.eclipse.epp.package.common
		 */
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		Manipulator manipulator = (Manipulator) parameters.get(EclipseTouchpoint.PARM_MANIPULATOR);
		//String previous = (String) getMemento().get(SPLASH_PROPERTY_NAME);
		ConfigData data = manipulator.getConfigData();
		String previous = data.getProperty(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS);
		if(previous!=null) {
			data.setProperty(TouchpointConstants.SPLASH_PROPERTY_NAME, previous);
			//data.getProperties().remove(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS);
			data.setProperty(TouchpointConstants.SPLASH_PROPERTY_NAME_PREVIOUS, null);
		}
		String previousArg = data.getProperty(TouchpointConstants.SPLASH_PROGRAM_ARG_PREVIOUS);
		if(previousArg!=null) {
			manipulator.getLauncherData().addProgramArg(TouchpointConstants.SPLASH_PROGRAM_ARG);
			manipulator.getLauncherData().addProgramArg(previousArg);
		}
		return Status.OK_STATUS;
	}

}
