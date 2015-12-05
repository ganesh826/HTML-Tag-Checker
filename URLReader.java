import java.awt.*;
import java.io.*;
import java.util.Scanner;

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

    public static String getURL(){
      System.out.println("Enter web address below. Note: requires hhtp header");
      Scanner scan = new Scanner(System.in);
      String url = scan.nextLine();
      return url;
    }
    public static void main(String[] args) throws Exception {



      //Parser website = new Parser(getURL());
      Parser website = new Parser("http://www.cs.siu.edu/");
      String[] results = website.parseSiteForErrors();
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


