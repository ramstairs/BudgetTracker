module HelloFX {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires AnimateFX;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
