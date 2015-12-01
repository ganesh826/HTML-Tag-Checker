import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.Stack;

public class URLReader {

    public static boolean isSelfClosing(String openingTag) {
        String[] selfClosingTagArray = {"<area>", "<base>",  "<br>", "<br />", "<br/>", "<col>", 
          "<embed>", "<hr>","<hr/>", "<hr /", "<img>", "<input>", "<keygen>", "<link>", 
          "<menuitem>", "<meta>", "<param>", "<source>", "<track>", "<wbr>"};
        /*
         Self closing tags aka void elements taken from W3 Schools:
          >  http://www.w3.org/html/wg/drafts/html/master/syntax.html#void-elements
        */

        boolean selfClosing = false;

        for (String tag : selfClosingTagArray) {
          // Check if the opening tag is a self-closing tag
          if (openingTag.equals(tag)){
            selfClosing = true;
          } // end if
        } // end for

        return selfClosing;
    }


    public static String formatOpeningTag(char[] inputArray, int i){
      String openingTag = "";
      openingTag += "<";
      boolean spaceReached = false;
      // spaceReached is used to find the end of the tag name and the start of 
      // things like id, name, etc. We dont want to store these things, so
      // we can just skip over them.

      for (int j = i+1; j < inputArray.length; j++) {
        // Since "<" has already been acounted for, we don't can start at i+1

        if (inputArray[j] == ' ') {
          //check for the spacereached

          spaceReached = true;
        } //end if

        if (inputArray[j] == '>' && openingTag.charAt(1)!='!') {
          // This finds the end of the tag, and only continues it if it is
          // not a comment.

            openingTag += inputArray[j];

            if (!isSelfClosing(openingTag)) {
              // Validate that the tag is not self closing before pushing
              return openingTag;
              //System.out.println(openingTag);
              // Reset the tags to allow for mutiple tags per row
            } //end if
            else {
              return null;
            }

        } //end if

        else if (inputArray[j] == '>' && openingTag.charAt(1)=='!') {
          return null;
        }

        else if (!spaceReached) {
          // If it's not the end of the tag and a space has not been found, 
          // the string is updated with the new character

          openingTag += inputArray[j];
        }  //end if
      } // end for
      return openingTag;
    } //end formatOpeningTag


    public static String formatClosingTag(char[] inputArray, int i){
      String closingTag = "<"; 
      // closing tag is stored without the '/' so it can be easily compared to the opening.

      for (int j = i+2; j < inputArray.length; j++) {
        // Since the first two characters, "</", are accounted for, we start at the third
          closingTag += inputArray[j];

        if (inputArray[j] == '>') {
          break;
        } //end if

      } // end for
      return closingTag;
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

        //URL oracle = new URL("http://woodyatrandom.com/recommend.php?film=27");
        URL oracle = new URL("https://www.google.com/");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        Stack<String> s = new Stack<String>();

        boolean errorFound = false;
        String errorFlag = null;
        String closingTag;
        String openingTag;
        String inputLine;
        char[] inputArray;

        int counter = 0;
        PrintWriter writer = new PrintWriter("RawHTML.txt", "UTF-8");
        while ((inputLine = in.readLine()) != null) { // Go through each line
          if (errorFound) {
            writer.println(errorFlag);
          }
          writer.println(inputLine);
          errorFound = false;

          counter++;
          closingTag = "";
          openingTag = "";
          
          inputArray = inputLine.toCharArray(); 
          // I found it easier to iterate through an array of characters to look for tags.

          for (int i = 0; i < inputArray.length; i++) {

            if (inputArray[i] == '<') {
              if (inputArray[i + 1] == '/') {
                // if the second character is a /, it must be a closing tag
                openingTag = s.pop();
                // pop off the opening tag for comparison

                closingTag = formatClosingTag(inputArray, i);

                // Check the last opening tag after a closing tag has been found 
                // This section still needs some work
                if (closingTag.equals(openingTag)) {
                  //System.out.println(openingTag + " -- " + closingTag);
                  //System.out.println("------SUCCESS-----");
                  //System.out.println(openingTag + " -- " + closingTag);
                  //System.out.println("-------------------\n");
                } // end if
                else {
                  System.out.println("\nError Line " + counter +"....");
                  // I don't think the line counter works...
                  System.out.println("-------------------");
                  System.out.println(openingTag + " -- " + closingTag);
                  System.out.println("-------------------\n");
                  errorFound = true;
                  errorFlag = flagErrors(openingTag, closingTag);

                } // end else

              } //end if

              else {
                // else, it must be an opening tag.
                openingTag = formatOpeningTag(inputArray, i);
                if (openingTag != null) {
                  s.push(openingTag);
                }
              } //end else
            } // end if
          } //end for

        } //end while


        //Opens the text file in default text editor
        writer.close();

        //path to file
        openHTMLFile("RawHTML.txt");

    }
}

