package cs340.getpet.panels;

import cs340.getpet.data.Animal;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class SearchResultPanel extends Panel {
    public SearchResultPanel(String id, Animal animal) {
        super(id);

        add(new Label("name", animal.name));
        add(new Label("species", animal.species.toString()));
        add(new Label("gender", animal.gender.toString()));
        add(new Label("breed", animal.breed));
        add(new Label("color", animal.colors[0].toString()));
        add(new Label("size", animal.size.toString()));
    }
}
