package net.combase.desktopcrm.swing;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Task;

import java.awt.BorderLayout;
import java.util.List;

public class TaskPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5663155990640457849L;

	/**
	 * Create the panel.
	 */
	public TaskPanel() {
		setLayout(new BorderLayout(0, 0));
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode();
		DefaultMutableTreeNode today = new DefaultMutableTreeNode("Today");
		today.setAllowsChildren(true);
		
		compileTaskList(today);
		
		top.add(today);
		
		JTree tree = new JTree(top);
		add(tree);
	}
	
	private static void compileTaskList(DefaultMutableTreeNode node)
	{
		List<Task> taskList = CrmManager.getTaskList();
		
		for (Task task : taskList) {
			DefaultMutableTreeNode taskNode = new DefaultMutableTreeNode(task);
			taskNode.setAllowsChildren(false);
			node.add(taskNode);
		}
	}

}
