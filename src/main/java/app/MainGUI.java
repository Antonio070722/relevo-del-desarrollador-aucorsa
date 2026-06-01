package app;

import controller.MainController;
import view.MainView;

public class MainGUI {
    public static void main(String[] args){
        MainView mainView = new MainView();
        new MainController(mainView);
        mainView.setVisible(true);
    }
}
