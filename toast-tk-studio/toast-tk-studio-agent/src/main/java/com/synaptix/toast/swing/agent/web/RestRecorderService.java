package com.synaptix.toast.swing.agent.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import com.google.gson.Gson;
import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.core.annotation.craft.FixMe;

@FixMe(todo = "ensure we have firefow browser installed, use a factory")
public class RestRecorderService extends Verticle {

	private static final Logger LOG = LogManager.getLogger(RestRecorderService.class);

	private WebDriver driver;

	private boolean isStarted;

	private static final String PATH = "/record";

	private KryoAgentServer server;

	private void processEvent(
		WebEventRecord record) {
		server.sendEvent(record);
	}

	@Override
	public void start() {
		LOG.info("Starting..");
		RouteMatcher matcher = new RouteMatcher();
		final Gson gson = new Gson();
		matcher.options("/record/event", new Handler<HttpServerRequest>() {
			@Override
			public void handle(
				HttpServerRequest req) {
				req.response().headers().add("Access-Control-Allow-Origin", "*");
				req.response().headers().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, POST");
				req.response().setStatusCode(200).end();
			}
		});
		matcher.post(PATH + "/event", new Handler<HttpServerRequest>() {

			@Override
			public void handle(
				HttpServerRequest req) {
				req.bodyHandler(new Handler<Buffer>() {

					@Override
					public void handle(
						Buffer buffer) {
						String eventJson = buffer.toString();
						WebEventRecord eventRecord = gson.fromJson(eventJson, WebEventRecord.class);
						RestRecorderService.this.processEvent(eventRecord);
					}
				});
				req.response().headers().add("Access-Control-Allow-Origin", "*");
				req.response().setStatusCode(200).end();
			}
		});
		matcher.get(PATH + "/ping", new Handler<HttpServerRequest>() {

			@Override
			public void handle(
				HttpServerRequest req) {
				LOG.info("Alive ping check!");
				req.response().headers().add("Access-Control-Allow-Origin", "*");
				req.response().setStatusCode(200).end();
			}
		});
		matcher.get(PATH + "/start", new Handler<HttpServerRequest>() {

			@Override
			public void handle(
				HttpServerRequest req) {
				openRecordingBrowser("http://www.amazon.fr");
				req.response().headers().add("Access-Control-Allow-Origin", "*");
				req.response().setStatusCode(200).end();
				if(isStarted) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							while(true) {
								try {
									Thread.sleep(1000);
								}
								catch(InterruptedException e1) {
									LOG.error(e1.getMessage(), e1);
								}
								if(driver != null && isStarted) {
									final WebElement body = driver.findElement(By.tagName("body"));
									final String attribute = body.getAttribute("recording");
									if(!"true".equals(attribute)) {
										LOG.info("Re-Injecting recorder !");
										try {
											injectRecordScript();
										}
										catch(IOException e) {
											LOG.error(e.getMessage(), e);
										}
									}
								}
							}
						}
					}, "ALIVE-RECORDER-CHECKER").start();
				}
			}
		});
		matcher.get(PATH + "/stop", new Handler<HttpServerRequest>() {

			@Override
			public void handle(
				HttpServerRequest req) {
				if(driver != null) {
					driver.close();
					driver = null;
				}
				server.close();
				req.response().headers().add("Access-Control-Allow-Origin", "*");
				req.response().setStatusCode(200).end();
				System.exit(0);
			}
		});
		vertx.createHttpServer().requestHandler(matcher).listen(4444);
		server = new KryoAgentServer(this);
		LOG.info("Started !");
	}

	private void injectRecordScript()
		throws IOException {
		JavascriptExecutor executor = ((JavascriptExecutor) driver);
		final FileInputStream resourceAsStream = FileUtils.openInputStream(new File("../addons/agent/recorder.js"));
		String script = IOUtils.toString(resourceAsStream);
		String subscript = "var script = window.document.createElement('script'); script.innerHTML=\"" + script
			.replace("\r\n", "\\\r\n") + "\";window.document.head.appendChild(script);";
		executor.executeScript(subscript);
		String recordingStatus = "window.document.body.setAttribute('recording','true');";
		executor.executeScript(recordingStatus);
		LOG.info("Recorder injected !");
	}

	private static WebDriver launchBrowser(
		String host) {
		WebDriver driver = new FirefoxDriver();
		driver.get(host);
		return driver;
	}

	protected void openRecordingBrowser(
		String host) {
		if(driver == null) {
			driver = launchBrowser(host);
			try {
				Thread.sleep(5000);
				RestRecorderService.this.injectRecordScript();
				isStarted = true;
			}
			catch(InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
			catch(IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	public static void main(
		String[] args)
		throws IOException {
		final FileInputStream resourceAsStream = FileUtils.openInputStream(new File("../addons/agent/recorder.js"));
		String script = IOUtils.toString(resourceAsStream);
		LOG.info(script);
	}
}