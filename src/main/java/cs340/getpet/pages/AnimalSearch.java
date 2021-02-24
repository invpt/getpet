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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class AnimalSearch extends PageTemplate {
    private static final long serialVersionUID = 1L;

    final Logger logger = LoggerFactory.getLogger(getClass());
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

        TextField<String> breed;

        Map<String, Species> speciesMap = linkedMapOfEntries(
                entry("Dog", Species.Dog),
                entry("Cat", Species.Cat)
        );
        RadioChoice<String> species;

        Map<String, Gender> genderMap = linkedMapOfEntries(
                entry("Male", Gender.Male),
                entry("Female", Gender.Female)
        );
        CheckBoxMultipleChoice<String> gender;

        CheckBoxMultipleChoice<Color> color;

        Map<String, Size> sizeMap = linkedMapOfEntries(
                entry("Small", Size.Small),
                entry("Medium", Size.Medium),
                entry("Large", Size.Large)
        );
        CheckBoxMultipleChoice<String> size;

        public SearchForm(String id) {
            super(id);

            breed = new TextField<>("breed", Model.of());
            add(breed);

            species = new RadioChoice<>("species", Model.of(), new ArrayList<>(speciesMap.keySet()));
            species.setRequired(true);
            species.setSuffix(CHOICE_SUFFIX);
            add(species);

            gender = new CheckBoxMultipleChoice<>("gender", Model.of(), new ArrayList<>(genderMap.keySet()));
            gender.setSuffix(CHOICE_SUFFIX);
            add(gender);

            color = new CheckBoxMultipleChoice<>("colors", Model.of(), List.of(Color.Black, Color.White, Color.Brown, Color.Gold, Color.DarkGray, Color.LightGray));
            color.setSuffix(CHOICE_SUFFIX);
            add(color);

            size = new CheckBoxMultipleChoice<>("size", Model.of(), new ArrayList<>(sizeMap.keySet()));
            size.setSuffix(CHOICE_SUFFIX);
            add(size);
        }

        public final void onSubmit() {
            // Delete previously found search results (WICKET SAVES STATE?!?!?)
            searchResults.stream().forEach(component -> searchResults.remove(component));

            try {
                Persistence.AnimalSearchQuery.Builder queryBuilder = new Persistence.AnimalSearchQuery.Builder()
                        .species(speciesMap.get(species.getModel().getObject()))
                        .genders(gender
                                .getModel()
                                .getObject()
                                .stream()
                                .map(s -> genderMap.get(s))
                                .collect(Collectors.toList()))
                        .colors(color.getModel().getObject())
                        .sizes(size
                                .getModel()
                                .getObject()
                                .stream()
                                .map(s -> sizeMap.get(s))
                                .collect(Collectors.toList()));

                if (breed.getModel().getObject() != null)
                    queryBuilder.breed(new Persistence.DatabaseObject<>(breed.getModel().getObject()));

                Persistence.AnimalSearchResults results = GetPetApplication
                        .getPersistence()
                        .findAnimal(queryBuilder.build());

                // display search result
                for (Animal animal = results.next(); animal != null; animal = results.next())
                    searchResults.add(new SearchResultPanel(searchResults.newChildId(), animal));
            } catch (Persistence.PersistenceException e) {
                logger.error("Error while executing search", e);
            }
        }

        // called on form validation error (e.g. required field not present)
        public final void onError() {
            // TODO: error page
            logger.info("Form failed to validate");
        }

        @SafeVarargs
        private <K, V> Map<K, V> linkedMapOfEntries(Map.Entry<K, V>... entries) {
            LinkedHashMap<K, V> map = new LinkedHashMap<>(entries.length);

            for (Map.Entry<K, V> entry : entries)
                map.put(entry.getKey(), entry.getValue());

            return map;
        }
    }
}
