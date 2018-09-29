import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.*;
 
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
 
public class Flappy extends Application{
   
    private ImageView bkg_img = null ;
    private ImageView birdImg = null ;
    private ImageView ground = null ;
    private Button    start_button = null;
    private Button    stop_button = null;
    private ImageView tubTop = null;
    private ImageView tubBottom = null;
    private Group     root = null;
    private TranslateTransition myAnimation = null;
    private Timeline tubeAnimation = null;
    private static boolean stop = false;
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Create a Group
        root = new Group();
        //Lab_1 code is here: add background, bird, ground and
        // "start" and "stop" button.
         start_button = new Button("Start");
        start_button.setLayoutX(150);
        start_button.setLayoutY(100);
        stop_button = new Button("Stop");
        stop_button.setLayoutX(300);
        stop_button.setLayoutY(100);
        bkg_img = new ImageView("background.png");
        bkg_img.setPreserveRatio(true);
        bkg_img.setLayoutX(0);
        bkg_img.setLayoutY(0);
        birdImg = new ImageView("flappy.png");
        birdImg.setLayoutX(30);
        birdImg.setLayoutY(40);
        ground = new ImageView ("ground.png");
        ground.setLayoutX(0);
        ground.setLayoutY(400*0.9);
        ground.setFitWidth(400*2);
        tubTop = new ImageView("obstacle_top.png");
        tubTop.setLayoutX(400);
        tubTop.setLayoutY(-200 + Math.random()*30);
        tubBottom = new ImageView("obstacle_bottom.png");
        tubBottom.setLayoutX(400);
        tubBottom.setLayoutY(300);
 
 
        //Add all nodes to the root.
 
        root.getChildren().add( bkg_img );
        root.getChildren().add( birdImg );
        root.getChildren().add(ground);
        root.getChildren().add(tubTop);
        root.getChildren().add(tubBottom);
        root.getChildren().add( start_button );
        root.getChildren().add( stop_button );
       
        //Create scene and add to stage
 
       
        primaryStage.setTitle(  "My first JavaFX Application");
 
       
        addActionEventHandler();
        addMouseEventHandler(); 
        setupTubeAnimation();
        addTubeListener();
        Scene scene = new Scene(root,400, 400);
        primaryStage.setScene(scene);  
        primaryStage.show();   
    }
 
    // Lab_2 code is here: implement translate animation for ground
    // and sequential animation for bird.
    public void movingGround(){
        if (!stop){
         myAnimation = new TranslateTransition(new Duration(2000), ground);
        myAnimation.setToX(-400);
        myAnimation.setInterpolator(Interpolator.LINEAR);
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.play();
        stop = true;
        }
    }  
    void flap(){
        TranslateTransition up = new TranslateTransition(new Duration(2000), birdImg);
        up.setByY(-50);
        TranslateTransition down = new TranslateTransition(new Duration(2000), birdImg);
        down.setToY(300);
        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(up, down);
        st.setCycleCount(1);
        st.play();
    }
 
   
    // Lab_3 and later.
    private void addActionEventHandler(){
       
        start_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
           
            public void handle(ActionEvent event) {
              // to do
           
                    movingGround();
                    tubeAnimation.play();
                    start_button.setVisible(false);
                    stop_button.setVisible(true);
                    stop = true;
               
               
                   
            }
           
        });
       
 
        stop_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
           
            public void handle(ActionEvent event) {
              // to do
                if (myAnimation != null){
                myAnimation.stop();
                }
                if (tubeAnimation != null){
                tubeAnimation.stop();
                }
                start_button.setVisible(false);
                stop_button.setVisible(false);
               
            }
        });
 
    }
   
               
 
   
    private void addMouseEventHandler(){
        root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            // to do   
               flap();          
            }
        });
    }  
    private void setupTubeAnimation() {
        KeyValue kv = new KeyValue(tubTop.translateXProperty(), -490);
        KeyValue kx = new KeyValue(tubBottom.translateXProperty(), -490);
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            tubTop.setLayoutY(-230 + java.lang.Math.random()*80);
            tubBottom.setLayoutY(220 + java.lang.Math.random()*80);    
 
            }
        };
        tubeAnimation = new Timeline();
       
        KeyFrame kframe = new KeyFrame(Duration.millis(10000), onFinished, kv, kx);
        tubeAnimation.setCycleCount(Timeline.INDEFINITE);
        tubeAnimation.getKeyFrames().add(kframe);
        
        }
       
    
public void checkCollision() {
    Bounds birdBounds = birdImg.localToScene(birdImg.getBoundsInLocal());
 
    Bounds tubeTopBounds =
 
    tubTop.localToScene(tubTop.getBoundsInLocal());
    Bounds tubeBottomBounds = tubBottom.localToScene(tubBottom.getBoundsInLocal());
 
    if(birdBounds.intersects(tubeTopBounds ) || birdBounds.intersects(tubeBottomBounds)){
       
tubeAnimation.stop();
myAnimation.stop();
stop_button.setVisible(false);
    }
}
public void addTubeListener() {
    DoubleProperty tubX = tubTop.translateXProperty();
 
    tubX.addListener(new ChangeListener<Number>() {
 
    public void changed(ObservableValue<? extends Number> ov,
    Number oldValue, Number newValue) {
        checkCollision();
        tubTop.translateXProperty();
        tubBottom.translateXProperty();
}
    });
}
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
            }