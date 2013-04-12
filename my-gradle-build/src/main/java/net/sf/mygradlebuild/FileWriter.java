package net.sf.mygradlebuild;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public final class FileWriter {

    public void writeToFile(final File parent, final String contents,
            final String newFile) throws IOException {
        writeToFile(toFile(parent, newFile), contents);
    }

    @SuppressWarnings("static-method")
    public void writeToFile(final File fileToWrite, final String contents)
            throws IOException {
        write(withWriter(fileToWrite), contents);
    }

    private static Writer withWriter(final File file) throws IOException {
        return new BufferedWriter(new java.io.FileWriter(file));
    }

    private static File toFile(final File parent, final String fileName) {
        return new File(parent, fileName);
    }

    private static void write(final Writer out, final String contents)
            throws IOException {
        try {
            out.write(contents);
        } finally {
            out.close();
        }
    }
}