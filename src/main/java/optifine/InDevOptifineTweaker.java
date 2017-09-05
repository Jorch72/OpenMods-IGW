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
package optifine;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

/**
 * NOT PRESENT IN PRODUCTION!
 */
@SuppressWarnings("SpellCheckingInspection")
public class InDevOptifineTweaker implements ITweaker {

    @SuppressWarnings("unused") // Accessed via reflection
    public static final class Transformer implements IClassTransformer {

        // 50 warnings should be enough to make them noticeable in the log, but not spam it.
        // Feel free to tweak this value according to your needs.
        // Set it to a negative number to mark all classes
        private static final int MAX_WARNINGS = 50;

        private final IClassTransformer of;

        private int warnedCount;

        public Transformer() {
            IClassTransformer t;
            try {
                System.out.printf("Attempting to obtain Optifine's class transformer.........");
                t = (IClassTransformer) Class.forName("optifine.OptiFineClassTransformer").newInstance();
                System.out.printf("success!%n");
            } catch (final Exception e) {
                System.err.printf("failure!%n%s %s%n", e.getClass().getName(), e.getMessage());
                for (final StackTraceElement s : e.getStackTrace()) System.err.printf("    at %s%n", s);
                t = null;
            }
            this.of = t;
            if (this.of == null) {
                System.err.printf("***************************************************%n");
                System.err.printf("* NO TWEAKER IDENTIFIED! THIS IS A SERIOUS ERROR! *%n");
                System.err.printf("*       Please disable Optifine dev support       *%n");
                System.err.printf("***************************************************%n");
            }
        }

        @Override
        public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
            if (this.of == null) {
                if (this.warnedCount <= MAX_WARNINGS || MAX_WARNINGS < 0) {
                    System.out.printf("Skipping transformation attempt for %s because the Optifine transformer is unavailable%n", name);
                    final int diff = MAX_WARNINGS - this.warnedCount;
                    System.out.printf("This warning will be printed %d more time%s%n", diff, diff == 1? "" : "s");
                    ++this.warnedCount;
                }
                return basicClass;
            }
            return this.of.transform(name == null? null : name.replace('.', '/'), transformedName, basicClass);
        }
    }

    private static final boolean USE_OF = true;

    @Override
    public void acceptOptions(final List<String> args, final File gameDir, final File assetsDir, final String profile) {
    }

    @Override
    public void injectIntoClassLoader(final LaunchClassLoader classLoader) {
        if (!USE_OF) {
            System.err.printf("*******************************************************%n");
            System.err.printf("*          OPTED OUT OF DEVELOPMENT OPTIFINE          *%n");
            System.err.printf("*    REMOVE OPTIFINE DEV RESOURCE FROM mods/1.10.2    *%n");
            System.err.printf("*******************************************************%n");
            return;
        }

        System.out.printf("*************************************%n");
        System.out.printf("*  LOADING OPTIFINE IN-DEV SUPPORT  *%n");
        System.out.printf("*************************************%n");
        classLoader.registerTransformer("optifine.InDevOptifineTweaker$Transformer");
        System.out.printf("Registered Optifine development transformer%n");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
