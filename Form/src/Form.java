import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Form {

    JFrame window;

    JTextField name;
    JComboBox age;
    JTextArea address;
    ButtonGroup group;
    JRadioButton male, female;
    JCheckBox sport, music, literature,other;

    GridBagConstraints c;
    GridBagConstraints b;


    public Form(){

        window = new JFrame("Новая анкета");

        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        b = new GridBagConstraints();
        b.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        window.add(getJMenuBar(), c);

        c.gridx = 0; c.gridy = 1;
        c.insets = new Insets(5,20,0,20);
        window.add(Name_Panel(), c);

        c.gridx = 0; c.gridy = 2;
        c.insets = new Insets(5,0,0,0);
        window.add(Center_Panel(), c);

        c.gridx = 0; c.gridy = 3;
        c.insets = new Insets(0,20,0,20);
        window.add(Address_Panel(), c);

        c.gridx = 0; c.gridy = 4;
        c.insets = new Insets(0,0,15,0);
        window.add(Button_Panel(), c);

        window.pack();
    }

    private JMenuBar getJMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Меню");
        JMenu server = new JMenu("Настройки");
        menuBar.add(menu);
        menuBar.add(server);

        return menuBar;
    }

    private JPanel Name_Panel()
    {
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Имя: "));
        name = new JTextField(27);
        panel1.add(name);

        return panel1;
    }

    private JPanel Center_Panel()
    {
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());

        b.gridx = 0; b.gridy = 0;
        panel2.add(new JLabel("Возраст: "), b);
        b.insets = new Insets(0,60,10,0);
        panel2.add(getJComboBox(), b);

        b.gridx = 0; b.gridy = 1;
        b.insets = new Insets(0,0,0,0);
        panel2.add(new JLabel("Пол: "), b);

        male = new JRadioButton("муж", false);
        female = new JRadioButton("жен", false);
        group = new ButtonGroup();
        group.add(male);
        group.add(female);

        b.gridx = 0; b.gridy = 2;
        b.insets = new Insets(0,0,0,115);
        panel2.add(male, b);
        b.gridx = 0; b.gridy = 3;
        panel2.add(female, b);

        b.gridx = 1; b.gridy = 0;
        b.insets = new Insets(0,70,0,0);
        panel2.add(new JLabel("Интересы: "), b);

        b.gridx = 1; b.gridy = 1;
        sport = new JCheckBox("Спорт");
        panel2.add(sport, b);
        b.gridx = 1; b.gridy = 2;
        music = new JCheckBox("Музыка");
        panel2.add(music, b);
        b.gridx = 1; b.gridy = 3;
        literature = new JCheckBox("Литература");
        panel2.add(literature, b);
        b.gridx = 1; b.gridy = 4;
        other = new JCheckBox("Прочее");
        panel2.add(other, b);

        return panel2;
    }

    private JPanel Address_Panel()
    {
        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());

        b.gridx = 0; b.gridy = 0;
        b.insets = new Insets(5,5,5,5);
        panel3.add(new JLabel("Адрес: "), b);

        b.gridx = 0; b.gridy = 1;
        b.insets = new Insets(5,5,10,5);
        address = new JTextArea(5,30);
        panel3.add(address, b);

        return panel3;
    }

    private JPanel Button_Panel() {
        JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());

        b.gridx = 0; b.gridy = 0;
        b.insets = new Insets(0,110,0,0);
        JButton clear = new JButton("Сброс");
        panel4.add(clear, b);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setText(null);
                address.setText(null);
                age.setSelectedIndex(0);
                group.clearSelection();
                sport.setSelected(false);
                music.setSelected(false);
                literature.setSelected(false);
                other.setSelected(false);
            }
        });


        b.gridx = 1; b.gridy = 0;
        b.insets = new Insets(0,20,0,0);
        JButton sent = new JButton("Отправить");
        panel4.add(sent, b);
        sent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                if (!name.getText().equals("")) {
                    System.out.println("Наш пользователь " + name.getText());
                    System.out.println("Возраст " + age.getSelectedItem().toString());
                    System.out.println("Адрес " + address.getText());
                }

            }
        });

        return panel4;
    }

    private JComboBox getJComboBox() {
        age = new JComboBox();

        age.addItem(" ");
        age.addItem("0-18");
        age.addItem("18-25");
        age.addItem("25-40");
        age.addItem(">40");

        return age;
    }


    public void open() {
        window.setVisible(true);
    }
}

class Main {

    public static void main(String[] args) {
        Form form = new Form();
        form.open();
    }

}
