package cs340.getpet.pages;

import cs340.getpet.GetPetApplication;
import cs340.getpet.PageTemplate;
import cs340.getpet.Persistence;
import cs340.getpet.data.*;
import cs340.getpet.panels.SearchResultPanel;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;
import java.util.stream.Collectors;

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

    class SearchForm extends Form<Void> {
        private static final String CHOICE_SUFFIX = "<br />";
        
        RadioChoice<String> species;
        CheckBoxMultipleChoice<String> gender;
        TextField<String> breed;
        CheckBoxMultipleChoice<String> color;
        CheckBoxMultipleChoice<String> size;

        public SearchForm(String id) {
            super(id);

            species = new RadioChoice<>("species", Model.of(""), List.of("Dog", "Cat"));
            species.setSuffix(CHOICE_SUFFIX);
            add(species);

            gender = new CheckBoxMultipleChoice<>("gender", Model.of(), List.of("Male", "Female"));
            gender.setSuffix(CHOICE_SUFFIX);
            add(gender);

            breed = new TextField<>("breed", Model.of(""));
            add(breed);

            color = new CheckBoxMultipleChoice<>("colors", Model.of(), List.of("Black", "White", "Brown", "Gold", "Dark Gray", "Light Gray"));
            color.setSuffix(CHOICE_SUFFIX);
            add(color);

            size = new CheckBoxMultipleChoice<>("size", Model.of(), List.of("Small", "Medium", "Large"));
            size.setSuffix(CHOICE_SUFFIX);
            add(size);
        }

        public final void onSubmit() {
            searchResults.stream().forEach(component -> searchResults.remove(component));

            try {
                Persistence.AnimalSearchQuery query = new Persistence.AnimalSearchQuery.Builder()
                        .species(Species.fromFormRepresentation(
                                species.getModel().getObject()))
                        .breed(new Persistence.DatabaseObject<>(
                                breed.getModel().getObject() == null ? "" : breed.getModel().getObject()))
                        .genders(gender
                                .getModel()
                                .getObject()
                                .stream()
                                .map(Gender::fromFormRepresentation)
                                .collect(Collectors.toList()))
                        .colors(color
                                .getModel()
                                .getObject()
                                .stream()
                                .map(Color::fromFormRepresentation)
                                .collect(Collectors.toList()))
                        .sizes(size
                                .getModel()
                                .getObject()
                                .stream()
                                .map(Size::fromFormRepresentation)
                                .collect(Collectors.toList()))
                        .build();

                Persistence.AnimalSearchResults results = GetPetApplication.getPersistence().findAnimal(query);

                // display search result
                for (Animal animal = results.next(); animal != null; animal = results.next())
                    searchResults.add(new SearchResultPanel(searchResults.newChildId(), animal));
            } catch (Persistence.PersistenceException e) {
                // oh god we have to implement an error page
                e.printStackTrace();
            }
        }
    }
}
