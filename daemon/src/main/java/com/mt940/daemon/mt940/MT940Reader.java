package com.mt940.daemon.mt940;

import java.io.*;
import java.util.stream.IntStream;

public class MT940Reader {


    String tag =null;
    String body =null;
    boolean startedTag = false;
    boolean startedBody = false;

    public String read(String input) throws IOException {
        InputStream targetStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new BufferedReader(new InputStreamReader(targetStream));
        int r;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            if (ch == ':') {
                startedTag = true;
                return readTag(reader, ch);
            }
            System.out.println("Do something with " + ch);
        }
        return null;
    }

    private String readTag(Reader reader, char startSymbol) throws IOException {
        StringBuilder builder = new StringBuilder(":");
        int r;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            if (ch == ':') {
                builder.append(ch);
                startedTag = false;
            } else {
                builder.append(ch);
            }

        }
        return builder.toString();
    }
}
