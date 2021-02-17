package cs340.getpet.pages;

import cs340.getpet.PageTemplate;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class AnimalSearch extends PageTemplate {
    private static final long serialVersionUID = 1L;

    public AnimalSearch(final PageParameters parameters) {
        super(parameters);

        add(new SearchForm("searchForm"));
    }

    @Override
    protected String getPageTitle() {
        return "Animal Search";
    }

    public static class SearchForm extends Form<Void> {
        RadioChoice<String> species;
        CheckBox male, female;
        TextField<String> breed;
        CheckBox black, white, brown, gold, dGray, lGray;
        CheckBox small, medium, large;

        public SearchForm(String id) {
            super(id);

            add(species = new RadioChoice<>("species", Model.of(""), List.of("Dog", "Cat")));
            add(male = new CheckBox("male", Model.of(false)));
            add(female = new CheckBox("female", Model.of(false)));
            add(breed = new TextField<>("breed", Model.of("")));
            add(black = new CheckBox("black", Model.of(false)));
            add(white = new CheckBox("white", Model.of(false)));
            add(brown = new CheckBox("brown", Model.of(false)));
            add(gold = new CheckBox("gold", Model.of(false)));
            add(dGray = new CheckBox("dGray", Model.of(false)));
            add(lGray = new CheckBox("lGray", Model.of(false)));
            add(small = new CheckBox("small", Model.of(false)));
            add(medium = new CheckBox("medium", Model.of(false)));
            add(large = new CheckBox("large", Model.of(false)));
        }

        public final void onSubmit() {
            System.out.println(species.getValue());
            System.out.println(gold.getValue());
            System.out.println(breed.getValue());
        }
    }
}
