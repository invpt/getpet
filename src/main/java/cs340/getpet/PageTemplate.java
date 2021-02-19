package cs340.getpet;

import cs340.getpet.panels.HeaderPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The base template for all of the pages in the application. Each page should
 * extend this class. See AnimalSearch and AnimalSearch.html for an example.
 */
public abstract class PageTemplate extends WebPage {
    HeaderPanel headerPanel;

    public PageTemplate(final PageParameters parameters) {
        super(parameters);

        // add header panel
        headerPanel = new HeaderPanel("header", LambdaModel.of(this::getPageTitle));
        add(headerPanel);

        // set page title based on what the extending class's title is
        add(new Label("title", LambdaModel.of(this::getPageTitle)));
    }

    /**
     * Gets the title for a page.
     * <p>
     * This method is used to determine the title of the browser tab.
     *
     * @return The page title
     */
    protected abstract String getPageTitle();
}
