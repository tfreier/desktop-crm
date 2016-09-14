/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import net.combase.desktopcrm.domain.AbstractCrmObject;

/**
 * @author "Till Freier"
 *
 */
public class ActionRequiredTableModel extends AbstractTableModel
{
    /**
     * 
     */
    private static final long serialVersionUID = -3890791456083674319L;

    private static final String[] COLUMN_NAMES = new String[] { "Title", "Owner", "" };

    private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, String.class, JButton.class };

    private final List<AbstractCrmObject> data;

    public ActionRequiredTableModel(List<AbstractCrmObject> data)
    {
	super();
	this.data = data;
    }

    public void update(List<AbstractCrmObject> tasks)
    {
	data.clear();
	data.addAll(tasks);

	fireTableDataChanged();
    }

    @Override
    public String getColumnName(int columnIndex)
    {
	return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
	return COLUMN_TYPES[columnIndex];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
	/* Adding components */
	AbstractCrmObject task = data.get(rowIndex);
	switch (columnIndex)
	{
	case 0:
	    return task.getTitle();
	case 1:
	    return task.getAssignedUser();
	case 2:
	    return createViewButton(task);

	default:
	    return "Error";
	}
    }

    private JButton createViewButton(final AbstractCrmObject task)
    {
	final JButton button = new JButton();
	button.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		DesktopUtil.openBrowser(task.getViewUrl());
	    }
	});

	button.setBackground(new Color(90, 115, 255, 100));
	switch (task.getCrmEntityType())
	{
	case "Cases":
	    button.setIcon(CrmIcons.WARN);
	    break;
	case "Leads":
	case "Contacts":
	case "Accounts":
	case "Prospects":
	    button.setIcon(CrmIcons.USER);
	    break;
	case "Opportunities":
	    button.setIcon(CrmIcons.DOLLAR);
	    break;
	default:
	    button.setIcon(CrmIcons.VIEW);
	    break;
	}
	button.setToolTipText("View...");

	return button;
    }

    @Override
    public int getRowCount()
    {
	return data.size();
    }

    @Override
    public int getColumnCount()
    {
	return COLUMN_NAMES.length;
    }

    public List<AbstractCrmObject> getData()
    {
	return new ArrayList<AbstractCrmObject>(data);
    }

}
