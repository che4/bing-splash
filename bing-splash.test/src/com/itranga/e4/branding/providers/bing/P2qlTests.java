package com.itranga.e4.branding.providers.bing;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.junit.Test;

public class P2qlTests {
	
	private static String IU = "io.github.che4.splash.feature.feature.group";
	
	IProvisioningAgent agent;

	//@Test
	public void getAllFeatures() {
		IQuery<IInstallableUnit> query = QueryUtil.createQuery("everything.select(x | x.id == $0)", IU);
	}
}
