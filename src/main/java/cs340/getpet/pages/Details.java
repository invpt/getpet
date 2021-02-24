package cs340.getpet.pages;

import cs340.getpet.GetPetApplication;
import cs340.getpet.PageTemplate;
import cs340.getpet.Persistence;
import cs340.getpet.data.*;
import cs340.getpet.panels.SearchResultPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class Details extends PageTemplate {

    public Details(final PageParameters parameters) {
        super(parameters);

        int intakeNumber = parameters.get("intakeNumber").toInt();
        try {
            Animal animal = GetPetApplication.getPersistence().getAnimal(intakeNumber);

            if (animal == null)
                throw new RuntimeException("Fuck");

            add(new Label("animalName", animal.name));
            add(new Label("animalSpecies", animal.species.toString()));
            add(new Label("animalBreed", animal.breed));
            add(new Label("animalSize", animal.size.toString()));
            add(new Label("animalColor", animal.colors[0].toString()));
            add(new Label("animalGender", animal.gender.toString()));
        } catch (Persistence.PersistenceException e) {
            System.out.println("Shit");
        }
    }

    @Override
    protected String getPageTitle(){
        return "Animal Details";
    }

    class DetailsForm extends Form<Void> {
        private static final String CHOICE_SUFFIX = "<br />";

        TextField<String> name;

        TextField<String> breed;

        NumberTextField<Double> weight;

        Map<String, Size> sizeMap = linkedMapOfEntries(
                entry("Small", Size.Small),
                entry("Medium", Size.Medium),
                entry("Large", Size.Large)
        );
        CheckBoxMultipleChoice<String> size;

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

        CheckBoxMultipleChoice<String> vaccinated;

        CheckBoxMultipleChoice<String> spayNeuter;

        public DetailsForm(String id, Animal animal) {
            super(id);

            name = new TextField<>("name", Model.of(animal.name));
            add(name);

            breed = new TextField<>("breed", Model.of(animal.breed));
            add(breed);

            weight = new NumberTextField<>("weight", Model.of(animal.weight));
            add(weight);

            size = new CheckBoxMultipleChoice<>("size", Model.of(/* TODO: Austin's an idiot */), new ArrayList<>(sizeMap.keySet()));
            size.setSuffix(CHOICE_SUFFIX);
            add(size);

            species = new RadioChoice<>("species", Model.of(), new ArrayList<>(speciesMap.keySet()));
            species.setRequired(true);
            species.setSuffix(CHOICE_SUFFIX);
            add(species);

            gender = new CheckBoxMultipleChoice<>("gender", Model.of(), new ArrayList<>(genderMap.keySet()));
            gender.setSuffix(CHOICE_SUFFIX);
            add(gender);

            //color = new CheckBoxMultipleChoice<>("colors", Model.of(), new ArrayList<>(colorMap.keySet()));
            //color.setSuffix(CHOICE_SUFFIX);
            //add(color);
        }

        public final void onSubmit() {
            // Delete previously found search results (WICKET SAVES STATE?!?!?)

        }

        // called on form validation error (e.g. required field not present)
        public final void onError() {
            // TODO: error page
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
