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
package openmods.igw.api.init;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Identifies a class that initializes the mod.
 *
 * <p>Initialization identifies a class which allows the mod
 * to load through all the three normal stages of mod
 * initialization, namely pre-initialization, initialization
 * and post-initialization.</p>
 *
 * <p>For server related life cycles or other initialization
 * phases, refer to other classes in this package.</p>
 *
 * @since 1.0
 */
public interface IInit {

    /**
     * Constructs the mod instance, before any other event is called.
     *
     * @param event
     *         The construction event provided by FML.
     *
     * @since 1.0
     */
    void construct(final FMLConstructionEvent event);

    /**
     * Loads the mod through the phase of pre-initialization.
     *
     * @param event
     *         The pre-initialization event provided by FML.
     *
     * @since 1.0
     */
    void preInit(final FMLPreInitializationEvent event);

    /**
     * Loads the mod through the phase of initialization.
     *
     * @param event
     *         The initialization event provided by FML.
     *
     * @since 1.0
     */
    void init(final FMLInitializationEvent event);

    /**
     * Loads the mod through the phase of post-initialization.
     *
     * @param event
     *         The post-initialization event provided by FML.
     *
     * @since 1.0
     */
    void postInit(final FMLPostInitializationEvent event);
}
