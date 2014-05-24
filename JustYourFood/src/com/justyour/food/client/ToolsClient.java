/**
 * 
 */
package com.justyour.food.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author tonio
 * 
 */
/**
 * @author tonio
 * 
 */
public class ToolsClient {

	/**
	 * @param widget
	 * @param field
	 *            - fontFamily or font-family is accepted
	 * @param value
	 */
	public static void setStyle(Widget widget, String field, String value) {
		int indexMinus = field.indexOf('-');
		if (indexMinus != -1) {
			String prefix = field.substring(0, indexMinus);
			String suffix = field.substring(indexMinus + 1);
			// word -> Word
			suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
			field = prefix + suffix;
		}
		System.out.println("setStyle(" + field + "," + value + ")");
		widget.getElement().setAttribute( field, value);
	}

	public static String getFormattedQuantity(Double qty) {
		NumberFormat fmt = null;
		if (qty == Math.rint(qty)) {
			fmt = NumberFormat.getFormat("0");
		} else {
			fmt = NumberFormat.getFormat("0.00");
		}
		return fmt.format(qty);
	}

	public static String getFormattedDelay(Double d) {
		if (d == 0.0)
			return "0";
		NumberFormat fmt = null;
		if (d == Math.rint(d)) {
			fmt = NumberFormat.getFormat("0");
		} else {
			fmt = NumberFormat.getFormat("0.00");
		}
		return fmt.format(d) + " min(s)";
	}

	public static String getFormattedNumber(double number) {
		NumberFormat fmt = null;
		if (number == Math.rint(number)) {
			fmt = NumberFormat.getFormat("0");
		} else {
			fmt = NumberFormat.getFormat("0.00");
		}
		return fmt.format(number);
	}

	public static double readNumber(String text, double oldValue) {
		String[] tokens = text.split(" ");
		try {
			return Double.parseDouble(tokens[0]);
		} catch (NumberFormatException e) {
		}
		return oldValue;
	}

