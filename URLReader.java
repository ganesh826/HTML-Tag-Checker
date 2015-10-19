
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
        try {
            while(!s.empty()||!s2.empty()) {
                System.out.println(s.pop()+" ------ "+s2.pop());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}