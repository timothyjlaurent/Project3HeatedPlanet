import java.awt.Dimension;

import javax.swing.JFrame;

import models.CommandLineParam;
import views.PresentationView;
import controllers.CommandLineController;
import dao.DatabaseDaoSqlImpl;

public class Demo {

	public static void main(final String[] args) {
		try {
			final CommandLineParam params = CommandLineController.parse(args);
			final PresentationView view = new PresentationView(params, new DatabaseDaoSqlImpl());
			view.setName("Heat Planet Simulation");
			view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			view.setPreferredSize(new Dimension(1200, 700));
			view.setMaximumSize(new Dimension(1200, 700));
			view.setResizable(false);
			view.pack();
			view.setVisible(true);
		} catch (final Exception e) {
			System.err.println(e);
		}
	}

}
