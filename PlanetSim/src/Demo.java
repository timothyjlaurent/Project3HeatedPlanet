import controllers.CommandLineController;

public class Demo {

	public static void main(final String[] args) {
		try {
			// TODO Simulation & Presentation
			CommandLineController.parse(args);
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
