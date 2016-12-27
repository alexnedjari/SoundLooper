package com.soundlooper.system.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.system.preferences.SoundLooperProperties;

public class IssueSender {

	private static Logger logger = LogManager.getLogger(IssueSender.class);

	public static GHIssue sendIssue(Throwable e) {
		logger.info("Issue creation started");
		String title = "Fatal error : " + e.toString();
		String content = getContent();
		return sendIssue(title, content);
	}

	private static String getContent() {
		String content = "Error on version : " + SoundLooperProperties.getInstance().getVersion() + "\n\n";
		content += "Complete log : \n";
		try {
			content += FileUtils.readFileToString(new File("log" + File.separator + "output.log"), "UTF-8");
			logger.info("Content created (" + content.length() + " o)");
			return content;
		} catch (IOException e1) {
			return "Unable to read log file : " + e1.toString();
		}
	}

	public static GHIssue sendIssue(String title, String body) {
		try {
			logger.info("Github connection");
			GitHub github = GitHub.connectUsingPassword("an-soundlooper", "mdpIntrouvable123");
			logger.info("Get the repository");
			GHRepository repository = github.getRepository("alexnedjari/SoundLooper");
			logger.info("Before create issue");
			GHIssueBuilder issueBuilder = repository.createIssue(title);
			issueBuilder.body(body);
			issueBuilder.label("bug");
			logger.info("After create issue");
			GHIssue issue = issueBuilder.create();
			logger.info("After send issue");
			return issue;
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Impossible de créer le ticket gitHub", e);
		}
	}
}
