package com.github.lwhite1.tablesaw.reducing;

import com.github.lwhite1.tablesaw.api.BooleanColumn;
import com.github.lwhite1.tablesaw.api.CategoryColumn;
import com.github.lwhite1.tablesaw.api.DateColumn;
import com.github.lwhite1.tablesaw.api.FloatColumn;
import com.github.lwhite1.tablesaw.api.IntColumn;
import com.github.lwhite1.tablesaw.api.ShortColumn;
import com.github.lwhite1.tablesaw.api.Table;
import com.github.lwhite1.tablesaw.columns.Column;
import com.google.common.collect.TreeBasedTable;

import java.time.LocalDate;

/**
 * Utilities for creating frequency and proportion cross tabs
 */
public final class CrossTab {

  public static Table xCount(Table table, CategoryColumn column1, CategoryColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, CategoryColumn column1, IntColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, CategoryColumn column1, ShortColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, BooleanColumn column1, BooleanColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, BooleanColumn column1, CategoryColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, BooleanColumn column1, FloatColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, CategoryColumn column1, BooleanColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, FloatColumn column1, FloatColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, DateColumn column1, DateColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, DateColumn column1, BooleanColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, BooleanColumn column1, DateColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, CategoryColumn column1, DateColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, DateColumn column1, CategoryColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, DateColumn column1, FloatColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, CategoryColumn column1, FloatColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, FloatColumn column1, DateColumn column2) {
    return xTabCount(table, column1, column2);
  }

  public static Table xCount(Table table, FloatColumn column1, CategoryColumn column2) {
    return xTabCount(table, column1, column2);
  }

  /**
   * Returns a table containing two-dimensional cross-tabulated counts for each combination of values in
   * {@code column1} and {@code column2}
   * <p>
   * @param table     The table we're deriving the counts from
   * @param column1   A column in {@code table}
   * @param column2   Another column in {@code table}
   * @return          A table containing the cross-tabs
   */
  public static Table xTabCount(Table table, Column column1, Column column2) {

    Table t = new Table("Crosstab Counts: " + column1.name() + " x " + column2.name());
    t.addColumn(CategoryColumn.create(""));

    Table temp = table.sortOn(column1.name(), column2.name());

    int colIndex1 = table.columnIndex(column1.name());
    int colIndex2 = table.columnIndex(column2.name());

    com.google.common.collect.Table<String, String, Integer> gTable = TreeBasedTable.create();
    String a;
    String b;

    for (int row : temp) {
      a = temp.column(colIndex1).getString(row);
      b = temp.column(colIndex2).getString(row);
      Integer cellValue = gTable.get(a, b);
      Integer value = 0;
      if (cellValue != null) {
        value = cellValue + 1;
      }
      gTable.put(a, b, value);
    }

    for (String colName : gTable.columnKeySet()) {
      t.addColumn(IntColumn.create(colName));
    }

    t.addColumn(IntColumn.create("total"));

    int[] columnTotals = new int[t.columnCount()];

    for (String rowKey : gTable.rowKeySet()) {
      t.column(0).addCell(rowKey);

      int rowSum = 0;

      for (String colKey : gTable.columnKeySet()) {
        Integer cellValue = gTable.get(rowKey, colKey);
        if (cellValue != null) {
          int colIdx = t.columnIndex(colKey);
          t.intColumn(colIdx).add(cellValue);
          rowSum += cellValue;
          columnTotals[colIdx] = columnTotals[colIdx] + cellValue;

        } else {
          t.intColumn(colKey).add(0);
        }
      }
      t.intColumn(t.columnCount() - 1).add(rowSum);
    }
    t.column(0).addCell("Total");
    int grandTotal = 0;
    for (int i = 1; i < t.columnCount() - 1; i++) {
      t.intColumn(i).add(columnTotals[i]);
      grandTotal = grandTotal + columnTotals[i];
    }
    t.intColumn(t.columnCount() - 1).add(grandTotal);
    return t;
  }

