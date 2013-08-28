/**
 * 
 */
package org.mwc.cmap.core.property_support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class DoubleHelper extends EditorHelper
{
	Object _previousValue;

	public DoubleHelper()
	{
		super(Double.class);
	}

	@SuppressWarnings({ "rawtypes" })
	public boolean editsThis(final Class target)
	{
		return ((target == Double.class) || (target == double.class));
	}

	public Object translateToSWT(final Object value)
	{
		_previousValue = value;
		return "" + value;
	}

	public Object translateFromSWT(final Object value)
	{
		Object res;

		// just do a quick check that it needs converting
		if (value instanceof Double)
		{
			res = value;
		}
		else
		{
			try
			{
				res = Double.valueOf((String) value);
			}
			catch (final NumberFormatException e)
			{
				res = _previousValue;
			}
		}

		return res;
	}

	public CellEditor getCellEditorFor(final Composite parent)
	{
		final TextCellEditor res = new TextCellEditor(parent);
		
		return res;
	}

}