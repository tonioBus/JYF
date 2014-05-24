/**
 * 
 */
package com.justyour.food.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Panel;

/**
 * @author tonio
 *
 */
public abstract class MyComposite extends Composite {
	
	public final void initHeader(Panel header) {
		initWidget(header);
//		header.setStyleName("gwt-Header", true);
//		this.setStylePrimaryName("row");
//		addStyleName("row");
		((HasVerticalAlignment) header)
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public final void initBody(Panel body) {
		initWidget(body);
		// body.setStyleName("jyf-Body", true);
//		this.setStylePrimaryName("row");
//		addStyleName("row");
		((HasVerticalAlignment) body)
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public final void initFooter(Panel footer) {
		initWidget(footer);
//		footer.setStyleName("gwt-Footer", true);
//		this.setStylePrimaryName("row");
//		addStyleName("row");
		((HasVerticalAlignment) footer)
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}

}
