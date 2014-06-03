package pipe.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @param <D> table row datum item
 */
public abstract class AbstractComponentTableModel<D extends AbstractDatum> extends AbstractTableModel {

    /**
     * Number of non-empty rows
     */
    protected int count = 0;

    /**
     * Maximum number of items to be shown
     */
    protected static final int DATA_SIZE = 100;


    /**
     * Data in the table once it has been modified.
     */
    protected final List<D> modifiedData = new ArrayList<>();


    /**
     * Data that has been deleted from the table
     */
    protected final Collection<D> deletedData = new HashSet<>();

    /**
     * Names of columns in table to appear in order
     */
    protected String[] columnNames;


    @Override
    public final String getColumnName(int col) {
        return columnNames[col];
    }


    /**
     *
     * @param row
     * @param col
     * @return true for all cells
     */
    @Override
    public final boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public final int getRowCount() {
        return modifiedData.size();
    }

    @Override
    public final int getColumnCount() {
        return columnNames.length;
    }

    /**
     * @param datum datum to check in model
     * @return true if the row being edited is an existing component in the Petri net
     */
    protected final boolean isExistingDatum(D datum) {
        return datum.initial != null;
    }

    public final List<D> getTableData() {
        return modifiedData;
    }

    public final void deleteRow(int row) {
        if (isExistingDatum(modifiedData.get(row))) {
            deletedData.add(modifiedData.get(row));
            count--;
        }
        modifiedData.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public final Collection<D> getDeletedData() {
        return deletedData;
    }

    @Override
    public final void setValueAt(Object value, int rowIndex, int colIndex) {
        D datum = modifiedData.get(rowIndex);

        updateTableAt(value, rowIndex, colIndex);

        if (!isExistingDatum(datum) && !datum.id.isEmpty()) {
            count++;
        }
        fireTableCellUpdated(rowIndex, colIndex);
    }

    /**
     *
     * Inherited method for updating the actual item
     *
     * @param value
     * @param rowIndex
     * @param colIndex
     */
    protected abstract void updateTableAt(Object value, int rowIndex, int colIndex);

}
