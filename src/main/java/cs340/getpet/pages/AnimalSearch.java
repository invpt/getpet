package cs340.getpet.pages;

import cs340.getpet.PageTemplate;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AnimalSearch extends PageTemplate {
    private static final long serialVersionUID = 1L;

    public AnimalSearch(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected String getPageTitle() {
        return "Animal Search";
    }
}
