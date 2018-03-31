/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Open Mods
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package openmods.igw.impl.utils;

import com.google.common.collect.ImmutableMap;
import openmods.Mods;

import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.service.ISystemIdentifierService;
import openmods.igw.prefab.record.mod.ModEntry;

import java.util.Map;

@SuppressWarnings("unused") // Most, it not all of these, are actually used through reflection
public final class Constants {

    public static final String MOD_ID = "OpenMods-IGW";
    public static final String NAME = "OpenMods-IGW";
    public static final String VERSION = "$VERSION$";
    public static final String FACTORY_CLASS = "openmods.igw.impl.client.GuiFactory";
    public static final String DEPENDENCIES = "required-after:openmods@[$LIB-VERSION$,$NEXT-LIB-VERSION$);"
             + "after:" + Mods.IGW + ";"
             + "after:" + Mods.OPENBLOCKS;
    public static final String UPDATE_URL = null; // TODO Update URL

    public static final String CLIENT_PROXY = "openmods.igw.impl.proxy.ClientProxy";
    public static final String COMMON_PROXY = "openmods.igw.impl.proxy.ServerProxy";

    public static final IModEntry[] CURRENTLY_SUPPORTED_MODS = new IModEntry[] {
            ModEntry.of(Mods.OPENBLOCKS, "1.7.2"),
    };

    @SuppressWarnings("MagicNumber")
    public static final Map<ISystemIdentifierService.SystemDetails, ISystemIdentifierService.SystemType> SYSTEMS =
            ImmutableMap.<ISystemIdentifierService.SystemDetails, ISystemIdentifierService.SystemType>builder()
                    .put(new ISystemIdentifierService.SystemDetails() {
                        {
                            // TSM - Development Environment
                            this.os = "Windows 10";
                            this.architecture = "amd64";
                            this.runDir = "E:/GitHub/OpenMods-IGW/run";
                            this.diskSpace = 119122100224L;
                            this.processors = 4;
                        }
                    }, ISystemIdentifierService.SystemType.DEVELOPER)
                    .build();

    private Constants() {}
}
