//uses java Swing gui tools
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Stack;

public class URLReader {

    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        p.Parse();
        JFrame frame = new GUI();
        frame.setTitle("HTMLReader");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }


}


