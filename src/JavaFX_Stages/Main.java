package JavaFX_Stages;
import Buildings.Building;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    public final int GAME_WIDTH = 1200;
    public final int GAME_HEIGHT = 490;

    public final double CLICK_WINDOW_TRANSLATE_X = -GAME_WIDTH/2.3;
    public final double CLICK_WINDOW_TRANSLATE_Y = -GAME_HEIGHT/2.5;

    public final double BUILDINGS_WINDOW_TRANSLATE_X = GAME_WIDTH/2.5;
    public final double BUILDINGS_WINDOW_TRANSLATE_Y = -GAME_HEIGHT/2.5;

    public long AMOUNT_OF_COINS = 0;
    public long COINS_PER_SECOND = 0;
    public long GAIN_PER_CLICK = 1;



    Text textClicks, textCoinsPerSecond, farmCost, farmAmount;
    ImageView ivCoin;
    Image imageCoin;

    Building Farm = new Building("Farm", "images/farm.png", 10, 2, 0);

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Elemental Clicker");
        primaryStage.setResizable(false);

        StackPane root = new StackPane();
        root.setId("background-neutral");

        Scene mainScene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        mainScene.getStylesheets().add(Main.class.getResource("/images/styles.css").toExternalForm());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                refreshGUI();
            }
        }, 1000, 10);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                clickPerSecond();
            }
        }, 1000, 1000);

        Button buttonClick = new Button();
        buttonClick.setText("Click");
        buttonClick.setId("shiny-orange");
        buttonClick.setTranslateX(0);
        buttonClick.setTranslateY(GAME_HEIGHT/2.3);
        buttonClick.setPrefSize(GAME_WIDTH/1.1,GAME_HEIGHT/9);
        buttonClick.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.5 * buttonClick.getPrefHeight())));
        buttonClick.setOnAction((event) -> {
            click();
        });



        Image imageBackgroundClicks = new Image("images/background_clicks.png");
        ImageView iv2 = new ImageView();
        iv2.setImage(imageBackgroundClicks);
        iv2.setFitWidth(145);
        iv2.setPreserveRatio(true);
        iv2.setTranslateX(CLICK_WINDOW_TRANSLATE_X);
        iv2.setTranslateY(CLICK_WINDOW_TRANSLATE_Y);
        iv2.setSmooth(true);
        iv2.setCache(true);

        ImageView ivCoinsPerSecond = new ImageView();
        ivCoinsPerSecond.setImage(imageBackgroundClicks);
        ivCoinsPerSecond.setFitWidth(145);
        ivCoinsPerSecond.setFitHeight(50);
        ivCoinsPerSecond.setTranslateX(iv2.getTranslateX());
        ivCoinsPerSecond.setTranslateY(iv2.getTranslateY()+ivCoinsPerSecond.getFitHeight()+20);
        ivCoinsPerSecond.setSmooth(true);
        ivCoinsPerSecond.setCache(true);


        textClicks = new Text(String.valueOf(AMOUNT_OF_COINS));
        textClicks.setTranslateX(iv2.getTranslateX());
        textClicks.setTranslateY(iv2.getTranslateY());
        textClicks.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.3 * iv2.getFitWidth())));

        textCoinsPerSecond = new Text(String.valueOf(COINS_PER_SECOND) + " / s");
        textCoinsPerSecond.setTranslateX(ivCoinsPerSecond.getTranslateX());
        textCoinsPerSecond.setTranslateY(ivCoinsPerSecond.getTranslateY());
        textCoinsPerSecond.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.2 * ivCoinsPerSecond.getFitWidth())));

        imageCoin = new Image("images/coin.png");
        ivCoin = new ImageView();
        ivCoin.setImage(imageCoin);
        ivCoin.setTranslateY(textClicks.getTranslateY());
        ivCoin.setTranslateX(textClicks.getTranslateX()-48);
        ivCoin.setFitWidth(25);
        ivCoin.setPreserveRatio(true);
        ivCoin.setSmooth(true);
        ivCoin.setCache(true);

        Button buttonShop = new Button();
        buttonShop.setText("Shop");
        buttonShop.setId(buttonClick.getId());
        buttonShop.setTranslateX(ivCoinsPerSecond.getTranslateX());
        buttonShop.setTranslateY(ivCoinsPerSecond.getTranslateY()+buttonShop.getHeight()+70);


        Button buttonOptions = new Button();
        buttonOptions.setText("Options");
        buttonOptions.setId(buttonClick.getId());
        buttonOptions.setTranslateX(buttonShop.getTranslateX());
        buttonOptions.setTranslateY(buttonShop.getTranslateY()+50);

        ImageView ivFarmBackground = new ImageView(imageBackgroundClicks);
        ivFarmBackground.setTranslateY(BUILDINGS_WINDOW_TRANSLATE_Y);
        ivFarmBackground.setTranslateX(BUILDINGS_WINDOW_TRANSLATE_X);
        ivFarmBackground.setFitWidth(250);
        ivFarmBackground.setFitHeight(75);
        ivFarmBackground.toBack();
        ivFarmBackground.setSmooth(true);
        ivFarmBackground.setCache(true);

        ivFarmBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            buy(Farm);
            event.consume();
        });



        ImageView ivFarm = new ImageView(new Image("images/farm.png"));
        ivFarm.setTranslateX(ivFarmBackground.getTranslateX()-75);
        ivFarm.setTranslateY(ivFarmBackground.getTranslateY()+3);
        ivFarm.toFront();
        ivFarm.setFitWidth(ivFarmBackground.getFitWidth()/3);
        ivFarm.setPreserveRatio(true);
        ivFarm.setSmooth(true);
        ivFarm.setCache(true);

        Text farmText = new Text(Farm.getName());
        farmText.setTranslateY(ivFarm.getTranslateY()-20);
        farmText.setTranslateX(ivFarm.getTranslateX()+80);
        farmText.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.1 * ivFarmBackground.getFitWidth())));


        farmCost = new Text(String.valueOf(Farm.getCost()));
        farmCost.setTranslateY(farmText.getTranslateY()+30);
        farmCost.setTranslateX(farmText.getTranslateX()-15);
        farmCost.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.1 * ivFarmBackground.getFitWidth())));

        farmAmount = new Text(String.valueOf(Farm.getAmount()));
        farmAmount.setTranslateY(ivFarm.getTranslateY()-5);
        farmAmount.setTranslateX(ivFarm.getTranslateX()+160);
        farmAmount.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.3 * ivFarmBackground.getFitWidth())));




        root.getChildren().addAll(buttonClick, iv2, textClicks, ivCoin, ivFarmBackground, ivFarm, buttonShop, buttonOptions, ivCoinsPerSecond, textCoinsPerSecond, farmCost, farmText, farmAmount);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    void buy(Building b){
        if(b.getCost() > AMOUNT_OF_COINS) return;
        AMOUNT_OF_COINS-= b.getCost();
        b.setAmount(b.getAmount()+1);
        COINS_PER_SECOND+=b.getCoinsPerSecond();
    }

    void click(){
        AMOUNT_OF_COINS+=GAIN_PER_CLICK;
    }

    void clickPerSecond(){
        AMOUNT_OF_COINS+=COINS_PER_SECOND;
    }


    void refreshGUI(){
        textClicks.setText(String.valueOf(AMOUNT_OF_COINS));
        textCoinsPerSecond.setText(String.valueOf(COINS_PER_SECOND) + " /s");
        farmCost.setText(String.valueOf((int)Farm.getCost()));
        farmAmount.setText(String.valueOf(Farm.getAmount()));
    }

    Image crop(Image image, int x, int y, int width, int height){
        PixelReader reader = image.getPixelReader();
        WritableImage newImage = new WritableImage(reader, x, y, width, height);
        return newImage;
    }



    public static void main(String[] args) {
        launch(args);
    }
}