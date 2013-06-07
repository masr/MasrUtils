package com.masr.masrutils.test;

import static org.junit.Assert.*;
import in.masr.masrutils.Utils;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void getSimpleEntryClassName() {
		assertEquals(
				"class org.eclipse.jdt.internal.junit.runner.RemoteTestRunner",
				Utils.getEntryClass().toString());
	}

	@Test
	public void getEntryClass() {
		assertEquals("RemoteTestRunner", Utils.getSimpleEntryClassName());
	}

}
