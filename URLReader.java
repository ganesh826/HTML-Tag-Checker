
import java.net.*;
import java.io.*;
import java.util.Stack;

public class URLReader {

    public static void main(String[] args) throws Exception {

        URL oracle = new URL("http://localhost/test.html");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        Stack<String> s = new Stack<String>();

        String closingTag;
        String openingTag;
        String inputLine;
        boolean spaceReached;
        char[] inputArray;

        while ((inputLine = in.readLine()) != null) {
          closingTag = "";
          openingTag = "";
          inputArray = inputLine.toCharArray();
          for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] == '<') {
              if (inputArray[i + 1] == '/') {
                closingTag += "</";
                for (int j = i+2; j < inputArray.length; j++) {
                  if (inputArray[j] == '>') {
                    closingTag += inputArray[j];
                    break;
                  }
                  else {
                    closingTag += inputArray[j];
                  }

                }
              }
              else {
                openingTag += "<";
                spaceReached = false;
                for (int j = i+1; j < inputArray.length; j++) {
                  if (inputArray[j] == ' ') {
                    spaceReached = true;
                  }
                  if (inputArray[j] == '>') {
                    openingTag += inputArray[j];
                    s.push(openingTag);
                    break;
                  }
                  if (!spaceReached) {
                    openingTag += inputArray[j];
                  } 
                }
              }
            }

          }


        }

        while (!s.isEmpty()) {
          System.out.println(s.pop());
        }



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



         */
    }
}
