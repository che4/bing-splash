package com.itranga.e4.branding.providers.bing;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;

public class EcfTest {
	public void config() {
		try {
			org.eclipse.ecf.core.IContainer c = ContainerFactory.getDefault().createContainer();
		} catch (ContainerCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
