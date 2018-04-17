package io.github.che4.splash.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import io.github.che4.splash.E4Constants;

public class BingSettingsViewHandler {
	@Execute
	public void execute(EPartService partService) {
		MPart mpart = partService.showPart(E4Constants.PART_DESCRIPTOR_ID, PartState.ACTIVATE);
	}
}
