package cs340.getpet.panels;

import cs340.getpet.data.Animal;
import cs340.getpet.pages.Details;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SearchResultPanel extends Panel {

    final Link<Void> details =  new BookmarkablePageLink<Void>("details", Details.class){
        @Override
        protected void onComponentTag(ComponentTag tag){
            super.onComponentTag(tag);
            tag.put("target","_blank");
        }
    };
    public SearchResultPanel(String id, Animal animal) {
        super(id);

        final Link<Void> details =  new BookmarkablePageLink<Void>("details", Details.class){
            @Override
            protected void onComponentTag(ComponentTag tag){
                super.onComponentTag(tag);
                tag.put("target","_blank");
            }
        };

        add(new Label("name", animal.name));
        add(new Label("gender", animal.gender.toString()));
        add(new Label("breed", animal.breed));
        // TODO: display colors as readable string in the same way
        // TODO: as in AnimalSearch
        add(new Label("color", Arrays.stream(animal.colors).map(Enum::toString).collect(Collectors.joining(", "))));
        add(new Label("size", animal.size.toString()));

        //add(new Button("details"));
        add(details);
    }
}
