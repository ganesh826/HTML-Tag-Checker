import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by devon on 10/30/2015.
 */
public class GUI extends JFrame{
    public GUI(){
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame,"Welcome to HTML Project");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button1 = new JButton("Run ");

        JPanel panel = new JPanel();
        panel.add(button1);
        add(panel);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parser p = new Parser();
                try {
                    Parser.Parse();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            }
        });
    }
}
