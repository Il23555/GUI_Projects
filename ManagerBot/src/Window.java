import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Window {

    JFrame window;
    JButton New, Pause, Settings;
    JList<String> list1, list2, list3, list4;
    DefaultListModel listModel1, listModel2, listModel3, listModel4;
    JDialog dialog;
    GridBagConstraints c;

    RoundRobin roundRobin;
    Integer[][] data;
    int period;

    public Window()
    {
        data = new Integer[][]{{2,2},{7,7},{2,10},{5,50}};
        period = 10;

        window = new JFrame("Round Robin");
        window.setSize(1250,400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.getContentPane().setLayout(new GridBagLayout());
        window.getContentPane().setBackground(Color.white);

        c = new GridBagConstraints();

        createLabels();
        createLists();
        createButtons();
    }

    private void createLabels()
    {
        JLabel label1 = new JLabel("Исполнители: ");
        JLabel label2 = new JLabel("Текущие задачи: ");
        JLabel label3 = new JLabel("Задачи исполнителя: ");
        JLabel label4 = new JLabel("Выполненные задачи: ");

        Font font = new Font(Font.SERIF,14,20);
        label1.setFont(font); label2.setFont(font);
        label3.setFont(font); label4.setFont(font);

        c.gridy = 0;
        c.insets = new Insets(0,20,0,100);
        c.gridx = 0; window.add(label1, c);
        c.gridx = 1; window.add(label2, c);
        c.insets = new Insets(0,0,0,45);
        c.gridx = 2; window.add(label3, c);
        c.gridx = 3; window.add(label4, c);
    }

    private void createLists()
    {
        listModel1 = new DefaultListModel(); list1 = new JList(listModel1);
        listModel2 = new DefaultListModel(); list2 = new JList(listModel2);
        listModel3 = new DefaultListModel(); list3 = new JList(listModel3);
        listModel4 = new DefaultListModel(); list4 = new JList(listModel4);

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Font font = new Font(Font.SERIF,30,16);
        list1.setFont(font); list1.setLayoutOrientation(JList.VERTICAL);
        list2.setFont(font); list2.setLayoutOrientation(JList.VERTICAL);
        list3.setFont(font); list3.setLayoutOrientation(JList.VERTICAL);
        list4.setFont(font); list4.setLayoutOrientation(JList.VERTICAL);

        c.gridy = 1;
        c.insets = new Insets(10,20,20,10);
        c.gridx = 0; window.add(new JScrollPane(list1), c);
        c.insets = new Insets(10,0,20,40);
        c.gridx = 1; window.add(new JScrollPane(list2), c);
        c.insets = new Insets(10,20,20,20);
        c.gridx = 2; window.add(new JScrollPane(list3), c);
        c.gridx = 3; window.add(new JScrollPane(list4), c);

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = list1.getSelectedIndex();
                list2.setSelectedIndex(index);
                roundRobin.getAllTasks(listModel3, index);
            }
        });
        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                list1.setSelectedIndex(list2.getSelectedIndex());
            }
        });
    }

    private void createButtons()
    {
        Settings = new JButton("Settings");
        New = new JButton("New");
        Pause = new JButton("Pause");

        Font font = new Font(Font.SERIF,1,16);
        Settings.setFont(font);
        New.setFont(font);
        Pause.setFont(font);

        c.gridy = 2; c.gridx = 0;
        c.insets = new Insets(15,0,20,150); window.add(Settings, c);
        c.gridx = 3;
        c.insets = new Insets(15,0, 20,150); window.add(New, c);
        c.insets = new Insets(15,150,20, 0); window.add(Pause, c);

        New.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list1.getSelectionModel().clearSelection();
                list2.getSelectionModel().clearSelection();
                listModel1.clear(); listModel4.clear();

                roundRobin = new RoundRobin(data);

                roundRobin.getUsers(listModel1);
                roundRobin.getCurrentTasks(listModel2);
                roundRobin.startTimer(period, listModel2, listModel3, listModel4);
            }
        });

        Settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog = getJDialog();
            }
        });

        Pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roundRobin.timer.cancel();
            }
        });

    }

    private JDialog getJDialog()
    {
        JDialog dialog = new JDialog(window,"Настройки",true);
        dialog.setSize(550,350);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Color.white);
        dialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel("Время срабатывания таймера (в секундах): ");
        JTextField textField = new JTextField("", 10);
        JButton sent = new JButton("Потвердить");
        JButton cancel = new JButton("Отмена");

        JTable table = new JTable(new MyTableModel(data)){
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(super.getPreferredSize().width,
                        getRowHeight() * getRowCount());
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(310);
        table.setRowHeight(23);

        Font font1 = new Font(Font.SERIF,20,19); label.setFont(font1);
        Font font2 = new Font(Font.SERIF,20,14); textField.setFont(font2);
        Font font3 = new Font(Font.SERIF,20,18); table.setFont(font3);
        Font font4 = new Font(Font.SERIF,1,16);
        sent.setFont(font4); cancel.setFont(font4);

        b.gridx = 0; b.gridy = 0;
        b.insets = new Insets(10,0,10,120); dialog.add(label, b);
        b.insets = new Insets(10,340,10,0); dialog.add(textField, b);
        b.gridy = 1;
        b.insets = new Insets(10,10,10,10);
        dialog.add(new JScrollPane(table), b);
        b.gridy = 2;
        b.insets = new Insets(15,40,10,0);  dialog.add(sent, b);
        b.insets = new Insets(15,300,10,0); dialog.add(cancel,b);

        sent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    period = Integer.valueOf(textField.getText());
                }
                catch (NumberFormatException nfe){ }

                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 2; j++) {
                        data[i][j] = (Integer) table.getValueAt(i,j+1);
                    }
                }
                System.out.println();
                dialog.dispose();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
        return dialog;
    }

    public void open() {
        window.setVisible(true);
    }
}