/****************************************************************************\
 __FILE..........: ChangesMojoTest.java
 __AUTHOR........: sergei

 __VERSION.......: 1.0
 __DESCRIPTION...:
 __HISTORY.......: DATE       COMMENT
 _____________________________________________________________________________
 ________________:10.01.13 sergei: created.
 ****************************************************************************/


package com.github.sionin.maven.mojo.changes;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Goal which edit changes file
 *
 * @goal changes
 * @phase deploy
 */
public class ChangesMojo extends AbstaractChangesMojo {

    /*===========================================[ STATIC VARIABLES ]===============*/

    public static final String HEADER =
            "==========================================\r\n" +
            " ${title}\r\n" +
            "==========================================\r\n" +
            " [${today}]\r\n\r\n\r\n";

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");

    /*===========================================[ INSTANCE VARIABLES ]=============*/


    /**
     * Command to file edit
     *
     * @parameter expression="${edit.command}"
     * default-value="notepad"
     */
    private String editCommand;

    /*===========================================[ CONSTRUCTORS ]===================*/

    /*===========================================[ CLASS METHODS ]==================*/

    public void execute() throws MojoExecutionException, MojoFailureException {

        File newChangesFile = createNewChanges(changesFile);

        editChanges(newChangesFile);

        if (!changesFile.delete()) {
            getLog().error("Cant delete file" + changesFile);
        }
        if (newChangesFile.renameTo(changesFile)) {
            getLog().error("Cant rename file" + changesFile);
        }
        newChangesFile.renameTo(changesFile);
    }

    private File createNewChanges(File textFile) throws MojoExecutionException {
        if (textFile != null) {

            if (textFile.isDirectory()) {
                throw new MojoExecutionException(
                        "The " + textFile.getAbsolutePath() + " is a directory which is not supported changes file");
            }
            try {

                if (!textFile.exists()) {
                    textFile.createNewFile();
                }

                File newChangesFile = new File(textFile.getParent(), "new" + textFile.getName());

                FileWriter writer = new FileWriter(newChangesFile, true);
                BufferedWriter out = new BufferedWriter(writer);

                String header = HEADER.
                        replace("${title}", title).
                        replace("${today}", sdf.format(new Date()));

                out.write(header);

                FileReader reader = new FileReader(textFile);
                BufferedReader in = new BufferedReader(reader);
                String line = null;
                while ((line = in.readLine()) != null) {
                    out.write(line);
                    out.newLine();
                }

                in.close();
                out.close();

                return newChangesFile;

            } catch (Exception e) {
                throw new MojoExecutionException("File read error", e);
            }
        }
        throw new MojoExecutionException(
                "Changes file can be null");
    }


    private void editChanges(File changes) throws MojoExecutionException {
        try {

            String[] commands = new String[]{
                    editCommand,
                    convertPath(changes)
            };

            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            InputStream processOutput = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(processOutput));

            int exitVal = process.waitFor();

            if (getLog().isDebugEnabled()) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    getLog().debug(line);
                }
                getLog().debug("Process exitValue: " + exitVal);
            }

        } catch (Exception e) {
            throw new MojoExecutionException("File modification error", e);
        }
    }

    private static String convertPath(File file) throws MalformedURLException {
        URL url = file.toURI().toURL();
        return url.getPath().substring(1);
    }

}
