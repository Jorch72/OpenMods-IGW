package openmods.igw.impl.utils;

import com.google.common.collect.ImmutableMap;
import openmods.Mods;

import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.service.ISystemIdentifierService;
import openmods.igw.prefab.record.mod.ModEntry;

import java.util.Map;

public final class Constants {

    public static final String MOD_ID = "OpenMods-IGW";
    public static final String NAME = "OpenMods-IGW";
    public static final String VERSION = "$VERSION$";
    public static final String FACTORY_CLASS = "openmods.igw.impl.client.GuiFactory";
    // Stupid Java restrictions!
    public static final String DEPENDENCIES = "required-after:openmods@[$LIB-VERSION$,$NEXT-LIB-VERSION$);" +
            "after:IGWMod;" +
            "after:" + Mods.OPENBLOCKS;

    public static final String CLIENT_PROXY = "openmods.igw.impl.proxy.ClientProxy";
    public static final String COMMON_PROXY = "openmods.igw.impl.proxy.CommonProxy";

    @SuppressWarnings("unused")
    public static final IModEntry[] CURRENTLY_SUPPORTED_MODS = new IModEntry[] {
                                                                            ModEntry.of(Mods.OPENBLOCKS, "1.7.2"),
                                                                         };

    @SuppressWarnings("unused")
    public static final Map<ISystemIdentifierService.SystemDetails, ISystemIdentifierService.SystemType> SYSTEMS =
            ImmutableMap.<ISystemIdentifierService.SystemDetails, ISystemIdentifierService.SystemType>builder()
                    .put(new ISystemIdentifierService.SystemDetails() {
                        {
                            // TSM - Development Environment
                            this.os = "Windows 10";
                            this.architecture = "amd64";
                            this.runDir = "E:/GitHub/OpenMods-IGW/run";
                            this.diskSpace = 119559680000L;
                            this.processors = 4;
                        }
                    }, ISystemIdentifierService.SystemType.DEVELOPER)
                    .build();
}
