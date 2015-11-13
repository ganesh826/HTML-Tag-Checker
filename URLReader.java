
import java.net.*;
import java.io.*;
import java.util.Stack;

public class URLReader {

    public static void main(String[] args) throws Exception {

        URL oracle = new URL("http://ubuntuforums.org/index.php");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        Stack<String> s = new Stack<String>();

        String closingTag;
        String openingTag;
        String inputLine;
        boolean spaceReached;
        boolean selfClosing;
        char[] inputArray;
        String[] selfClosingTagArray = {"<area>", "<base>",  "<br>", "<br/>", "<col>", 
          "<embed>", "<hr>","<hr/>", "<img>", "<input>", "<keygen>", "<link>", 
          "<menuitem>", "<meta>", "<param>", "<source>", "<track>", "<wbr>"};
        /*
         Self closing tags aka void elements taken from W3 Schools:
          >  http://www.w3.org/html/wg/drafts/html/master/syntax.html#void-elements
        */


        int counter = 0;
        PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
        while ((inputLine = in.readLine()) != null) { // Go through each line
          writer.println(inputLine);

          counter++;
          closingTag = "";
          openingTag = "";
          
          inputArray = inputLine.toCharArray(); 
          // I found it easier to iterate through an array of characters to look for tags.

          for (int i = 0; i < inputArray.length; i++) {

            if (inputArray[i] == '<') {
              if (inputArray[i + 1] == '/') {
                openingTag = s.pop();
                // pop off the opening tag for comparrison
                closingTag += "<"; 
                // closing tag is stored without the '/' so it can be easily compared to the opening.

                for (int j = i+2; j < inputArray.length; j++) {
                  // Since the first two characters, "</", are accounted for, we start at the third
                    closingTag += inputArray[j];

                  if (inputArray[j] == '>') {
                    break;
                  } //end if

                } // end for


                // Check the last opening tag after a closing tag has been found 
                // This section still needs some work
                if (closingTag.equals(openingTag)) {
                  //System.out.println(openingTag + " -- " + closingTag);
                  //System.out.println("------SUCCESS-----");
                  //System.out.println(openingTag + " -- " + closingTag);
                  //System.out.println("-------------------\n");
                  closingTag = "";
                  openingTag = "";
                  // Reset the tags to allow for mutiple tags per row
                } // end if
                else {
                  System.out.println("\nError Line " + counter +"....");
                  // I don't think the line counter works...
                  System.out.println("-------------------");
                  System.out.println(openingTag + " -- " + closingTag);
                  System.out.println("-------------------\n");
                } // end else

              } //end if


              else {
                // else, it must be an opening tag.

                openingTag += "<";
                spaceReached = false;
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

                      selfClosing = false;

                      for (String tag : selfClosingTagArray) {
                        // Check if the opening tag is a self-closing tag
                        if (openingTag.equals(tag)){
                          selfClosing = true;
                        } // end if
                      } // end for

                      if (!selfClosing) {
                        // Validate that the tag is not self closing before pushing
                        s.push(openingTag);
                        //System.out.println(openingTag);
                        // Reset the tags to allow for mutiple tags per row
                      } //end if
                      openingTag = "";
                      closingTag = "";


                      break;
                  } //end if
                  else if (inputArray[j] == '>' && openingTag.charAt(1)=='!') {
                      openingTag = "";
                      closingTag = "";
                      break;
                  }

                  else if (!spaceReached) {
                    // If it's not the end of the tag and a space has not been found, 
                    // the string is updated with the new character

                    openingTag += inputArray[j];
                  }  //end if
                } // end for
              } //end else
            } // end if

          } //end for

        } //end while

        writer.close();


        /*
         a better way to do this would be to pop a div when you get a /div 
         because it would let you keep better track of both the html doc's structure
         and your position on the page. All this logic tells us is if there is an incorrect number
         of opening and closing div tags. It might be useful though idk 
         The code below might work..mostly just so i can remember my thinking lol
         
         
         while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("div"))
                s.push("div");
            if(inputLine.contains("/div"))
                s2.pop();
            System.out.println(inputLine); // This could be erased if you don't want to see the html in console
        }
         

        Your old code ##############

        String inputLine;
        Stack s = new Stack();
        Stack s2 = new Stack();
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("div"))
                s.push("div");
            if(inputLine.contains("/div"))
                s2.push("/div");
            System.out.println(inputLine);
        }
        try {
            while(!s.empty()||!s2.empty()) {
                System.out.println(s.pop()+" ------ "+s2.pop());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ----------------------------------------------------------------------------------------

          All right, finally got a chance to dive into this. A couple things I notice right away:

          1) The point of using the stack would be to compare the closing tags with 
          the last pushed opening tag and make sure they match. For example, you
          find a closing </div>, and you pop off the stack to make sure an opening
          <div> was the last thing pushed. Since we don't need to store the closing value, 
          we should be able to get away with just one stack.

          2) The way you're locating the tags is going to cause problems later on.
          We should design the program to find tags regardless of their text. The
          most obvious way to do this is to look for the angle brackets <>.

          My above code seems to be working for a normal html file. There are some 
          edge cases we'll have to take into account. It's throwing a lot of errors
          for big sites like Twitter, but working for basic html files. I'm a beer
          too far in to fix it right now, but we're at least moving forward.


         */
    }
}
