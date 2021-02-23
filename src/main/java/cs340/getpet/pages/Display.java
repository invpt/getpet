package cs340.getpet.pages;

import cs340.getpet.GetPetApplication;
import cs340.getpet.PageTemplate;
import cs340.getpet.Persistence;
import cs340.getpet.data.*;
import cs340.getpet.panels.SearchResultPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.*;

public class Display extends PageTemplate {

    public Display(final PageParameters parameters) {
        super(parameters);
        
        add(new Label("animalName"));
        add(new Label("animalSpecies"));
        add(new Label("animalBreed"));
        add(new Label("animalSize"));
        add(new Label("animalColor"));
        add(new Label("animalGender"));
    }

    @Override
    protected String getPageTitle(){
        return "Animal Details";
    }
}
