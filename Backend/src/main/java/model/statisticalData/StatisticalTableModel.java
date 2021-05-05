package model.statisticalData;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class StatisticalTableModel implements TableModel {

    private DataTransferObject data;

    public StatisticalTableModel(DataTransferObject data) {
        this.data = data;
    }

    @Override
    public void addTableModelListener(TableModelListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Class<?> getColumnClass(int j) {
        return data.getColumnClass(j);
    }

    @Override
    public int getColumnCount() {
        return data.getColumnCount();
    }

    @Override
    public String getColumnName(int j) {
        return data.getColumnName(j);
    }

    @Override
    public int getRowCount() {
        return data.getRowCount();
    }

    @Override
    public Object getValueAt(int i, int j) {
        return data.getValueAt(i, j);
    }

    @Override
    public boolean isCellEditable(int i, int j) {
        return false;
    }

    @Override
    public void removeTableModelListener(TableModelListener arg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValueAt(Object arg, int i, int j) {
        // TODO Auto-generated method stub

    }

}
