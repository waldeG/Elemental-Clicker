package uiContainers;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import Buildings.Building;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DrawBuilding extends HBox implements Drawable {
	private Text buildingText;
	private Text buildingCost;
	private Text buildingProportion;
	private Text buildingAmount;
	private int errorTimeLeft = 0;
	private Building b;

	public DrawBuilding(Building b) {
		this.b = b;
		uiContainers.DrawMaster.getDrawMaster().add(this);

		ImageView ivbuilding = new ImageView(b.getIcon());
		ivbuilding.setFitWidth(80);
		ivbuilding.setFitHeight(80);
		ivbuilding.setPreserveRatio(true);
		ivbuilding.setSmooth(true);
		ivbuilding.setCache(true);

		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setSpacing(5);
		vb.setMinWidth(130);
		buildingText = new Text(b.getName());
		buildingText.setStyle("-fx-font-size: 22px;");

		buildingCost = new Text(String.format("%.0f", b.getCost()));
		buildingCost.setStyle("-fx-font-size: 16px;");

		buildingProportion = new Text(b.getProportion() == 0.0 ? String.format("%.0f%s", b.getProportion(), "%")
				: String.format("%.1f%s", b.getProportion(), "%"));
		buildingProportion.setStyle("-fx-font-size: 16px;");

		vb.getChildren().addAll(buildingText, buildingCost, buildingProportion);

		buildingAmount = new Text(String.valueOf(b.getAmount()));
		buildingAmount.setStyle("-fx-font-size: 30px;");
		buildingAmount.textProperty().addListener((obs, oldVal, newVal) -> {
			DrawMaster.getDrawMaster().fixSizes();
		});
		buildingAmount.setWrappingWidth(60);

		setAlignment(Pos.CENTER);
		setSpacing(25);
		setPadding(new Insets(2));
		setStyle(
				"-fx-border-color: black;-fx-background-color: #FFFFFF;-fx-background-size: 201 100;-fx-border-width: 0 0 4 4;");
		getChildren().addAll(ivbuilding, vb, buildingAmount);



	}

	//view.setStyle(String.format("-fx-border-color: rgba(%i, %i, %i, 1.0)", counter, 255-counter, 255-counter));


	@Override
	public void fixSize() {
		if(errorTimeLeft!=0){
			this.setStyle(String.format("-fx-border-color: black;-fx-border-width: 0 0 4 4;"
					+ "-fx-background-size: %.0f %.0f;", getWidth(), getHeight()) +
					String.format("-fx-background-color: rgba(%d, 0, 0, 1.0)", 155+errorTimeLeft));
		}else{
			this.setStyle(
					String.format("-fx-border-color: black;-fx-background-color: #FFFFFF;-fx-border-width: 0 0 4 4;"
					+ "-fx-background-size: %.0f %.0f;", getWidth(), getHeight()));
		}
	}

	@Override
	public void draw() {
		buildingText.setText(b.getName());
		buildingCost.setText(formartiermich(b.getCost()));
		buildingProportion.setText(b.getProportion() == 0.0 ? String.format("%.0f%s", b.getProportion(), "%")
				: String.format("%.1f%s", b.getProportion(), "%"));
		buildingAmount.setText(String.valueOf(b.getAmount()));
		if(errorTimeLeft!=0){
			this.setStyle(String.format("-fx-border-color: black;-fx-border-width: 0 0 4 4;"
					+ "-fx-background-size: %.0f %.0f;", getWidth(), getHeight()) +
					String.format("-fx-background-color: rgba(%d, 0, 0, 1.0)", 155+errorTimeLeft));
					errorTimeLeft--;
		}else{
			this.setStyle(
					String.format("-fx-border-color: black;-fx-background-color: #FFFFFF;-fx-border-width: 0 0 4 4;"
					+ "-fx-background-size: %.0f %.0f;", getWidth(), getHeight()));
		}
	}

	public void setErrorTimeLeft(int errorTimeLeft) {
		this.errorTimeLeft = errorTimeLeft;
	}

	private static final NavigableMap<Double, String> suffixes = new TreeMap<>();
	static {
		suffixes.put(1_000D, "k");
		suffixes.put(1_000_000D, "M");
		suffixes.put(1_000_000_000D, "B");
		suffixes.put(1_000_000_000_000D, "T");
		suffixes.put(1_000_000_000_000_000D, "Qa");
		suffixes.put(1_000_000_000_000_000_000D, "Qi");
		suffixes.put(1_000_000_000_000_000_000_000D, "Sx");
		suffixes.put(1_000_000_000_000_000_000_000_000D, "Sp");
		suffixes.put(1_000_000_000_000_000_000_000_000_000D, "Oc");
		suffixes.put(1_000_000_000_000_000_000_000_000_000_000D, "No");
		suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000D, "Dc");
		suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000D, "Ud");
	}

	public static String formartiermich(double value) {
		if (value == Double.MIN_VALUE)
			return formartiermich(Double.MIN_VALUE + 1);
		if (value < 0)
			return "-" + formartiermich(-value);
		if (value < 1000)
			return String.format("%.0f", value);

		Map.Entry<Double, String> e = suffixes.floorEntry(value);
		Double divideBy = e.getKey();
		String suffix = e.getValue();

		double truncated = value / (divideBy / 10);
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return (hasDecimal ? String.format("%.2f", (truncated / 10d)) + suffix
				: String.format("%.2f", (truncated / 10)) + suffix).replace(",", ".");
	}

}
