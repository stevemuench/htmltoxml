package com.stevemuench.utils;

import oracle.sql.CLOB;
import org.w3c.tidy.Tidy;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class HTMLToXHTML {
    public static String toXHTML(String html) {
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setMakeClean(true);
        tidy.setDocType("omit");
        tidy.setDropProprietaryAttributes(true);
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        tidy.parseDOM(in, out);
        String xhtml = null;
        try {
            xhtml = out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return xhtml;
    }

    // NEW method for Oracle to call
    public static CLOB toXML(CLOB inputClob) throws Exception {
        // Read CLOB into String
        Reader reader = inputClob.getCharacterStream();
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[8192];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, read);
        }
        reader.close();

        String cleanedXHTML = toXHTML(sb.toString());

        // Get Oracle DB internal connection
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");

        // Create a new temporary CLOB for result
        CLOB outputClob = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
        outputClob.setString(1, cleanedXHTML);
        return outputClob;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar htmltoxml.jar <html-content>");
            System.exit(1);
        }

        String html = args[0];
        String xhtml = toXHTML(html);
        System.out.println(xhtml);
    }

}
