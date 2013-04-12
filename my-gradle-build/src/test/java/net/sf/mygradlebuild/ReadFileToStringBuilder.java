package net.sf.mygradlebuild;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

final class ReadFileToStringBuilder {
    static StringBuilder readBuildFile(final File parent, final String filename)
            throws FileNotFoundException, IOException {
        final BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(parent, filename)));
        final StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
            sb.append(line).append("\n");
        }
        return sb;
    }

}