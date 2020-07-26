package com.qa.runner;

import java.io.IOException;

import org.apache.logging.log4j.ThreadContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty"
		, "summary"
		, "de.monochromata.cucumber.report.PrettyReports:target/cucumber-2"}
		, features = { "src/test/resources" }
		, glue = {"com.bdd.steps" }
		, snippets = SnippetType.CAMELCASE
		, dryRun = false
		, monochrome = true
		, strict = true)
//		,tags = {"@foo", "not @bar"})

public class MyTestRunner {

	@BeforeClass
	public static void initialize() throws Exception {
		GlobalParams params = new GlobalParams();
		params.initializeGlobalParams();
		ThreadContext.put("ROUTINGKEY", params.getPlatformName() + "_" + params.getDeviceName());
		new ServerManager().startServer();
		new DriverManager().initializeDriver();
		// new VideoManager().startRecording();
	}

	@AfterClass
	public static void quit() throws IOException {

		// new VideoManager().stopRecording(scenario.getName());
		DriverManager driverManager = new DriverManager();
		if (driverManager.getDriver() != null) {
			driverManager.getDriver().quit();
			driverManager.setDriver(null);
		}
		ServerManager serverManager = new ServerManager();
		if (serverManager.getServer() != null) {
			serverManager.getServer().stop();
		}
	}
}
