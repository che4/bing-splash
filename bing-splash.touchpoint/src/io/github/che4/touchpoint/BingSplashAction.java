package io.github.che4.touchpoint;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.EclipseTouchpoint;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.Util;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.actions.ActionConstants;
import org.eclipse.equinox.internal.provisional.frameworkadmin.ConfigData;
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
	
	protected static final String NAMESPACE_ECLIPSE_TYPE = "org.eclipse.equinox.p2.eclipse.type";
	protected static final String TYPE_ECLIPSE_FEATURE = "feature";
	protected static final String SPLASH_PROPERTY_NAME = "osgi.splashPath"; //$NON-NLS-1$
	protected static final String SPLASH_PREFIX = "platform:/base/features/"; //$NON-NLS-1$
	protected static final String CAPABILITY_NS_UPDATE_FEATURE = "org.eclipse.update.feature"; //$NON-NLS-1$

	@Override
	public IStatus execute(Map<String, Object> parameters) {
		//org.eclipse.equinox.p2.core.IProvisioningAgent agent = (IProvisioningAgent) parameters.get(ActionConstants.PARM_AGENT);
		IProfile profile = (IProfile) parameters.get(ActionConstants.PARM_PROFILE);
		IInstallableUnit iu = (IInstallableUnit) parameters.get(EclipseTouchpoint.PARM_IU);
		
		IQuery<IInstallableUnit> query = QueryUtil.createQuery("latest(f|f.providedCapabilities.exists(cap|cap.namespace==$0 && cap.name==$1)"+
			" && everything.exists(iu|iu.id==$2 && iu.requirements.exists(rc| f~=rc)))",
				NAMESPACE_ECLIPSE_TYPE, TYPE_ECLIPSE_FEATURE, iu.getId());
		
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
			if(namespace != null && namespace.equals(CAPABILITY_NS_UPDATE_FEATURE)) {
				featureName = cap.getName();
				break;
			}
		}
		if(featureName == null) {
			return Util.createError("Failed to find capability " + CAPABILITY_NS_UPDATE_FEATURE + " in " + featureJar.getId());
		}
		
		/* add program property to platform:/base/configuration/configuration.ini */
		Manipulator manipulator = (Manipulator) parameters.get(EclipseTouchpoint.PARM_MANIPULATOR);
		ConfigData data = manipulator.getConfigData();
		String previous = data.getProperty(SPLASH_PROPERTY_NAME);
		/* make a backup - even if it is null */ 
		getMemento().put(ActionConstants.PARM_PREVIOUS_VALUE, previous);
		data.setProperty(SPLASH_PROPERTY_NAME, SPLASH_PREFIX + featureName);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		Manipulator manipulator = (Manipulator) parameters.get(EclipseTouchpoint.PARM_MANIPULATOR);
		String previous = (String) getMemento().get(SPLASH_PROPERTY_NAME);
		manipulator.getConfigData().setProperty(SPLASH_PROPERTY_NAME, previous);
		return Status.OK_STATUS;
	}

}
