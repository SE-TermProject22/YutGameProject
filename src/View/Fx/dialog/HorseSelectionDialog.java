package View.Fx.dialog;

import Model.DoubledHorse;
import Model.Horse;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.function.Consumer;

public class HorseSelectionDialog {

    public static void show(List<Horse> horses, int horseCount, Consumer<Horse> onSelected) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: transparent;");

        Image modalImage = new Image(HorseSelectionDialog.class.getResourceAsStream("/image/말 적용.png"));
        ImageView modalView = new ImageView(modalImage);
        root.getChildren().add(modalView);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setTranslateY(25);

        for (Horse horse : horses) {
            String imagePath;
            if (horse.id < 20) {
                imagePath = "/image/선택 " + horse.color + "/" + (horse.id % horseCount + 1) + ".png";
            } else {
                int imageType = ((DoubledHorse) horse).getImageType();
                String suffix;
                if (imageType == 0) suffix = "2개";
                else if (imageType == 1) suffix = "1개";
                else suffix = ((DoubledHorse) horse).horseCount + "개";
                imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + suffix + ".png";
            }

            Image image = new Image(HorseSelectionDialog.class.getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            btn.setOnAction(e -> {
                dialog.close();
                onSelected.accept(horse);
            });

            buttonBox.getChildren().add(btn);
        }

        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, 1100, 700);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.centerOnScreen();
        dialog.showAndWait();
    }
}