	public static void applyNumericMask(TextBox textBox,
			final boolean allowDecimal) {
		textBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent keyDownEvent) {
				int keyCode = keyDownEvent.getNativeKeyCode();
				if ((!((keyCode > 46 && keyCode < 58)) && !((keyCode > 96 && keyCode < 108)))
						&& (keyCode != (char) KeyCodes.KEY_TAB)
						&& (keyCode != (char) KeyCodes.KEY_BACKSPACE)
						&& (keyCode != (char) KeyCodes.KEY_ESCAPE)
						&& (keyCode != (char) KeyCodes.KEY_DELETE)
						&& (keyCode != (char) KeyCodes.KEY_ENTER)
						&& (keyCode != (char) KeyCodes.KEY_HOME)
						&& (keyCode != (char) KeyCodes.KEY_END)
						&& (keyCode != (char) KeyCodes.KEY_LEFT)
						&& (keyCode != (char) KeyCodes.KEY_UP)
						&& (keyCode != 190 || !allowDecimal)
						&& (keyCode != 110 || !allowDecimal)
						&& (keyCode != (char) KeyCodes.KEY_RIGHT)
						&& (keyCode != (char) KeyCodes.KEY_DOWN)) {

					keyDownEvent.preventDefault();
					keyDownEvent.stopPropagation();
				}
			}
		});

		textBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent keyUpEvent) {
				int keyCode = keyUpEvent.getNativeKeyCode();
				if ((!((keyCode > 46 && keyCode < 58)) && !((keyCode > 96 && keyCode < 108)))
						&& (keyCode != (char) KeyCodes.KEY_TAB)
						&& (keyCode != (char) KeyCodes.KEY_BACKSPACE)
						&& (keyCode != (char) KeyCodes.KEY_ESCAPE)
						&& (keyCode != (char) KeyCodes.KEY_DELETE)
						&& (keyCode != (char) KeyCodes.KEY_ENTER)
						&& (keyCode != (char) KeyCodes.KEY_HOME)
						&& (keyCode != (char) KeyCodes.KEY_END)
						&& (keyCode != (char) KeyCodes.KEY_LEFT)
						&& (keyCode != (char) KeyCodes.KEY_UP)
						&& (keyCode != 190 || !allowDecimal)
						&& (keyCode != 110 || !allowDecimal)
						&& (keyCode != (char) KeyCodes.KEY_RIGHT)
						&& (keyCode != (char) KeyCodes.KEY_DOWN)) {

					keyUpEvent.preventDefault();
					keyUpEvent.stopPropagation();
				}
			}
		});
	}

	/**
	 * @param clazz
	 *            - the class from which we will extract a simple name
	 * @return - the simple name, used for the mapping between window and GWT
	 *         page class
	 */
	public static String getSimpleName(Class<?> clazz) {
		String typeName = clazz.getName();
		int lastDot = typeName.lastIndexOf('.');
		if (lastDot >= 0) {
			int lastDollar = typeName.lastIndexOf('$', lastDot);
			if (lastDollar > lastDot) {
				lastDot = lastDollar;
			}
			return typeName.substring(lastDot + 1);
		}
		return typeName;
	}

	static public interface IUpdate {
		abstract public void updateFromObject(double ratio);

		abstract public void updateFromGWT();
	}

	static public abstract class Update implements IUpdate {
		HasText hasText = null;

		public Update(HasText hasText) {
			this.hasText = hasText;
		}

		public void setHasText(HasText hasText) {
			this.hasText = hasText;
		}

		public void setText(String text) {
			this.hasText.setText(text);
		}

		public String getText() {
			return this.hasText.getText();
		}
	};

	static public class Field {
		public Field(String name, String value, boolean readOnly, String title,
				Update update) {
			super();
			this.name = name;
			this.value = value;
			this.readOnly = readOnly;
			this.title = title;
			this.update = update;
		}

		String name;
		String value;
		boolean readOnly;
		String title;
		Update update;
	};

	static public void setGridCellStyle(Grid grid, String evenRow, String oddRow) {
		for (int row = 0; row < grid.getRowCount(); row++) {
			if (row == 0 || row % 2 == 0) {
				grid.getRowFormatter().setStyleName(row, evenRow);
			} else {
				grid.getRowFormatter().setStyleName(row, oddRow);
			}
		}
	}

	static public class PropPanel extends VerticalPanel implements IUpdate {
		public static final String VALUE_READ_ONLY = "?";
		protected List<Update> updates = new ArrayList<Update>();

		public PropPanel(String name, Field... fields) {
			super();
			HTML labelName = new HTML(name);
			// labelName.setStyleName("jyf-simpleLabel");
			labelName
					.getElement()
					.setAttribute("style",
							"display:block; width:auto; margin:0px; border-color:yellow;");
			this.add(labelName);
			Grid grid = new Grid(fields.length, 2);
			grid.setStyleName("jyf-simpleBorder", true);
			// grid.setBorderWidth(5);
			grid.setCellSpacing(2);
			int i = 0;
			for (Field field : fields) {
				Label labelField = new Label(field.name + " :");
				// labelField.setStyleName("jyf-simpleLabel", true);
				labelField.getElement().setAttribute("style",
						"font-style:normal; font-size:1.0em;");
				TextBox tbValue = new TextBox();
				tbValue.setStyleName("jyf-textbox", true);
				tbValue.setText(field.value);
				ToolsClient.setStyle(tbValue, "width", "10em");
				// tbValue.setMaxLength(8);
				tbValue.setReadOnly(field.readOnly);
				if (field.readOnly) {
					tbValue.getElement().setAttribute("color", "Khaki");
					ToolsClient.setStyle(tbValue, "font-style", "normal");
					ToolsClient.setStyle(tbValue, "background-color", "Kakhi");
				} else {
					tbValue.getElement()
							.setAttribute("style",
									"font-style:bold; background-color: LightYellow; border-color:orange;");
					tbValue.getElement().setAttribute("type", "number");
					ToolsClient.setStyle(tbValue, "width", "8em");
				}
				final Update update = field.update;
				update.setHasText(tbValue);
				if (field.readOnly == false) {
					ToolsClient.applyNumericMask(tbValue, false);
					tbValue.addKeyPressHandler(new com.google.gwt.event.dom.client.KeyPressHandler() {

						@Override
						public void onKeyPress(
								com.google.gwt.event.dom.client.KeyPressEvent event) {
							if (event.getNativeEvent().getKeyCode() == 13) {
								update.updateFromGWT();
							}
						}
					});
				}
				updates.add(update);
				tbValue.setTitle(field.title);
				grid.setWidget(i, 0, labelField);
				grid.setWidget(i, 1, tbValue);
				grid.getCellFormatter().setHorizontalAlignment(i, 0,
						HasHorizontalAlignment.ALIGN_RIGHT);
				grid.getCellFormatter().setVerticalAlignment(i, 0,
						HasVerticalAlignment.ALIGN_MIDDLE);
				grid.getCellFormatter().setHorizontalAlignment(i, 1,
						HasHorizontalAlignment.ALIGN_LEFT);
				grid.getCellFormatter().setVerticalAlignment(i, 1,
						HasVerticalAlignment.ALIGN_MIDDLE);
				i++;
			}
			this.add(grid);
		}

		@Override
		public void updateFromObject(double ratio) {
			for (Update update : this.updates) {
				try {
					update.updateFromObject(ratio);
				} catch (Exception e) {
					update.setText(e.getLocalizedMessage());
				}
			}
		}

		@Override
		public void updateFromGWT() {
			for (Update update : this.updates) {
				update.updateFromGWT();
			}
		}
	}

	public static class JYFDialog extends DialogBox {
		VerticalPanel panel;

		/**
		 * @return the panel
		 */
		public VerticalPanel getPanel() {
			return panel;
		}

		public JYFDialog(String titleSz, String labelSz) {
			// Set the dialog box's caption.
			setText(titleSz);

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			Label label = new Label(labelSz);

			panel = new VerticalPanel();
			panel.setHeight("100");
			panel.setWidth("300");
			panel.setSpacing(10);
			panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			Image image = new Image("images/jyf.jpg");
			panel.add(image);
			panel.add(label);
			setWidget(panel);
		}
	}

	public static JYFDialog showDialog(String title, String msg) {
		final JYFDialog dialog = new JYFDialog(title, msg);
		Button ok = new Button("OK");
		dialog.getPanel().add(ok);
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		int left = Window.getClientWidth() / 2;
		int top = Window.getClientHeight() / 2;
		dialog.setPopupPosition(left, top);
		dialog.show();
		return dialog;
	}

	public static JYFDialog showProcessDialog(String title, String msg) {
		final JYFDialog dialog = new JYFDialog(title, msg);
		Image progressImage = new Image("images/pleasewait.gif");
		dialog.getPanel().add(progressImage);
		int left = Window.getClientWidth() / 2;
		int top = Window.getClientHeight() / 2;
		dialog.setPopupPosition(left, top);
		dialog.show();
		return dialog;
	}

	public static void endingProcessDialog(final JYFDialog dialog,
			String endingMsg) {
		Button ok = new Button("Done");
		if (endingMsg != null)
			dialog.getPanel().add(new HTML(endingMsg));
		dialog.getPanel().add(ok);
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
	}

	/*
	 * public static PropPanel createProp(String name, Field fields[], Panel
	 * rootPanel) { VerticalPanel retPanel = new VerticalPanel(); Label
	 * labelName = new Label(name); labelName.setStyleName("frameTitle", true);
	 * retPanel.add(labelName); Grid grid = new Grid(fields.length, 2);
	 * grid.setCellSpacing(5); for (Field field : fields) { Label labelField =
	 * new Label(field.name); TextBox tbValue = new TextBox();
	 * tbValue.setText(field.value); final Update update = field.update; if
	 * (update != null) { update.setHasText(tbValue);
	 * ToolsClient.applyNumericMask(tbValue, false);
	 * tbValue.addKeyPressHandler(new
	 * com.google.gwt.event.dom.client.KeyPressHandler() {
	 * 
	 * @Override public void onKeyPress(
	 * com.google.gwt.event.dom.client.KeyPressEvent event) { if
	 * (event.getNativeEvent().getKeyCode() == 13) { update.updateFromGWT(); } }
	 * }); updates.add(update); } else { tbValue.setReadOnly(true); }
	 * tbValue.setTitle(title[i]);
	 * 
	 * } for (int i = 0; i < fields.length; i++) { Label labelField = new
	 * Label(fields[i].name); TextBox tbValue = new TextBox();
	 * tbValue.setText(value[i]); final Update update = updates[i]; if (update
	 * != null) { update.setHasText(tbValue);
	 * ToolsClient.applyNumericMask(tbValue, false);
	 * tbValue.addKeyPressHandler(new
	 * com.google.gwt.event.dom.client.KeyPressHandler() {
	 * 
	 * @Override public void onKeyPress(
	 * com.google.gwt.event.dom.client.KeyPressEvent event) { if
	 * (event.getNativeEvent().getKeyCode() == 13) { update.updateFromGWT(); } }
	 * }); this.updates.add(update); } else { tbValue.setReadOnly(true); }
	 * tbValue.setTitle(title[i]); grid.setWidget(i, 0, labelField);
	 * grid.setWidget(i, 1, tbValue);
	 * grid.getCellFormatter().setHorizontalAlignment(i, 0,
	 * HasHorizontalAlignment.ALIGN_RIGHT);
	 * grid.getCellFormatter().setVerticalAlignment(i, 0,
	 * HasVerticalAlignment.ALIGN_MIDDLE);
	 * grid.getCellFormatter().setHorizontalAlignment(i, 1,
	 * HasHorizontalAlignment.ALIGN_LEFT);
	 * grid.getCellFormatter().setVerticalAlignment(i, 1,
	 * HasVerticalAlignment.ALIGN_MIDDLE); } DecoratorPanel decoratorPanel = new
	 * DecoratorPanel(); decoratorPanel.add(grid); retPanel.add(decoratorPanel);
	 * rootPanel.add(retPanel); return decoratorPanel; }
	 */

}
