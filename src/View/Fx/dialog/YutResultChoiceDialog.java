package View.Fx.dialog;

import Controller.YutResult;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.function.Consumer;

public class YutResultChoiceDialog {

    public static void show(List<YutResult> yutResults, Consumer<YutResult> onSelected) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: transparent;");

        // 배경 이미지
        Image modalImage = new Image(YutResultChoiceDialog.class.getResourceAsStream("/image/결과 적용.png"));
        ImageView modalView = new ImageView(modalImage);
        root.getChildren().add(modalView);

        // 버튼 박스
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setTranslateY(25);

        for (YutResult result : yutResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            Image image = new Image(YutResultChoiceDialog.class.getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            btn.setOnAction(e -> {
                dialog.close();
                onSelected.accept(result);
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
