package com.justyour.food.test;

import org.junit.Test;

import com.justyour.food.client.ToolsClient;
import com.justyour.food.client.pages.Login;
import com.justyour.food.client.pages.Receipe;

public class FlowControlTest {


	@Test
	public void testSimpleName() {
		System.out.println("SimpleName[]=" + ToolsClient.getSimpleName(Error.class));
		System.out.println("SimpleName[]=" + ToolsClient.getSimpleName(Login.class));
		System.out.println("SimpleName[]=" + ToolsClient.getSimpleName(Receipe.class));
	}

}
