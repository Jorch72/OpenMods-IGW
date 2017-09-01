package openmods.igw.impl.integration.openblocks;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.prefab.handler.OpenModsEventHandler;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
//@Explain("Accessed via reflection")
public final class OpenBlocksEventHandler extends OpenModsEventHandler {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String GLASS_CANVAS_PAGE_NAME = "block/openblocks.canvasglass";

    @SubscribeEvent
    public void handleCustomBlocks(final PageChangeEvent event) {
        if (GLASS_CANVAS_PAGE_NAME.equals(event.currentFile)) {
            OpenModsIGWApi.get().log().info("Redirected Glass Canvas page from default to OpenMods-IGW");
            this.redirectToIgwTab(event);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    //@Explain("Field populated by Forge, so...")
    public void handleCustomIcons(final PageChangeEvent event) {
        // TODO Wouldn't this be a "equals" check?
        if (event.currentFile.contains(GLASS_CANVAS_PAGE_NAME) && OpenBlocksItemHolder.GLASS_CANVAS != null) {
            OpenModsIGWApi.get().log().info("Associated Glass Canvas icon to page");
            this.askTabForNewIcon(event, new ItemStack(OpenBlocksItemHolder.GLASS_CANVAS));
        }
    }

    @SubscribeEvent
    public void addCurrentBlockStatus(final VariableRetrievalEvent event) {
        this.replaceVariableWithBlockStatus(event);
    }

    @SubscribeEvent
    public void addCurrentConfigValues(final VariableRetrievalEvent event) {
        this.replaceVariableWithConfigValue(event);
    }

    @SubscribeEvent
    public void addCurrentItemStatus(final VariableRetrievalEvent event) {
        this.replaceVariableWithItemStatus(event);
    }

    @Nonnull
    @Override
    public String modId() {
        return openmods.Mods.OPENBLOCKS;
    }

    @Nonnull
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public String configClass() {
        return "openblocks.Config";
    }
}
