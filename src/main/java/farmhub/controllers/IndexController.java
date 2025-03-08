package farmhub.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class IndexController extends BaseController {
   @FXML
   private AnchorPane anchorPane;

   @FXML
   private ImageView imageView;

   @FXML
   public void initialize() {
      imageView.fitWidthProperty().bind(anchorPane.widthProperty());
      imageView.fitHeightProperty().bind(anchorPane.heightProperty());
   }

   @FXML
   private void handlerRegisterClick(ActionEvent event) {
      navigateTo("/views/account/register.fxml", event);
   }
}