package openmods.igw.utils;

public interface IPageInit {
	boolean mustRegister(final String modId);

	@SuppressWarnings("SameParameterValue")
	//@Explain("New mods added later")
	void register(final String modId, final Class<? extends igwmod.gui.tabs.IWikiTab> tabClass);
}
