package io.github.che4.touchpoint;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.EclipseTouchpoint;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.Util;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.actions.ActionConstants;
import org.eclipse.equinox.p2.touchpoint.eclipse.query.OSGiBundleQuery;

import org.eclipse.equinox.p2.query.QueryUtil;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

@SuppressWarnings("restriction")
public class InstallTouchpointAction extends ProvisioningAction {

	@Override
	public IStatus execute(Map<String, Object> parameters) {
		IProvisioningAgent agent = (IProvisioningAgent) parameters.get(ActionConstants.PARM_AGENT);
		IProfile profile = (IProfile) parameters.get(ActionConstants.PARM_PROFILE);
		IInstallableUnit iu = (IInstallableUnit) parameters.get(EclipseTouchpoint.PARM_IU);
		
		/*
		if (!QueryUtil.isGroup(iu)) {
			System.out.println("What is a non-feature doing here!!! -- " + iu); //$NON-NLS-1$
			return Util.createError(iu + " is not a feature");
		}
		*/
		if (!OSGiBundleQuery.isOSGiBundle(iu)) {
			System.out.println("What is a non-bundle doing here!!! -- " + iu); //$NON-NLS-1$
			return Util.createError(iu + " is not a bundle");
		}
		
		if(!iu.isResolved()) {
			return Util.createError(iu + " is not resolved");
		}
		
		Collection<IArtifactKey> artifacts = iu.getArtifacts();
		if (artifacts == null || artifacts.isEmpty())
			return Util.createError(iu + " has no artifacts");
		/*
		IArtifactKey artifactKey = null;
		for (IArtifactKey candidate : artifacts) {
			File file = Util.getArtifactFile(agent, artifactKey, profile);
			FrameworkUtil.getBundle(InstallTouchpointAction.class);
				//.getBundleContext().i.installBundle(candidate.);
//			//PackageAdmin.refreshPackages()
			//Bundle.start()
		}
		*/
		
		return null;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
