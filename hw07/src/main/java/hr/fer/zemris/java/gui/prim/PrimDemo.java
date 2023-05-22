package hr.fer.zemris.java.gui.prim;


import javax.swing.*;
import java.awt.*;

public class PrimDemo extends JFrame{

    public PrimDemo() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(200, 200);
        initGUI();
    }
    private void initGUI() {
        Container cp = getContentPane();

        PrimListModel primListModel = new PrimListModel();
        cp.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JScrollPane scrollPane1 = new JScrollPane(new JList<>(primListModel));
        panel.add(scrollPane1, BorderLayout.EAST);
        JScrollPane scrollPane2 = new JScrollPane(new JList<>(primListModel));
        panel.add(scrollPane2, BorderLayout.WEST);

        cp.add(panel, BorderLayout.CENTER);


        JButton next = new JButton("next");
        next.addActionListener(e -> primListModel.next());
        cp.add(next, BorderLayout.PAGE_END);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new PrimDemo().setVisible(true);
        });
    }
}
