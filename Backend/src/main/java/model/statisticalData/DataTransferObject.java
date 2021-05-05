package model.statisticalData;

import javax.swing.table.AbstractTableModel;
import java.io.Serializable;

public class DataTransferObject extends AbstractTableModel implements Serializable {

    private static String[] colHeads = {"Context", "#Friends", "Duration"};
    private static Class<?>[] types = {String.class, Integer.class, Integer.class};
    /**
     * The indices of those columns that contain numerical data that can be aggregated
     */
    private static Integer[] numberTypes = {1, 2};
    /**
     * The indices of those columns that contain data that is to be updated in the aggregation
     * row.
     */
    private static Integer[] updatables = {};

    private Object[][] data;

    /* The Jadex Framework needs this parameterless constructor to be able to serialize
     * DataTransferObjects in a message content
     */
    public DataTransferObject() {
        super();
    }

    /* Should be used as the default constructor, even though this class offers a parameterless one*/
    public DataTransferObject(Object[][] data) {
        this();
        this.data = data;
    }

    public static String[] getColHeads() {
        return colHeads;
    }

    public static void setColHeads() {
        colHeads = colHeads;
    }

    public static Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        DataTransferObject.types = types;
    }

    public static Integer[] getNumberTypes() {
        return numberTypes;
    }

    public static void setNumberTypes(Integer[] numberTypes) {
        numberTypes = numberTypes;
    }

    @Override
    public String getColumnName(int j) {
        return colHeads[j];
    }

    @Override
    public Class<?> getColumnClass(int j) {
        return types[j];
    }

    @Override
    public int getColumnCount() {
        return colHeads.length;
    }

    public Integer[] getUpdatables() {
        return updatables;
    }

    public static void setUpdatables(Integer[] updatables) {
        updatables = updatables;
    }

    @Override
    public Object getValueAt(int i, int j) {
        return data[i][j];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
        fireTableStructureChanged();
    }

    @Override
    public boolean isCellEditable(int i, int j) {
        return false;
    }

    @Override
    public void setValueAt(Object arg, int i, int j) {
        // forget it!
    }

    /**
     * This method facilitates data aggregation by adding the data of another
     * DataTransferObject to the aggregation row of the DataTransferObject this method
     * is invoked on. The data row of the other DataTransferObject always has the index '0' whereas
     * the aggregation row always has the index '1'.
     */
    public void addDataTransferObject(DataTransferObject single) {
        // First Step: Update the Elements in the 'lastRound' row
        for (int j = 0; j < data[0].length; j++) {
            data[0][j] = single.getData()[0][j];
        }
        // Second Step: Aggregate the Numeric Data (Numeric Data are marked by use of
        // the static number-types array. The data aggregation takes places in the second row
        // of a DataTransferObject and uses the data of the first row of another DataTransferObject
        for (Integer j : getNumberTypes()) {
            if (data[1][j] instanceof Integer)
                data[1][j] = ((Integer) data[1][j]) + ((Integer) single.getData()[0][j]);
            else if (data[1][j] instanceof Double)
                data[1][j] = ((Double) data[1][j]) + ((Double) single.getData()[0][j]);
        }
        // Third Step: Update those entries in the data aggregation row that are marked as
        // updatable in the static updatable array
        for (Integer j : getUpdatables()) {
            data[2][j] = single.getData()[1][j];
        }
        // At the end all Table Model listeners are to be informed of the changing of the
        // data in the aggregation row
        fireTableDataChanged();
    }

}
