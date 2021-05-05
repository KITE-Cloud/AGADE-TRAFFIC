package model.statisticalData;

public class DataTransferPool extends DataTransferObject {
    private Object[][] data;

    public DataTransferPool() {

    }

    public DataTransferPool(Object[][] data) {
        this.data = data;
    }

    public void addDataTransferSingle(DataTransferSingle single) {
        for (int i = 0; i < data.length; i++) {
            for (Integer j : getNumberTypes()) {
                if (data[i][j] instanceof Integer)
                    data[i][j] = ((Integer) data[i][j]) + ((Integer) single.getData()[i][j]);
                else if (data[i][j] instanceof Double)
                    data[i][j] = ((Double) data[i][j]) + ((Double) single.getData()[i][j]);
            }
            for (Integer j : getUpdatables()) {
                data[i][j] = single.getData()[i][j];
            }
        }
    }

    @Override
    public Object getValueAt(int i, int j) {
        return data[i][j];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object[][] getData() {
        return data;
    }

    @Override
    public void setData(Object[][] data) {
        this.data = data;
    }
}
