package cs340.getpet.pages;

import cs340.getpet.GetPetApplication;
import cs340.getpet.PageTemplate;
import cs340.getpet.Persistence;
import cs340.getpet.data.*;
import cs340.getpet.panels.SearchResultPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class AnimalSearch extends PageTemplate {
    private static final long serialVersionUID = 1L;

    RepeatingView searchResults;

    public AnimalSearch(final PageParameters parameters) {
        super(parameters);

        add(new SearchForm("searchForm"));

        add(searchResults = new RepeatingView("result"));
    }

    @Override
    protected String getPageTitle() {
        return "Animal Search";
    }

    class SearchResult {

    }

    class SearchForm extends Form<Void> {
        private static final String CHOICE_SUFFIX = "<br />";
        
        RadioChoice<String> species;
        CheckBoxMultipleChoice<String> gender;
        TextField<String> breed;
        CheckBoxMultipleChoice<String> color;
        CheckBoxMultipleChoice<String> size;

        public SearchForm(String id) {
            super(id);

            add(species = new RadioChoice<>("species", Model.of(""), List.of("Dog", "Cat")));
            species.setSuffix(CHOICE_SUFFIX);
            add(gender = new CheckBoxMultipleChoice<>("gender", Model.of(), List.of("Male", "Female")));
            gender.setSuffix(CHOICE_SUFFIX);
            add(breed = new TextField<>("breed", Model.of("")));
            add(color = new CheckBoxMultipleChoice<>("colors", Model.of(), List.of("Black", "White", "Brown", "Gold", "Dark Gray", "Light Gray")));
            color.setSuffix(CHOICE_SUFFIX);
            add(size = new CheckBoxMultipleChoice<>("size", Model.of(), List.of("Small", "Medium", "Large")));
            size.setSuffix(CHOICE_SUFFIX);
        }

        public final void onSubmit() {
            try {
                Persistence.AnimalSearchQuery query = new Persistence.AnimalSearchQuery.Builder()
                        .species(Species.fromFormRepresentation(species.getDefaultModelObjectAsString()))
                        // .gender(Gender.fromFormRepresentation(gender.getDefaultModelObjectAsString())) ugh
                        .breed(new Persistence.DatabaseObject<>(breed.getDefaultModelObjectAsString()))
                        // .color(Color.fromFormRepresentation(color.getDefaultModelObjectAsString())) ugh
                        // .size(Size.fromFormRepresentation(size.getDefaultModelObjectAsString())) ugh
                        .build();
                Persistence.AnimalSearchResult result = GetPetApplication.getPersistence().findAnimal(query);

                ///////////////////////////////////// display the fucking result /////////////////////////////////////
                for (Animal animal = result.next(); animal != null; animal = result.next())
                    searchResults.add(new SearchResultPanel(searchResults.newChildId(), animal));
            } catch (Persistence.PersistenceException e) {
                // oh god we have to implement an error page
                e.printStackTrace();
            }
        }
    }
}
