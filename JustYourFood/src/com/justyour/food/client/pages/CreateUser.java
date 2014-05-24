/**
 * 
 */
package com.justyour.food.client.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.justyour.food.client.Index;
import com.justyour.food.client.JYFServiceAsync;
import com.justyour.food.client.MyComposite;
import com.justyour.food.client.PageArguments;
import com.justyour.food.client.RootPage;
import com.justyour.food.client.ToolsClient;
import com.justyour.food.shared.jpa.models.UserID;

/**
 * @author tonio
 * 
 */
public class CreateUser extends RootPage {

	static final String acceptUrl = "icons/Accept-icon24.png";
	static final String errorUrl = "icons/Sign-Error-icon24.png";
	private JYFServiceAsync server = Index.getServer();

	static final int PRENOM = 0;
	static final int NOM = 1;
	static final int EMAIL = 2;
	static final int PASSWD1 = 3;
	static final int PASSWD2 = 4;

	TextBox prenomTB = new TextBox();
	Image prenomIMG = new Image();
	TextBox nomTB = new TextBox();
	Image nomIMG = new Image();
	TextBox emailTB = new TextBox();
	Image emailIMG = new Image();
	PasswordTextBox passwd1 = new PasswordTextBox();
	Image passwd1IMG = new Image();
	PasswordTextBox passwd2 = new PasswordTextBox();
	Image passwd2IMG = new Image();
	Image[] icons = new Image[] { prenomIMG, nomIMG, emailIMG, passwd1IMG,
			passwd2IMG };
	TextBox[] textBoxes = new TextBox[] { prenomTB, nomTB, emailTB, passwd1,
			passwd2 };

	Button loginButton = new Button("Enregistrer");

	static public String getSimpleName() {
		return ToolsClient.getSimpleName(CreateUser.class);
	}

