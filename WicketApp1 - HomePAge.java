/*
 * HomePage.java
 *
 * Created on 2015/02/28, 1:49
 */
package com.myapp.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;

public class HomePage extends BasePage {

    public HomePage() {
        add(new Label("message", "Hello, World!"));

        String text
                = "\nThis is a line.\n"
                + "And this is another line.\n"
                + "End of lines.\n";

        add(new MultiLineLabel("multiLineLabel", text){
            public boolean isVisible(){return true;};
        });
        

        add(new ExternalLink("externalLink1", "http://www.javalobby.org", "To JavaLobby"));

        Contact contact = null;
        ArrayList list = new ArrayList();

        char character;

        // a - z
        for (int i = 97; i < 123; i++) {
            character = (char) i;
            contact = new Contact(String.valueOf(character));
            list.add(contact);
        }
        final DataView dataView = new DataView("simple", new ListDataProvider(list)) {
            @Override
            public void populateItem(final Item item) {
                final Contact user = (Contact) item.getModelObject();
                item.add(new Label("id", user.getId()));
            }
        };

        dataView.setItemsPerPage(10);

        add(dataView);

        add(new PagingNavigator("navigator", dataView));

        Label markupLabel = new Label("markupLabel", "now <i>that</i> is a pretty <b>bold</b> statement!");
        markupLabel.setEscapeModelStrings(false);
        add(markupLabel);
    }

    class Contact implements Serializable {

        private final String id;

        public Contact(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

}
