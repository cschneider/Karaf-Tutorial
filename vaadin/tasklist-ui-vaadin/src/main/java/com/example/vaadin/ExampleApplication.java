package com.example.vaadin;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
class ExampleApplication extends Application {

    ExampleApplication() {
    }

    @Override
    public void init() {
        final GridLayout layout = new GridLayout(1, 1);
        setMainWindow(new Window("Example", layout));
        final TextField text = new TextField("Text");
        layout.addComponent(text);
        Button button = new Button("Send Log Message");
        layout.addComponent(button);

        new Table("Tasks", dataSource);
        
        button.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Sending " + text.getValue()));
            }
        });
    }

}
