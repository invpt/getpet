package cs340.getpet;

import cs340.getpet.pages.AnimalSearch;
import cs340.getpet.pages.Login;
import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the application class. It's the big daddy of everything else.
 */
public class GetPetApplication extends WebApplication
{
	static final String DATABASE_NAME = "getpet";
	private static Persistence persistence;

	final Logger logger = LoggerFactory.getLogger(GetPetApplication.class);

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return Login.class;
	}

	/**
	 * This is where to put the code that is run on startup.
	 */
	@Override
	public void init() {
		super.init();

		// Allow loading of resources.
		getCspSettings().blocking()
				// ... our own stylesheets
				.add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF)
				// ... google fonts
				.add(CSPDirective.STYLE_SRC, "https://fonts.googleapis.com/css2")
				// ... and still google fonts
				.add(CSPDirective.FONT_SRC, "https://fonts.gstatic.com");

		// create database connection
		logger.info("Setting up persistent storage...");
		try {
			persistence = new Persistence("localhost", DATABASE_NAME, "root", null);
			logger.info("Persistent storage setup complete.");
		} catch (Persistence.PersistenceException e) {
			logger.error("Failed to initialize persistent storage!", e);
			// TODO: is there a way to more gracefully exit a WicketApplication?
			System.exit(1);
		}

		// mount each page to its respective URL; this is where we define
		// the paths of each page in the application.
		mountPage("/search", AnimalSearch.class);
	}

	public static Persistence getPersistence() {
		return persistence;
	}
}
