/** 
 * Project: JustYourFood
 * 
 * File: WatermarkTextBox.java
 * Package: com.justyour.food.client.pages.tools
 * Date: 9 févr. 2014
 * @author Igor Moochnick 
 * 
 */

package com.justyour.food.client.pages.tools;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author tonio
 * 
 */
public class WatermarkedSuggestBox extends SuggestBox implements BlurHandler,
		FocusHandler {

	String watermark;
	HandlerRegistration blurHandler;
	HandlerRegistration focusHandler;

	public WatermarkedSuggestBox(SuggestOracle receipeOracle, String watermark) {
		super(receipeOracle);
		setText("");
		this.setStylePrimaryName("jyf-textInput");
		setWatermark(watermark);
	}

	/**
	 * Adds a watermark if the parameter is not NULL or EMPTY
	 * 
	 * @param watermark
	 */
	public void setWatermark(final String watermark) {
		this.watermark = watermark;

		if ((watermark != null) && (watermark.length() != 0)) {
			blurHandler = addBlurHandler(this);
			focusHandler = addFocusHandler(this);
			EnableWatermark();
		} else {
			// Remove handlers
			blurHandler.removeHandler();
			focusHandler.removeHandler();
		}
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler, BlurEvent.getType());
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	@Override
	public void onBlur(BlurEvent event) {
		EnableWatermark();
	}

	void EnableWatermark() {
		String text = getText();
		if ((text.length() == 0) || (text.equalsIgnoreCase(watermark))) {
			// Show watermark
			setText(watermark);
			addStyleDependentName("watermark");
		}
	}

	@Override
	public void onFocus(FocusEvent event) {
		removeStyleDependentName("watermark");

		if (getText().equalsIgnoreCase(watermark)) {
			// Hide watermark
			setText("");
		}

	}
}
