import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

    String[] FirstColumn = new String[] {
            "Количество исполнителей ",
            "Количество задач ",
            "Производительность исполнителя ",
            "Сложность задачи "};

    Integer [][] data;

    public MyTableModel(Integer[][] inform){
        data = new Integer[4][2];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                data[i][j] = inform[i][j];
            }
        }
    }
    @Override
    public int getRowCount() {
        return 4;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int c) {
        String result = "";
        if (c == 1)
            result = "Min";
        if (c == 2)
            result = "Max";
        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return ((columnIndex > 0) && (columnIndex <= 2));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try
        {
            Integer value = Integer.valueOf((String) aValue);
            data[rowIndex][columnIndex - 1] = value;
            fireTableCellUpdated (rowIndex, columnIndex);
        }
        catch (NumberFormatException nfe) { }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return FirstColumn[rowIndex];
            case 1:
                return data[rowIndex][0];
            case 2:
                return data[rowIndex][1];
            default:
                return " ";
        }
    }
}
