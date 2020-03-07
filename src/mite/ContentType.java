package mite;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

/**
 * MIME types
 */
public enum ContentType {

    HTML("text/html"),
    TEXT("text/plain"),
    GIF("image/gif"),
    CLASS("application/octet-stream"),
    JPEG("image/jpeg");

    private final String stream;

    ContentType(String stream) {
        this.stream = stream;
    }

    public String getStreamName() {
        return stream;
    }

    public void writeMIMEHeader(Writer writer, StatusCode status, int length) throws IOException {
        writeln("HTTP 1.0 " + status.toString(),writer);
        Date now = new Date();
        writeln("Date: " + now,writer);
        writeln("Server: " + MiteHTTPServer.NAME,writer);
        writeln("Content-length: " + length,writer);
        writeln("Content-type: " + getStreamName(),writer);
        writeln("",writer);
    }

    private void writeln(String string, Writer out) throws IOException {
        out.write(string + "\r\n");
    }
}
