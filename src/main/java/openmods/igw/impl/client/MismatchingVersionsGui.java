package openmods.igw.impl.client;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.record.mod.IMismatchingModEntry;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * This GUI is shown to the end user when the version of the
 * installed mods mismatch with the wiki-supported ones.
 *
 * <p>By default, the check allows as valid versions only the
 * one specified in {@link openmods.igw.impl.utils.Constants}, but
 * this behaviour can be modified through the annotations
 * available in the API package. Refer to the specific
 * documentation for more information.</p>
 *
 * <p>This GUI cannot be disabled.</p>
 *
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class MismatchingVersionsGui extends GuiErrorScreen {

    private static boolean show;
    private static final String TITLE = OpenModsIGWApi.get().translate("mismatching.title");
    private static final String MESSAGE_FIRST_PART = OpenModsIGWApi.get().translate("mismatching.message.first");
    private static final String MESSAGE_SECOND_PART = OpenModsIGWApi.get().translate("mismatching.message.second");
    @SuppressWarnings("SpellCheckingInspection")
    private static final String ENTRY_RAW = "openmods.igw.mismatching.entry.raw"; // FFS, I18n! Why you break my services??
    private static final String ENTRY_MORE = OpenModsIGWApi.get().translate("mismatching.entry.more");
    private static final String BUTTON_TEXT = OpenModsIGWApi.get().translate("button.confirm");
    private static final int CONFIRM = 0;
    private static final int BUTTON_Y = 140;
    private static final int TITLE_Y = 75;
    private static final int MESSAGE_FIRST_Y = 95;
    private static final int MESSAGE_SECOND_Y = 110;
    private static final int LIST_Y_START = 130;
    private static final int LIST_Y_JUMP = 15;
    private static final int MAX_Z_LEVEL = 16777215;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DISTANCE_FROM_BOTTOM = BUTTON_HEIGHT + 5;
    private static final int GREEN = -10108578;
    private static final int RED = -11530224;

    private final List<IMismatchingModEntry> mismatchingMods = Lists.newArrayList();

    public MismatchingVersionsGui(final List<IMismatchingModEntry> mismatchingMods) {
        super(TITLE, MESSAGE_FIRST_PART + "\n" + MESSAGE_SECOND_PART);
        show = false;
        this.mismatchingMods.addAll(mismatchingMods);
        OpenModsIGWApi.get().log().warning("Identified mismatching mods. Gui constructed and shown.");
        OpenModsIGWApi.get().log().warning("    Mismatching mods: ");
        OpenModsIGWApi.get().log().warning("    " + this.mismatchingMods.toString());
    }

    @Override
    public void initGui() {
        this.buttonList.add(
                new GuiButton(CONFIRM,
                    this.width / 2 - 100,
                    Math.min(BUTTON_Y + LIST_Y_JUMP * this.mismatchingMods.size(), this.height - DISTANCE_FROM_BOTTOM),
                    BUTTON_TEXT)
        );
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case CONFIRM:
                this.mc.displayGuiScreen(null);
                break;
            default:
                throw new IllegalStateException("Invalid button ID in MismatchingVersionGui @ OpenMods-IGW");
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        this.drawDefaultBackground();
        this.drawGradientRect(0, 0, this.width, this.height, GREEN, RED); // May remove it

        this.drawCenteredString(this.fontRendererObj, TITLE, this.width / 2, TITLE_Y, MAX_Z_LEVEL);
        this.drawCenteredString(this.fontRendererObj, MESSAGE_FIRST_PART, this.width / 2, MESSAGE_FIRST_Y, MAX_Z_LEVEL);
        this.drawCenteredString(this.fontRendererObj, MESSAGE_SECOND_PART, this.width / 2, MESSAGE_SECOND_Y, MAX_Z_LEVEL);

        for (final Object obj : this.buttonList) {
            if (!(obj instanceof GuiButton)) continue; // Avoid random crashes

            ((GuiButton) obj).drawButton(this.mc, x, y);
        }

        int currentJump = LIST_Y_START;
        boolean flag = true;

        for (int i = 0; i < this.mismatchingMods.size(); ++i) {
            if (!flag) break;

            final IMismatchingModEntry entry = this.mismatchingMods.get(i);
            String toDraw = this.format(entry);
            final int prevAmount = currentJump;

            currentJump += LIST_Y_JUMP;

            if (currentJump >= this.height - DISTANCE_FROM_BOTTOM && i != (this.mismatchingMods.size() - 1)) {
                toDraw += this.handleMore();
                flag = false;
            }

            this.drawCenteredString(this.fontRendererObj,
                    toDraw,
                    this.width / 2,
                    prevAmount,
                    MAX_Z_LEVEL);
        }
    }

    @Nonnull
    private String format(final IMismatchingModEntry entry) {
        // TODO Expose this in API
        return net.minecraft.client.resources.I18n.format(ENTRY_RAW, entry.modId(), entry.version(), entry.installedVersion());
    }

    /**
     * Adds the reset formatting code at the end.
     *
     * <p>If this operation is not performed, the string will be drawn incorrectly:
     * the shadow will appear normal, while the full string will be drawn in bold.</p>
     *
     * @return
     *     The more string if it ends with {@code §r}, or the more string with
     *     the formatting code appended if it doesn't.
     */
    @Nonnull
    private String handleMore() {
        final String more = MismatchingVersionsGui.ENTRY_MORE;
        return more.endsWith("\u00A7r")? more : more + "\u00A7r";
    }

    /**
     * Tells the Gui to show up when the Main Menu opens.
     *
     * @since 1.0
     */
    public static void show() {
        show = true;
    }

    /**
     * Gets if the Gui should be constructed and shown.
     *
     * @return
     *         If the GUI should be constructed and shown.
     *
     * @since 1.0
     */
    public static boolean shouldShow() {
        return show;
    }
}
