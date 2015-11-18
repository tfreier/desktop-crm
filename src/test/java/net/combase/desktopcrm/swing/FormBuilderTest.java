package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.combase.desktopcrm.domain.Lead;

import org.formbuilder.Form;
import org.formbuilder.FormBuilder;
import org.formbuilder.mapping.beanmapper.SampleBeanMapper;
import org.formbuilder.mapping.beanmapper.SampleContext;

public class FormBuilderTest
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				JFrame f = new JFrame("test");
				JPanel main = new JPanel();
				main.setLayout(new BorderLayout());
				final Form<Lead> form = FormBuilder.map(Lead.class)
					.with(new SampleBeanMapper<Lead>()
					{

						@Override
						protected JComponent mapBean(Lead sample, SampleContext<Lead> ctx)
						{
							final Box main = Box.createVerticalBox();

							Box box = Box.createHorizontalBox();
							box.add(ctx.label(sample.getFirstname()));
							box.add(ctx.editor(sample.getFirstname()));
							main.add(box);

							box = Box.createHorizontalBox();
							box.add(ctx.label(sample.getLastName()));
							box.add(ctx.editor(sample.getLastName()));

							main.add(box);

							return main;
						}
					})
					.buildForm();

				main.add(form.asComponent(), BorderLayout.CENTER);
				JButton save = new JButton("save");
				save.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						Lead value = form.getValue();

						System.out.println("firstname: " + value.getFirstname());
						System.out.println("lastname: " + value.getLastName());
					}
				});

				main.add(save, BorderLayout.SOUTH);

				form.setValue(new Lead(null, ""));

				f.getContentPane().add(main);
				f.pack();
				f.setVisible(true);
			}
		});

	}
}
