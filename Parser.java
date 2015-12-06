/*
CS 220 Final Project
HTML Parser and Tag Checker

Sam Messina, Devon Guinn, & Ganesh Koripalli
 */

import java.net.*;
import java.io.*;
import java.util.Stack;

public class Parser {
  String websiteURL;

  public Parser(String websiteURL) {
    this.websiteURL = websiteURL;
  }


  public static boolean isSelfClosing(String openingTag) {
    /*
     * This method takes and opening tag and makes sure it isn't included in W3's list
     * of self-closing, or void tags.
     *
     * @param Takes in the tag.
     * @return returns true if the tag is self-closing, false if it is not.
     */
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
    /*
     * After parseSiteForErrors finds what it thinks is an opening tag,
     * this method takes over and formats the tag so it can be pushed to
     * the stack.
     *
     * @param The current line's char array is taken in, along with the
     *        starting point of the opening tag.
     * @return The opening tag is returned if it is found to be able to
     *         be pushed to the stack, otherwise null is returned.
     */

    String openingTag = "";
    // The tag itself.
    openingTag += "<";
    // We can start it by adding the "<" for it.

    boolean spaceReached = false;
    /*
     * spaceReached is used to find the end of the tag name and the start of
     * things like id, name, etc. We don't want to store these things, so
     * we can just skip over them.
    */

    for (int j = i+1; j < inputArray.length; j++) {
      // Since "<" has already been accounted for, we can start at i+1

      if (inputArray[j] == ' ') {
        //check for spacereached

        spaceReached = true;
      } //end if

      if (inputArray[j] == '>' && openingTag.charAt(1)=='!') {
        // If the tag closes, but the second character is a "!", it is a comment
        // We can return null and exit the method.
        return null;
      }

      else if (inputArray[j] == '>' && openingTag.charAt(1)!='!') {
        // This finds the end of the tag, and only continues it if it is
        // not a comment.

        openingTag += inputArray[j];
        // Add the ">".

        if (!isSelfClosing(openingTag)) {
          // Validate that the tag is not self closing before pushing
          break;
          // Exit the for loop
        } //end if
        else {
          return null;
          // If it is self closing, simply return null.
        }
      } //end if

      else if (!spaceReached) {
        // If it's not the end of the tag and a space has not been found,
        // the string is updated with the new character

        openingTag += inputArray[j];
      }  //end if
    } // end for
    if (openingTag.length() < 2 || openingTag.charAt(1)=='!' ) {
      // One last check outside the for loop. This if fixed false tags
      // coming from less than symbols in inline javascript.
      openingTag = null;
    }
    return openingTag;
  } //end formatOpeningTag


  public static String formatClosingTag(char[] inputArray, int i){
    /*
     * This works just like formatOpeningTag, but for closing tags.
     * Closing tags are easier, since they can't be self-closing and
     * don't have attributes like id, class, etc.
     *
     * @param The current line's char array is taken in, along with the
     *        starting point of the closing tag.
     * @return The closing tag is returned.
     */
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


  public static String flagErrors(String openingTag, String closingTag) {
    /*
     * This method just takes the mismatched opening and closing tags and
     * formats an HTML comment that we can put in our returned file, making
     * it easier to debug.
     *
     * @param The opening and closing tags that did not match.
     * @return The HTML comment detailing the error.
     */
    String result = "\n<!--\n   --Error Found on Line Above--\n" +
            "Found opening tag " + openingTag + " closed by " + closingTag +
            "\nPlease fix and try again\n-->\n";
    return result;
  }


  public String[] parseSiteForErrors() throws Exception{
    /*
     * This method is the main method of Parser. It calls all other methods.
     * It will parse the html file found from websiteURL, and record all
     * errors.
     *
     * @param No parameter, as websiteURL is set in Parser's constructor
     * @return A String array is returned. The first element in the array
     *         is the HTML file, with errors recorded as comments in it.
     *         The second element is a String with only the errors and line
     *         numbers listed.
     */

    String HTMLOutput = "";
    // HTMLOutput is the HTML file with errors commented in
    String errorOutput = "";
    // errorOutput is the text with just the errors in it

    URL oracle = new URL(websiteURL);
    BufferedReader in = new BufferedReader(
            new InputStreamReader(oracle.openStream()));
    // Open the site for reading

    Stack<String> s = new Stack<String>();
    // s is our stack. It stores opening tags as they are found.

    boolean errorFound = false;
    // errorFound is triggered when an error is found on a line.

    String errorFlag = null;
    // errorFlag is the comment written to the HTML file.

    String closingTag;
    // closingTag stores the closing tag as it is being parsed.

    String openingTag;
    // openingTag stores the opening tag as it is being parsed.

    String inputLine;
    // inputLine stores the lines of the HTML as they come up

    char[] inputArray;
    // inputArray will be a char array version of inputLine, so we
    // can parse character by character.

    int counter = 0;
    // Counter is a line counter. It corresponds to the output html file, not
    // the original

    while ((inputLine = in.readLine()) != null) { // Go through each line
      if (errorFound) {
        // if an error was found on the previous line, add the errorFlag to HTMLOutput
        HTMLOutput += errorFlag + "\n";
        errorFlag = "";
      }
      HTMLOutput += inputLine + "\n";
      // Add the next line to our HTMLOutput
      errorFound = false;
      // Make sure errorFound is set to false at the beginning of each line.

      counter++;
      // Increment counter for the new line.

      inputArray = inputLine.toCharArray();
      // I found it easier to iterate through an array of characters to look for tags.

      for (int i = 0; i < inputArray.length; i++) {
        if (inputArray[i] == '<') {
          // If we find a "<", we can check if it's a tag.

          if (inputArray[i + 1] == '/') {
            // if the second character is a /, it must be a closing tag

            if (s.size() != 0) {
              openingTag = s.pop();
              // pop off the opening tag for comparison, but only if one has been pushed
            }
            else {
              openingTag = "";
            }

            closingTag = formatClosingTag(inputArray, i);
            // Call Parser's formatClosingTag method to create our closing tag.

            if (!closingTag.equals(openingTag)) {
              // Check if the closing tag matches the last pushed opening tag.
              // If not, we add the errors.

              errorOutput += "\nError Line " + counter +"....\n";
              errorOutput += "-------------------\n";
              errorOutput += openingTag + " -- " + closingTag + "\n";
              errorOutput += "-------------------\n";
              // Add an error to our errorOutput.

              errorFound = true;
              // Set errorFound to true so we can add our errorFlag.

              errorFlag = flagErrors(openingTag, closingTag);
              // Call Parser's flagErrors method, which formats
              // a nice comment for HTMLOutput.

              counter += 7;
              //counter has to account for the errorFlag text

            } // end if
          } //end if

          else {
            // else, it must be an opening tag.
            openingTag = formatOpeningTag(inputArray, i);
            if (openingTag != null) {
              s.push(openingTag);
            } // end if
          } //end else
        } // end if
      } //end for

    } //end while
    while (s.size() > 0) {
      // Check for any extra tags that haven't been closed.
      openingTag = s.pop();
      errorOutput += "\nError \n";
      errorOutput += "-------------------";
      errorOutput += openingTag + " Was not closed\n";
      errorOutput += "-------------------\n";
    } // end while

    if (errorOutput.length() == 0) {
      // If there were no errors, we congratulate the user. Kudos!
      errorOutput += "No errors found! Well done!";
    }

    String[] outputArray = {HTMLOutput, errorOutput};
    // return the HTMLOutput and errorOutput to the user.
    return outputArray;
  }
}
