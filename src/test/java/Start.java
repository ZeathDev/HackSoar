import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import com.google.common.base.Strings;
import net.minecraft.client.main.Main;

public class Start {
    public static void main(String[] args) {
        loadNatives();
        Main.main(concat(new String[] {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static void loadNatives() {
        String paths = System.getProperty("java.library.path");
        String nativesDir;
        try {
            nativesDir = new File(Objects.requireNonNull(Start.class.getResource("/natives")).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (paths == null || paths.isEmpty()) {
            paths = nativesDir;
        } else {
            paths += File.pathSeparator + nativesDir;
        }

        System.setProperty("java.library.path", paths);

        // Hack the classloader
        try {
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