  public static Table xTabCount(Table table, DateColumn column1, Column column2) {

    Table t = new Table("CrossTab Counts");
    t.addColumn(CategoryColumn.create("value"));
    Table temp = table.sortOn(column1.name(), column2.name());

    int colIndex2 = table.columnIndex(column2.name());

    com.google.common.collect.Table<LocalDate, String, Integer> gTable = TreeBasedTable.create();

    LocalDate a;
    String b;

    for (int row : temp) {
      a = temp.dateColumn(column1.name()).get(row);
      b = temp.column(colIndex2).getString(row);
      Integer cellValue = gTable.get(a, b);
      Integer value = 0;
      if (cellValue != null) {
        value = cellValue + 1;
      }
      gTable.put(a, b, value);
    }

    for (String colName : gTable.columnKeySet()) {
      t.addColumn(FloatColumn.create(colName));
    }

    t.addColumn(FloatColumn.create("total"));

    int[] columnTotals = new int[t.columnCount()];

    for (LocalDate rowKey : gTable.rowKeySet()) {
      t.dateColumn(0).add(rowKey);

      int rowSum = 0;

      for (String colKey : gTable.columnKeySet()) {
        Integer cellValue = gTable.get(rowKey, colKey);
        if (cellValue != null) {
          int colIdx = t.columnIndex(colKey);
          t.intColumn(colIdx).add(cellValue);
          rowSum += cellValue;
          columnTotals[colIdx] = columnTotals[colIdx] + cellValue;

        } else {
          t.intColumn(colKey).add(0);
        }
      }
      t.intColumn(t.columnCount() - 1).add(rowSum);
    }
    t.column(0).addCell("Total");
    int grandTotal = 0;
    for (int i = 1; i < t.columnCount() - 1; i++) {
      t.intColumn(i).add(columnTotals[i]);
      grandTotal = grandTotal + columnTotals[i];
    }
    t.intColumn(t.columnCount() - 1).add(grandTotal);
    return t;
  }

/*
  public static Table xTabCount(Table table, String column1) {
    return Table.groupApply(table, column1, StaticUtils::count, column1);
  }
*//*


  public static Table xApply(Table table,
                             String groupColumnName,
                             String valueColumnName,
                             Function<FloatColumn, Double> fun) {
    return Table.groupApply(table, valueColumnName, fun, groupColumnName);
  }

  private CrossTab() {
  }

  public static Table tablePercents(Table xTabCounts) {

    Table pctTable = new Table("Proportions");
    CategoryColumn labels = CategoryColumn.create("labels");

    pctTable.addColumn(labels);

    for (int i = 0; i < xTabCounts.rowCount(); i++) {
      labels.add(xTabCounts.column(0).getString(i));
    }

    for (int i = 1; i < xTabCounts.columnCount(); i++) {
      Column column = xTabCounts.column(i);
      pctTable.addColumn(FloatColumn.create(column.name()));
    }

    long tableTotal
        = (long) xTabCounts.column(xTabCounts.columnCount() - 1).get(xTabCounts.rowCount() - 1);

    for (int i = 0; i < xTabCounts.rowCount(); i++) {
      Row row = xTabCounts.getRow(i);
      Row newRow = pctTable.getRow(i);

      for (int c = 1; c < xTabCounts.columnCount(); c++) {
        newRow.set(c, (long) (row.get(c)) / (double) tableTotal);
      }
    }
    return pctTable;
  }
*/

  public static Table rowPercents(Table xTabCounts) {

    Table pctTable = new Table("Crosstab Row Proportions: ");
    CategoryColumn labels = CategoryColumn.create("");

    pctTable.addColumn(labels);

    for (int i = 0; i < xTabCounts.rowCount(); i++) {
      labels.add(xTabCounts.column(0).getString(i));
    }

    for (int i = 1; i < xTabCounts.columnCount(); i++) {
      Column column = xTabCounts.column(i);
      pctTable.addColumn(FloatColumn.create(column.name()));
    }

    for (int i = 0; i < xTabCounts.rowCount(); i++) {
      float rowTotal = (float) xTabCounts.intColumn(xTabCounts.columnCount() - 1).get(i);

      for (int c = 1; c < xTabCounts.columnCount(); c++) {
        if (rowTotal == 0) {
          pctTable.floatColumn(c).add(Float.NaN);
        } else {
          pctTable.floatColumn(c).add((float) xTabCounts.intColumn(c).get(i) / rowTotal);
        }
      }
    }
    return pctTable;
  }

  public static Table tablePercents(Table xTabCounts) {

    Table pctTable = new Table("Crosstab Table Proportions: ");
    CategoryColumn labels = CategoryColumn.create("");

    pctTable.addColumn(labels);

    int grandTotal = xTabCounts.intColumn(xTabCounts.columnCount() - 1).get(xTabCounts.rowCount() - 1);

    for (int i = 0; i < xTabCounts.rowCount(); i++) {
      labels.add(xTabCounts.column(0).getString(i));
    }

    for (int i = 1; i < xTabCounts.columnCount(); i++) {
      Column column = xTabCounts.column(i);
      pctTable.addColumn(FloatColumn.create(column.name()));
    }

    for (int i = 0; i < xTabCounts.rowCount(); i++) {
     // float rowTotal = (float) xTabCounts.intColumn(xTabCounts.columnCount() - 1).get(i);

      for (int c = 1; c < xTabCounts.columnCount(); c++) {
        if (grandTotal == 0) {
          pctTable.floatColumn(c).add(Float.NaN);
        } else {
          pctTable.floatColumn(c).add((float) xTabCounts.intColumn(c).get(i) / grandTotal);
        }
      }
    }
    return pctTable;
  }
}