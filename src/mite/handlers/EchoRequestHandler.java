package mite.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.StringTokenizer;

import mite.ContentType;
import mite.HTTPVersion;
import mite.RequestHandler;
import mite.StatusCode;

/**
 * Simple handler mostly for demonstration and debugging.
 */
public final class EchoRequestHandler
        implements RequestHandler
{

    public static EchoRequestHandler newInstance() {
        return new EchoRequestHandler();
    }

    private EchoRequestHandler() {}

    public void handle(String request, Socket socket, InputStream in, OutputStream out) throws IOException {
        StringTokenizer st = new StringTokenizer(request);
        String method = st.nextToken();  // we don't care about the method
        final String filename = st.nextToken();

        String R = "\r";
        String page =
                "<html>" +
                        "<body>" +
                        "<pre>" +
                        "request =" + request  + R +
                        "method  =" + method   + R +
                        "filename=" + filename + R +
                        "</pre>" +
                        "</body>" +
                        "</html>";

        Writer writer = new OutputStreamWriter(out);
        if (HTTPVersion.isMIMEAware(request)) {
            ContentType.HTML.writeMIMEHeader(writer, StatusCode.OK, page.length());
        }
        writer.write(page);
        writer.close();
        socket.close();
    }

    public boolean handles(String request) {
        return true;
    }

}
