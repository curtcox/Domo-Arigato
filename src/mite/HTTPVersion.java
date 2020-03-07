package mite;

import java.util.StringTokenizer;

/**
 * HTTP version.
 */
public final class HTTPVersion {


    public static boolean isMIMEAware(String request) {
        final StringTokenizer st = new StringTokenizer(request);
        st.nextToken(); // we don't care about the method
        st.nextToken(); // or what is being requested
        String version = "";
        if (st.hasMoreTokens(  )) {
            version = st.nextToken(  );
        }
        return version.startsWith("HTTP ");
    }

}
