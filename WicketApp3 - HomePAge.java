/*
 * HomePage.java
 *
 * Created on 2015/03/01, 23:39
 */
package com.myapp.wicket;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class HomePage extends BasePage {

    private Model sampleText;
    private Model sampleLabel;

    public HomePage() {
        add(new Label("message", "Hello, World!"));

        List list = Arrays.asList(new String[]{"xxx", "yyy", "zzz"});
        add(new DropDownChoice("combo", new Model((Serializable) list)) {

            void onSelectionChange() {

            }
        ;
        });
        
        Form form = new Form("form", new Model());
        add(form);

        sampleText = new Model();
        form.add(new TextField("sampletext", new PropertyModel(this, "sampleText")));

        sampleLabel = new Model();
        form.add(new Label("samplelabel", new PropertyModel(this, "sampleLabel")) {
            @Override
            public boolean isVisible() {
                return true;
            }
        });

        Button submitButton = new Button("submit", new Model()) {
            @Override
            public void onSubmit() {
                sampleLabel.setObject(sampleText.getObject());
            }
        };
        form.add(submitButton);
    }

}
