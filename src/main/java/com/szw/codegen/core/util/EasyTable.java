package com.szw.codegen.core.util;

import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * @author SZW
 * @date 2021/9/13
 */
public class EasyTable {
	private String title = "";
	private TableModel tableModel;
	private float fontSize = 18.0f;
	private int[] colWidths;

	public void showTable() {
		int rowHeight = (int) (fontSize * 1.5);
		int headerHeight = (int) (fontSize * 1.8);

		int width = 100;
		int height = headerHeight + rowHeight * tableModel.getRowCount ();

		JTable table = new JTable ();

		Font font = table.getFont ().deriveFont (fontSize);

		table.setModel (tableModel);
		table.setRowHeight (rowHeight);
		table.setAutoCreateRowSorter (true);
		table.setFont (font);
		table.setFillsViewportHeight (true);
		table.getTableHeader ().setFont (font);
		table.getTableHeader ().setPreferredSize (new Dimension (-1, headerHeight));

		if (colWidths != null && colWidths.length > 0) {
			TableColumnModel columnModel = table.getColumnModel ();
			for (int i = 0; i < table.getColumnCount (); i++) {
				columnModel.getColumn (i).setPreferredWidth (colWidths[i]);
			}

			for (int i = 0; i < tableModel.getColumnCount (); i++) {
				width += colWidths[i];
			}
		} else {
			width *= table.getColumnCount ();
		}

		JScrollPane scrollPane = new JScrollPane (table);

		JFrame frame = new JFrame ();
		frame.add (scrollPane, BorderLayout.CENTER);
		frame.setTitle (title);
		frame.setLocationByPlatform (true);
		frame.setDefaultCloseOperation (WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible (true);

		Insets insets = frame.getInsets ();
		Insets insets1 = scrollPane.getInsets ();
		frame.setSize (width, height + insets.top + insets.bottom + insets1.top + insets1.bottom);
	}

	public String getTitle() {
		return title;
	}

	public EasyTable setTitle(String title) {
		this.title = title;
		return this;
	}

	public float getFontSize() {
		return fontSize;
	}

	public EasyTable setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public int[] getColWidths() {
		return colWidths;
	}

	public EasyTable setColWidths(int[] colWidths) {
		this.colWidths = colWidths;
		return this;
	}

	public EasyTable setData(Object[] head, Object[][] body) {
		tableModel = new AbstractTableModel () {
			public String getColumnName(int column) {
				return head[column].toString ();
			}

			public int getRowCount() {
				return body.length;
			}

			public int getColumnCount() {
				return head.length;
			}

			public Object getValueAt(int row, int col) {
				return body[row][col];
			}
		};
		return this;
	}

	public <T> EasyTable setData(T[] data, Class<T> dataClass, Comparator<? super BeanPropUtil.Property<T>> sort) {
		List<BeanPropUtil.Property<T>> props = BeanPropUtil.getProperties (dataClass, true);
		if (sort != null) {
			props.sort (sort);
		}

		tableModel = new AbstractTableModel () {
			@Override
			public String getColumnName(int column) {
				return props.get (column).getName ();
			}

			@Override
			public int getRowCount() {
				return data.length;
			}

			@Override
			public int getColumnCount() {
				return props.size ();
			}

			@SneakyThrows
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return props.get (columnIndex).get (data[rowIndex]);
			}
		};
		return this;
	}

	public <T> EasyTable setData(T[] data, Class<T> dataClass) {
		return setData (data, dataClass, null);
	}

	public static void show(Object[] head, Object[][] body) {
		new EasyTable ().setData (head, body).showTable ();
	}

	public static <T> void show(T[] data, Class<T> dataClass) {
		new EasyTable ().setData (data, dataClass).showTable ();
	}
}
