package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.util.LongIntHashtable;

public class TabBarItem extends CustomButtonField {

	public TabBarItem(String text) {
		this(text, 0, null, null);
	}

	public TabBarItem(String text, String image) {
		this(text, 0, image);
	}

	public TabBarItem(String text, long style) {
		this(text, style, null, null);
	}

	public TabBarItem(String text, long style, String image) {
		this(text, style, null, image);
	}

	public TabBarItem(String text, long style, LongIntHashtable colourTable,
			String image) {
		super(text, style, colourTable, image);
	}

}
