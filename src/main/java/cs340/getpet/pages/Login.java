package cs340.getpet.pages;

import cs340.getpet.PageTemplate;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class Login extends PageTemplate {
	private static final long serialVersionUID = 1L;

	public Login(final PageParameters parameters) {
		super(parameters);

		// currently the login page has no functionality, so we just
		// redirect straight to the animal search page.
		throw new RestartResponseException(AnimalSearch.class);
	}

	@Override
	protected String getPageTitle() {
		return "Login";
	}
}
