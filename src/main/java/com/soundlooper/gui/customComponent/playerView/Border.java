package com.soundlooper.gui.customComponent.playerView;

import com.soundlooper.CssColor;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Line;

public class Border extends FlowPane {
	Line line1 = new Line(0, 0, this.getWidth(), 0);
	Line line2 = new Line(0, 0, this.getWidth(), 0);
	Line line3 = new Line(0, 0, this.getWidth(), 0);

	public Border(double leftPaddin) {
		this.setOrientation(Orientation.HORIZONTAL);

		line1.setStroke(CssColor.GRAY.getColor());
		this.getChildren().add(line1);

		line2.setStroke(CssColor.WHITE.getColor());
		line2.setStrokeWidth(2);
		this.getChildren().add(line2);

		line3.setStroke(CssColor.GRAY.getColor());
		this.getChildren().add(line3);

		this.setPadding(new Insets(0, 0, 0, leftPaddin));
		this.setHeight(4);
	}

	public void setLineWidth(double width) {
		line1.setEndX(width);
		line2.setEndX(width);
		line3.setEndX(width);
	}
}
