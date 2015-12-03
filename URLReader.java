package htmlProject;
import java.awt.*;
import java.io.*;

public class URLReader {


  public static void printToTextFile(String filename, String textToWrite) throws Exception{

    File myFile = new File(filename);

    if(!myFile.exists()) {
        myFile.createNewFile();
    }

    PrintWriter writer = new PrintWriter(filename, "UTF-8");

    BufferedReader br = new BufferedReader(new StringReader(textToWrite));

    String line = null;

    while ((line = br.readLine()) != null) {
      writer.println(line);
    }

    writer.close();
  }

    public static boolean openHTMLFile(String fileName) throws IOException {
      File file = new File(fileName);

      Desktop desktop = null;

      if (Desktop.isDesktopSupported()) {
          desktop = Desktop.getDesktop();
      }

      desktop.open(file);

      return true;
    }


    public static String flagErrors(String openingTag, String closingTag) {

      String result = "\n<!--\n   --Error Found on Line Above--\n" +
        "Found opening tag " + openingTag + " closed by " + closingTag +
        "\nPlease fix and try again\n-->\n";

      return result;
    }


    public static void main(String[] args) throws Exception {

      Parser woodyAllenSite = new Parser("http://woodyatrandom.com/recommend.php?film=14");
      String[] results = woodyAllenSite.parseSiteForErrors();
      String checkedSite = results[0];
      String errors = results[1];
      String filename = "RawHTML.txt";
      printToTextFile(filename, checkedSite);
      openHTMLFile(filename);
      filename = "Errors.txt";
      printToTextFile(filename, errors);
      openHTMLFile(filename);

    }
}


