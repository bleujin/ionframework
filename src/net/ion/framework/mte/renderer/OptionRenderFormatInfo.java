package net.ion.framework.mte.renderer;

import net.ion.framework.mte.RenderFormatInfo;

public class OptionRenderFormatInfo implements RenderFormatInfo {

	private final String[] options;

	public OptionRenderFormatInfo(String[] options) {
		this.options = options;
	}

	public String[] getOptions() {
		return options;
	}

}
