import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 */
public class JarLauncher {

    public static void main(String[] args) throws Exception {
        File file = whereWeWereLaunchedFrom();
    }

    private static final File whereWeWereLaunchedFrom()
            throws URISyntaxException
    {
        Class<JarLauncher> c = JarLauncher.class;
        ProtectionDomain domain = c.getProtectionDomain();
        CodeSource       source = domain.getCodeSource();
        URL            location = source.getLocation();

        // This is only available from 1.5.0+
        URI          uri = location.toURI();
        File        file = new File(uri);
        File launchPoint = file.getAbsoluteFile();
        System.out.println("Launchpoint=" + launchPoint);
        return launchPoint;
    }

}
