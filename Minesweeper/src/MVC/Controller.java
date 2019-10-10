package MVC;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.Optional;
import java.util.Timer;

public class Controller {

    MinesweeperModel model;
    Button[][] board;
    final int ROWCOUNT = 9, COLUMNCOUNT = 11;

    Label countFlag;
    Label time;
    Timer timer;

    @FXML
    private ToggleGroup level;

    @FXML
    private Button next;

    @FXML
    private void startGame() throws Exception {
        Stage stage = (Stage) next.getScene().getWindow();

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("View/Game.fxml"));
        Pane root = (Pane)myLoader.load();

        stage.getScene().setRoot(root);

        Controller controller = (Controller) myLoader.getController();
        controller.model = new MinesweeperModel(getMineCount());
        controller.createBoard(root);
    }

    private int getMineCount(){
        int count = 10;
        RadioButton btn = (RadioButton) level.getSelectedToggle();
        if (btn.getText().equals("Любитель")) count = 15;
        if (btn.getText().equals("Проффесионал")) count = 20;
        return count;
    }

    private void createBoard(Pane root) {
        GridPane pane = new GridPane();
        board = new Button[ROWCOUNT][COLUMNCOUNT];
        for (int i = 0; i < ROWCOUNT; i++) {
            for (int j = 0; j < COLUMNCOUNT; j++) {
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setStyle("-fx-font: 20 arial; -fx-base:#dcdcdc; -fx-color:#a2a6a7;");
                int row = i; int column = j;
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                            onRightClick(row,column);
                        }
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            onLeftClick(row, column);
                        }
                    }
                });
                board[i][j] = button;
                pane.add(board[i][j],j,i);
            }
        }
        pane.setLayoutX(60.0);
        pane.setLayoutY(50.0);

        countFlag = new Label("" + model.countFlag);
        countFlag.setStyle("-fx-font: 18 constantia; -fx-text-fill: #247957;");
        countFlag.setLayoutX(220);
        countFlag.setLayoutY(440);

        timer = new Timer();
        time = new TimerLabel(timer);
        time.setStyle("-fx-font: 18 constantia; -fx-text-fill: #247957;");
        time.setLayoutX(410);
        time.setLayoutY(440);

        root.getChildren().add(pane);
        root.getChildren().add(time);
        root.getChildren().add(countFlag);
    }

    private void onLeftClick(int row, int column) {
        model.openCell(row, column);
        syncWithModel();
        if (model.isWin())
            showMessage("Поздравляем! Вы выиграли!");
        if (model.isGameOver())
            showMessage("Игра закончена. Вы проиграли.");
    }

    private void onRightClick(int row, int column) {
        model.flaggedCell(row, column);
        syncWithModel();
    }

    private void syncWithModel(){
        for (int i = 0; i < ROWCOUNT; i++) {
            for (int j = 0; j < COLUMNCOUNT; j++) {
                Cell cell = model.cellsTable[i][j];
                Button btn = board[i][j];

                if (model.isGameOver() && cell.mined)
                    btn.setStyle("-fx-color:#000000;");

                if (cell.state == "closed") {
                    btn.setGraphic(null);
                    btn.setText(" ");
                }

                if (cell.state == "opened") {
                    if (cell.mined)
                        btn.setStyle("-fx-color:#FF0000;");
                    else {
                        btn.setStyle("-fx-font: 20 arial;");
                        if (cell.counter > 0)
                            btn.setText("" + cell.counter);
                        else
                            btn.setStyle("-fx-color: #FbFbFb");
                    }
                }

                if (cell.state == "flagged") {
                    btn.setStyle("-fx-text-fill:#FF0000;-fx-font-weight:bold;");
                    btn.setText("B");
                    countFlag.setText("" + model.countFlag);
                }

            }
        }

    }

    private void showMessage(String s) {
        timer.cancel();
        Stage dialog = new Stage();
        Pane dialogPane = new Pane();

        Button button = new Button("Начать новую игру");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    newGame();
                    dialog.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        button.setLayoutX(60); button.setLayoutY(60);
        Label text = new Label(s);
        text.setLayoutX(20); text.setLayoutY(10);

        dialogPane.setStyle("-fx-background-color:#ffffff; -fx-font: 20 constantia; -fx-text-fill:#247957;");
        dialogPane.getChildren().add(button);
        dialogPane.getChildren().add(text);
        dialog.setScene(new Scene(dialogPane,330,120));
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/image/icon.png")));
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner((Stage) countFlag.getScene().getWindow());
        dialog.showAndWait();
    }

    @FXML
    private void showGameRules() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Правила игры");
        DialogPane dialogPane = new DialogPane();
        dialogPane.setStyle("-fx-background-color:#ffffff;-fx-font:17 constantia;");
        dialog.setDialogPane(dialogPane);
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        dialogPane.setContentText("Игра - Сапер.\n" +
                "Цель игры - открыть все ячейки без мин.\n" +
                "Цифры на ячейках указывают количество мин, находящихся в соседних клетках.\n" +
                "Открыть ячейку - щелчок левой кнопкой мыши.\n" +
                "Поставить метку - щелчок правой кнопкой мыши.\n" +
                "Убрать метку - еще один клик правой кнопкой мыши.");
        dialog.setWidth(300);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner((Stage) countFlag.getScene().getWindow());
        dialog.showAndWait();
    }

    @FXML
    private void newGame() throws Exception {
        Parent root1 = FXMLLoader.load(getClass().getResource("View/NewGame.fxml"));
        Stage stage = (Stage) countFlag.getScene().getWindow();
        stage.getScene().setRoot(root1);
    }

    @FXML
    private void closeGame(ActionEvent actionEvent) {
        timer.cancel();
        Platform.exit();
    }

}
