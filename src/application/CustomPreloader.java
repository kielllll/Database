package application;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomPreloader extends Preloader {

    private static final double WIDTH = 250;
    private static final double HEIGHT = 100;

    private Stage preloaderStage;
    private Scene scene;

    private Label progress;

    public CustomPreloader() {
        // Constructor is called before everything.
//        System.out.println(MyApplication.STEP() + "MyPreloader constructor called, thread: " + Thread.currentThread().getName());
    }

    @Override
    public void init() throws Exception {
//        System.out.println(MyApplication.STEP() + "MyPreloader#init (could be used to initialize preloader view), thread: " + Thread.currentThread().getName());

        // If preloader has complex UI it's initialization can be done in MyPreloader#init
        Platform.runLater(() -> {
            Label title = new Label("Setting resources");
            title.setTextAlignment(TextAlignment.CENTER);
            progress = new Label("0%");

            VBox root = new VBox(title, progress);
            root.setAlignment(Pos.CENTER);

            scene = new Scene(root, WIDTH, HEIGHT);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        System.out.println(MyApplication.STEP() + "MyPreloader#start (showing preloader stage), thread: " + Thread.currentThread().getName());

        this.preloaderStage = primaryStage;

        // Set preloader scene and show stage.
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.getIcons().add(new Image(getClass().getResourceAsStream("books.png")));
        preloaderStage.setScene(scene);
        preloaderStage.setResizable(false);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        // Handle application notification in this point (see MyApplication#init).
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                // Called after MyPreloader#start is called.
//                System.out.println(MyApplication.STEP() + "BEFORE_LOAD");
                break;
            case BEFORE_INIT:
                // Called before MyApplication#init is called.
//                System.out.println(MyApplication.STEP() + "BEFORE_INIT");
                break;
            case BEFORE_START:
                // Called after MyApplication#init and before MyApplication#start is called.
//                System.out.println(MyApplication.STEP() + "BEFORE_START");

                preloaderStage.hide();
                break;
        }
    }
}
