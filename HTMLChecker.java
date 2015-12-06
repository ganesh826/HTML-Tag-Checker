/*
CS 220 Final Project
HTML Parser and Tag Checker

Sam Messina, Devon Guinn, & Ganesh Koripalli
 */

        import java.awt.*;
        import java.io.*;

public class HTMLChecker {


    public static void printToTextFile(String filename, String textToWrite)
            throws Exception{
    /*
     * This method creates and populates a text file with a given string.
     * It is used to store our programs output to a text file for easier
     * reading.
     *
     * @param The name of the file and the text to write to it.
     * @return There is no return from this method, the file is
     *         simply stored to the user's computer.
     */

        File myFile = new File(filename);

        if(!myFile.exists()) {
            // Create the file if it does not exist.
            myFile.createNewFile();
        }

        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        BufferedReader br = new BufferedReader(new StringReader(textToWrite));

        String line = null;

        while ((line = br.readLine()) != null) {
            // Write each line to the file
            writer.println(line);
        }

        writer.close();
        // Close out the file after completion.
    }


    public static boolean openFile(String fileName) throws IOException {
      /*
       * This method opens a given file in the user's default text editor.
       *
       * @param The name of the file to open.
       * @return Boolean value true if the method completes without error
       */
        File file = new File(fileName);
        if(!file.exists()) {
            file.createNewFile();
        }

        Desktop desktop = null;

        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }

        desktop.open(file);

        return true;
    }


    public static void main(String[] args) throws Exception {

        Parser siu = new Parser("http://www.siu.edu");
        Parser craigslist = new Parser("https://carbondale.craigslist.org/");
        Parser samsWebsite = new Parser("http://woodyatrandom.com/filmography.php");
        Parser reddit = new Parser("https://www.reddit.com/");

        // A new Parser object is created. This object will parse our site for errors.

        String[] results = samsWebsite.parseSiteForErrors();
        // Check the site for errors. See Parser for details on the method.
        // Note: change the Parser object called here to change the site that is checked.

        String checkedSite = results[0];
        // The first element is the HTML text

        String errors = results[1];
        // The second element is the errors

        String filename = "RawHTML.html";
        // Write to and open the html file
        printToTextFile(filename, checkedSite);
        openFile(filename);

        filename = "Errors.txt";
        // Write to and open the error file
        printToTextFile(filename, errors);
        openFile(filename);

    }
}
