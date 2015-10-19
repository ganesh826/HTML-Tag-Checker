
import java.net.*;
import java.io.*;
import java.util.Stack;

public class URLReader {

    public static void main(String[] args) throws Exception {

        URL oracle = new URL("https://www.twitter.com");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

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
         
         */
        try {
            while(!s.empty()||!s2.empty()) {
                System.out.println(s.pop()+" ------ "+s2.pop());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
