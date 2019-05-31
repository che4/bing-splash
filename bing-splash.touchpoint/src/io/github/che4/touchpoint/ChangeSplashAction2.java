package io.github.che4.touchpoint;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitOperand;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.EclipseTouchpoint;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.Util;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.actions.ActionConstants;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;

@SuppressWarnings("restriction")
public class ChangeSplashAction2  extends ProvisioningAction {

	@Override
	public IStatus execute(Map<String, Object> parameters) {
		IProvisioningAgent agent = (IProvisioningAgent) parameters.get(ActionConstants.PARM_AGENT);
		IProfile profile = (IProfile) parameters.get(ActionConstants.PARM_PROFILE);
		IInstallableUnit iu = (IInstallableUnit) parameters.get(EclipseTouchpoint.PARM_IU);//"iu");
		IInstallableUnit upgradeFrom = null;
		//parameters.get(ActionConstants.PARM_BUNDLE);
		Object operand = parameters.get("operand");
		try {
			if (operand != null && operand instanceof InstallableUnitOperand)
				upgradeFrom = ((InstallableUnitOperand) operand).first();
		} catch (Throwable e) {
			// Ignore class not found in case InstallableUnitOperand is missing
		}
		if (upgradeFrom != null)
			performUpgrade(iu, upgradeFrom);
		else
			performNewInstall(iu);
		
		//TestTxtFile txtFile = TestTxtFile.get();
		parameters.entrySet().stream()
			.forEach( e -> {
				try {
					//txtFile.appendLine(e.getKey()+ " : " + e.getValue().toString() + " : " + e.getValue().getClass().getName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		try{
			//txtFile.appendLine("ARTIFACT LOCATION: " + parameters.get(EclipseTouchpoint.PARM_ARTIFACT_LOCATION).toString());
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			//txtFile.appendLine("ARTIFACT : " + ((IArtifactKey) parameters.get(EclipseTouchpoint.PARM_ARTIFACT)).getId()
			//		+ "  == " + parameters.get(EclipseTouchpoint.PARM_ARTIFACT).toString());
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//txtFile.appendLine("FEATURE TO BE INSTALLED : "+ iu.getId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//txtFile.appendLine("Artifacts "+ iu.getId());
			Collection<IArtifactKey> artifacts = iu.getArtifacts();
			//txtFile.appendLine("Found: " + artifacts.size());
			IArtifactKey artifactKey = null;
			for (IArtifactKey candidate : artifacts) {
				File file = Util.getArtifactFile(agent, artifactKey, profile);
				//txtFile.appendLine("- candidate = " + candidate.getId() + " : " + file.getAbsolutePath());
				//FrameworkUtil.getBundle(InstallTouchpointAction.class);
					//.getBundleContext().i.installBundle(candidate.);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			String url = "platform:/base/plugins/com.itranga.e4.branding.touchpoint";
			URL nativeUrl = FileLocator.resolve(new URL(url));
			//txtFile.appendLine("Platform URL " + url + " = " + nativeUrl.toString());
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void performUpgrade(IInstallableUnit iu, IInstallableUnit oldIu) {
		String msg = "Upgrade: " + iu.getId() + " " + oldIu.getVersion() + " -> " + iu.getVersion();
		try {
			//TestTxtFile txtFile = TestTxtFile.get();
			//txtFile.appendLine(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void performNewInstall(IInstallableUnit iu) {
		String msg = "New Install: " + iu.getId() + " " + iu.getVersion();
		try {
			//TestTxtFile txtFile = TestTxtFile.get();
			//txtFile.appendLine(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
