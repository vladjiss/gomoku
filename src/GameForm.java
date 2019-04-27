import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameForm extends JFrame {
    private JPanel _mainField;
    private JPanel _board;
    private JPanel _header;
    private JLabel text;
    public boolean lock = true;
    public boolean first = false;
    private IGomokuService server;

    public GameForm(IGomokuService _server) {
        super("ГОМОКУ");
        server = _server;
        setSize(600, 623);
        setResizable(false);
        Container main = getContentPane();
        setBackground(Color.orange);
        _mainField = new JPanel(new FlowLayout(FlowLayout.CENTER));
        _board = new JPanel();
        _header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        text = new JLabel("");
        _header.add(text);
        _header.setBackground(Color.orange);
        _board.setBackground(Color.orange);
        _mainField.setBackground(Color.orange);

        GridLayout grid = new GridLayout(19,19,0,0);
        grid.preferredLayoutSize(_mainField);

        _board.setLayout(grid);

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                makeButton(i, j);
            }
        }
        _mainField.add(_board);

        main.add(_header, BorderLayout.NORTH);
        main.add(_mainField, BorderLayout.SOUTH);
        setVisible(true);
    }



    void makeButton(int i, int j){
        JButton button = new JButton();//JButton(name);
        button.setPreferredSize(new Dimension(30,30));
        _board.add(button, CENTER_ALIGNMENT);
        SetCircle setCircle = new SetCircle(i, j);
        button.addActionListener(setCircle);
    }

    private class SetCircle implements ActionListener{
        private int _row;
        private int _col;
        public SetCircle(int i, int j){
            _row = i;
            _col = j;
        }
        public void actionPerformed(ActionEvent event)
        {
            if(!lock) {
                Color color;
                if(first)
                    color = Color.WHITE;
                else
                    color = Color.BLACK;
                addCircle(color, _row, _col);
                lock = true;
                try {
                    server.SendMessageToClient("step "+first+" "+_row+" "+_col);
                }
                catch (Exception e) {

                }
            }
        }
    }

    public void addCircle(Color color, int _row, int _col){
        _board.remove(_row * 19 + _col);
        _board.add(new MyCircle(color), _row * 19 + _col);
        revalidate();
        repaint(_row * 30, _col * 30, 30, 30);
    }

    public class MyCircle extends JPanel {
        private Color color;
        public MyCircle(Color _color){
            color = _color;
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setSize(30, 30);
            g.setColor(color);
            g.fillOval(0, 0, 30, 30);
        }
    }

    public void SetText(String txt){
        text.setText(txt);
    }

    public void Alert(String text){
        JOptionPane.showMessageDialog(null, text, "Сообщение", 1);
    }

}