	private class Header extends MyComposite {
		public Header() {
			HorizontalPanel hPanel = new HorizontalPanel();
			initHeader(hPanel);
			hPanel.setSpacing(5);

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
					PageArguments args1 = new PageArguments(Login.class);
					History.newItem(args1.getURL());
				}
			});

			Button expertInfoButton = new Button("Administration (Expert)");
			expertInfoButton.setStyleName("button");
			expertInfoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PageArguments args = new PageArguments(ExpertInfo.class);
					History.newItem(args.getURL());
				}
			});
			hPanel.setStyleName("gwt-Menu", true);
			hPanel.add(returnButton);
			hPanel.add(loginButton);
			if (Index.IsAdmin())
				hPanel.add(expertInfoButton);
			hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			verifyInput();
		}
	};

	private class Body extends MyComposite {
		public Body() {
			VerticalPanel panel = new VerticalPanel();
			initBody(panel);
			panel.setSpacing(5);
			panel.setStyleName("gwt-Section", true);

			// Label Receipe
			Label labelReceipe = new Label("Enregistrement d'un utilisateur");
			labelReceipe
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			ToolsClient.setStyle(labelReceipe, "font-size", "2.0em");
			panel.add(labelReceipe);

			Grid grid = new Grid(5, 3);

			Label label = new Label("* Prénoms");
			ToolsClient.setStyle(label, "margin", "0.2em");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			grid.setWidget(0, 0, label);
			addVerifyer(prenomTB);
			grid.setWidget(0, 1, prenomTB);
			grid.setWidget(0, 2, prenomIMG);
			ToolsClient.setStyle(prenomIMG, "vertical-align", "middle");
			ToolsClient.setStyle(prenomIMG, "margin-left", "1em");

			label = new Label("* Nom");
			ToolsClient.setStyle(label, "margin", "0.2em");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			grid.setWidget(1, 0, label);
			addVerifyer(nomTB);
			grid.setWidget(1, 1, nomTB);
			grid.setWidget(1, 2, nomIMG);
			ToolsClient.setStyle(nomIMG, "vertical-align", "middle");
			ToolsClient.setStyle(nomIMG, "margin-left", "1em");

			label = new Label("* Email");
			ToolsClient.setStyle(label, "margin", "0.2em");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			grid.setWidget(2, 0, label);
			addVerifyer(emailTB);
			grid.setWidget(2, 1, emailTB);
			grid.setWidget(2, 2, emailIMG);
			ToolsClient.setStyle(emailIMG, "vertical-align", "middle");
			ToolsClient.setStyle(emailIMG, "margin-left", "1em");

			label = new Label("* Mot de Passe");
			ToolsClient.setStyle(label, "margin", "0.2em");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			grid.setWidget(3, 0, label);
			addVerifyer(passwd1);
			grid.setWidget(3, 1, passwd1);
			grid.setWidget(3, 2, passwd1IMG);
			ToolsClient.setStyle(passwd1IMG, "vertical-align", "middle");
			ToolsClient.setStyle(passwd1IMG, "margin-left", "1em");

			label = new Label("* Verifier votre mot de passe");
			ToolsClient.setStyle(label, "margin", "0.2em");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			grid.setWidget(4, 0, label);
			addVerifyer(passwd2);
			grid.setWidget(4, 1, passwd2);
			grid.setWidget(4, 2, passwd2IMG);
			ToolsClient.setStyle(passwd2IMG, "vertical-align", "middle");
			ToolsClient.setStyle(passwd2IMG, "margin-left", "1em");

			ToolsClient.setStyle(grid, "margin", "0.4em");

			DecoratorPanel decoPanel = new DecoratorPanel();
			decoPanel.add(grid);
			panel.add(decoPanel);
			ToolsClient.setStyle(grid, "padding", "1em");
			ToolsClient.setStyle(grid, "marge", "1em");
			ToolsClient.setStyle(decoPanel, "width", "100%");
			ToolsClient.setStyle(decoPanel, "padding", "1em");

			loginButton.setStyleName("button");
			loginButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final String prenoms = prenomTB.getText().trim();
					String nom = nomTB.getText().trim();
					String email = emailTB.getText().trim();
					String password = passwd1.getText().trim();
					UserID userProfile = new UserID(prenoms, nom,
							email, password);
					server.register(userProfile,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									showDialog(true,
											"Erreur durant l'enregistrement:",
											caught.getLocalizedMessage());
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(final String msgErr) {
									if (msgErr == null) {
										showDialog(true, "Thanks " + prenoms
												+ " for registering !");
										PageArguments args1 = new PageArguments(
												Login.class);
										History.newItem(args1.getURL());
									} else {
										showDialog(false,
												msgErr);
									}
								}
							});

				}

			});
			panel.add(loginButton);

		}

	}

	private void addVerifyer(TextBox textBox) {
		textBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				verifyInput();
			}
		});
	}

	private void verifyInput() {
		loginButton.setEnabled(false);
		loginButton.setVisible(false);

		boolean enable = true;
		for (Image icon : icons) {
			icon.setUrl(acceptUrl);
			icon.setTitle(null);
		}
		String pass1 = passwd1.getText().trim();
		String pass2 = passwd2.getText().trim();
		if (pass1.equals(pass2) == false) {
			passwd1IMG.setUrl(errorUrl);
			passwd1IMG.setTitle("les mots de passes ne correspondent pas");
			passwd2IMG.setUrl(errorUrl);
			passwd2IMG.setTitle("les mots de passes ne correspondent pas");
			enable = false;
		}
		boolean passWordCorrect = isPassWordOk(pass1);
		if (passWordCorrect == false) {
			passwd1IMG.setUrl(errorUrl);
			passwd1IMG
					.setTitle("le mot de passe doit avoir etre entre 8 20 caractères de long et contenir au moins 1 lettre et 1 chiffre");
			enable = false;
		}
		passWordCorrect = isPassWordOk(pass2);
		if (passWordCorrect == false) {
			passwd2IMG.setUrl(errorUrl);
			passwd2IMG
					.setTitle("le mot de passe doit avoir etre entre 8 20 caractères de long et contenir au moins 1 lettre et 1 chiffre");
			enable = false;
		}
		String email = emailTB.getText().trim();
		if (email
				.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") == false) {
			emailIMG.setUrl(errorUrl);
			emailIMG.setTitle("le email n'a pas une syntaxe correcte (ex: durant@gmail.com)");			
		}
		int i = 0;
		for (TextBox tb : textBoxes) {
			if (tb.getText().trim().isEmpty()) {
				icons[i].setUrl(errorUrl);
				icons[i].setTitle("ce champs ne peut être vide");
				enable = false;
			}
			i++;
		}
		loginButton.setVisible(enable);
		loginButton.setEnabled(enable);
	}

	/*
	 * private boolean verifyParam__() { String pass1 =
	 * passwd1.getText().trim(); String pass2 = passwd2.getText().trim(); if
	 * (pass1 != pass2) { return showDialog(false,
	 * "Les 2 mot de passe ne sont pas identique"); } boolean passWordCorrect =
	 * isPassWordOk(pass1); if (passWordCorrect == false) { return
	 * showDialog(true, "Thanks for registering !"); } UserProfile userProfile =
	 * new UserProfile(prenomTB.getText(), nomTB.getText(), emailTB.getText(),
	 * pass1); String msg = userProfile.isValidate(); if (msg == null) { return
	 * showDialog(true, "Thanks for registering !"); } return showDialog(false,
	 * msg); }
	 */

	private boolean isPassWordOk(String pass) {
		int length = pass.length();
		int charCount = 0, intCount = 0;
		byte b[] = pass.getBytes();
		boolean errOccurred = false;
		errOccurred = length < 8 || length > 20;
		if (!errOccurred) {
			for (int i = 0; i < b.length; i++) {
				charCount = ((b[i] > 64 && b[i] < 91) // within A - Z
				|| (b[i] > 96 && b[i] < 123)) == true // within a-z
				? charCount + 1
						: charCount; // increment or use same
				intCount = (b[i] > 47 && b[i] < 58) == true // within A - Z
				? intCount + 1
						: intCount; // increment or use same
			}
		}
		// error occurred earlier or char count or int count was 0
		errOccurred = errOccurred || charCount == 0 || intCount == 0;
		return !errOccurred;
	}

	private boolean showDialog(boolean isCorrect, String... message) {
		DialogBox dialogBox = createDialogBox(!isCorrect, message);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.center();
		dialogBox.show();
		return isCorrect;
	}

	/**
	 * Create the dialog box for this example.
	 * 
	 * @return the new dialog box
	 */
	private DialogBox createDialogBox(boolean error, String... msg) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("dialogBox.setText");

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		ToolsClient.setStyle(dialogContents, "margin", "2em");
		ToolsClient.setStyle(dialogContents, "padding", "1em");
		dialogBox.setWidget(dialogContents);

		// Add some text to the top of the dialog
		String details = "";
		for (String sz : msg) {
			details += "<b>" + sz + "</b><br/>";
		}
		HTML detailsHT = new HTML(details);
		dialogContents.add(detailsHT);
		dialogContents.setCellHorizontalAlignment(detailsHT,
				HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogContents.add(closeButton);
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			dialogContents.setCellHorizontalAlignment(closeButton,
					HasHorizontalAlignment.ALIGN_LEFT);

		} else {
			dialogContents.setCellHorizontalAlignment(closeButton,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}

		// Return the dialog box
		return dialogBox;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public CreateUser(PageArguments args) {
		super(CreateUser.class);
		super.header = new Header();
		super.body = new Body();
	}

	@Override
	public void init(PageArguments args) {
	}

}
