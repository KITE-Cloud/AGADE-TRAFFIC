package model.statisticalData;

public class DataTransferSingle extends DataTransferObject {

    private Object[][] data;

    public DataTransferSingle() {

    }

    public DataTransferSingle(Object[][] data) {
        this.data = data;
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
