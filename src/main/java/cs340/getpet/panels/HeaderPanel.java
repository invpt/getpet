package cs340.getpet.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class HeaderPanel extends Panel {
    public HeaderPanel(String id, IModel<String> title) {
        super(id);

        add(new Label("title", title));
    }
}
