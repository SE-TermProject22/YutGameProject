package View.Fx.dialog;

import Controller.YutResult;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.function.Consumer;

public class FixedYutChoiceDialog {

    public static void show(Consumer<YutResult> onSelected) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(665, 298);
        root.setStyle("-fx-background-color: transparent");

        Image modalImage = new Image(FixedYutChoiceDialog.class.getResourceAsStream("/image/결과 적용.png"));
        ImageView modalImageView = new ImageView(modalImage);
        modalImageView.setFitWidth(665);
        modalImageView.setFitHeight(298);
        root.getChildren().add(modalImageView);

        YutResult[] fixedResults = {
                YutResult.DO, YutResult.GAE, YutResult.GEOL, YutResult.YUT, YutResult.MO, YutResult.BackDo
        };

        double x = 20;
        double y = 110;

        for (YutResult result : fixedResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            Image image = new Image(FixedYutChoiceDialog.class.getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            btn.setLayoutX(x);
            btn.setLayoutY(y);

            btn.setOnAction(e -> {
                dialog.close();
                onSelected.accept(result);
            });

            root.getChildren().add(btn);
            x += image.getWidth() + 20;
        }

        Scene scene = new Scene(root, 665, 298);
        scene.setFill(null);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private static String getKoreanName(YutResult result) {
        return switch (result) {
            case DO -> "도";
            case GAE -> "개";
            case GEOL -> "걸";
            case YUT -> "윷";
            case MO -> "모";
            case BackDo -> "백도";
        };
    }
}

