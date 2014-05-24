/**
 * 
 */
package com.justyour.food.client.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;

/**
 * @author tonio
 * 
 */
public class Test_tmp extends RootPage {

	final TextArea textAreaCurrentTrace = new TextArea();

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(Test_tmp.class);
	}

	@Override
	public boolean isInitEnable() {
		return false;
	}

	private class Header extends MyComposite {
		public Header() {
			VerticalPanel panel = new VerticalPanel();
			initHeader(panel);

			Button returnButton = new Button("Retour");
			returnButton.setStyleName("button");
			returnButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					History.back();
				}
			});
			Button loginButton = new Button("Accueil");
			loginButton.setStyleName("button");
			loginButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(Login.class);
					History.newItem(args.getURL());
				}
			});
			Grid buttonGrid = new Grid(1, 2);
			buttonGrid.setStyleName("gwt-Menu", true);
			buttonGrid.setWidget(0, 0, returnButton);
			buttonGrid.setWidget(0, 1, loginButton);
			buttonGrid.getCellFormatter().setHorizontalAlignment(0, 0,
					HasHorizontalAlignment.ALIGN_CENTER);
			buttonGrid.getCellFormatter().setVerticalAlignment(0, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			panel.add(buttonGrid);
		}
	}

	public class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(5);

			VerticalPanel errorPanel = new VerticalPanel();
			Label label = new Label("La précédente requête à échouer.");
			errorPanel.add(label);
			Label label1 = new Label("parce qu'elle le vaut bien");
			errorPanel.add(label1);
			label1.setStyleName("gwt-ErrorLabel", true);
			errorPanel.add(label1);
			final DecoratorPanel errorLabelDec = new DecoratorPanel();
			errorLabelDec.add(errorPanel);

			VerticalPanel currentStackPanel = new VerticalPanel();
			currentStackPanel.add(new Label("Current Stack"));
			currentStackPanel.add(textAreaCurrentTrace);
			textAreaCurrentTrace.setAlignment(TextAlignment.LEFT);
			textAreaCurrentTrace.setReadOnly(true);
			String text = "Il y avait déjà bien des années que, de Combray, tout ce qui n’était pas le théâtre et le drame de mon coucher n’existait plus pour moi, quand un jour d’hiver, comme je rentrais à la maison, ma mère, voyant que j’avais froid, me proposa de me faire prendre, contre mon habitude, un peu de thé. Je refusai d’abord et, je ne sais pourquoi, me ravisai. Elle envoya chercher un de ces gâteaux courts et dodus appelés Petites Madeleines qui semblaient avoir été moulées dans la valve rainurée d’une coquille de Saint-Jacques. Et bientôt, machinalement, accablé par la morne journée et la perspective d’un triste lendemain, je portai à mes lèvres une cuillerée du thé où j’avais laissé s’amollir un morceau de madeleine. Mais à l’instant même où la gorgée mêlée des miettes du gâteau toucha mon palais, je tressaillis, attentif à ce qui se passait d’extraordinaire en moi. Un plaisir délicieux m’avait envahi, isolé, sans la notion de sa cause. Il m’avait aussitôt rendu les vicissitudes de la vie indifférentes, ses désastres inoffensifs, sa brièveté illusoire, de la même façon qu’opère l’amour, en me remplissant d’une essence précieuse: ou plutôt cette essence n’était pas en moi, elle était moi. J’avais cessé de me sentir médiocre, contingent, mortel. D’où avait pu me venir cette puissante joie ? Je sentais qu’elle était liée au goût du thé et du gâteau, mais qu’elle le dépassait infiniment, ne devait pas être de même nature. D’où venait-elle ? Que signifiait-elle ? Où l’appréhender ? Je bois une seconde gorgée où je ne trouve rien de plus que dans la première, une troisième qui m’apporte un peu moins que la seconde. Il est temps que je m’arrête, la vertu du breuvage semble diminuer. Il est clair que la vérité que je cherche n’est pas en lui, mais en moi. Il l’y a éveillée, mais ne la connaît pas, et ne peut que répéter indéfiniment, avec de moins en moins de force, ce même témoignage que je ne sais pas interpréter et que je veux au moins pouvoir lui redemander et retrouver intact, à ma disposition, tout à l’heure, pour un éclaircissement décisif. Je pose la tasse et me tourne vers mon esprit. C’est à lui de trouver la vérité. Mais comment ? Grave incertitude, toutes les fois que l’esprit se sent dépassé par lui-même ; quand lui, le chercheur, est tout ensemble le pays obscur où il doit chercher et où tout son bagage ne lui sera de rien. Chercher ? pas seulement : créer. Il est en face de quelque chose qui n’est pas encore et que seul il peut réaliser, puis faire entrer dans sa lumière.";
			textAreaCurrentTrace.setText(text);

			DecoratorPanel currentDec = new DecoratorPanel();
			currentDec.add(currentStackPanel);
			panel.add(errorLabelDec);
//			panel.add(currentDec);
			panel.add(textAreaCurrentTrace);
		}
	}

	public Test_tmp(PageArguments args) {
		super(Test_tmp.class);
		super.header = new Header();
		super.body = new Body();
		super.footer = null;
	}

	@Override
	public void init(PageArguments args) {
	}

	@Override
	public String getTitle() {
		return "Madeleines";
	}

}
