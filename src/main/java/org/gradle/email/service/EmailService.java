package org.gradle.email.service;

import java.util.List;
import java.util.Locale;

public interface EmailService {
	public void sendMailAttatchmentFile(String[] emails, String[] ccEmails, Locale locale, String subjectCode, String contentCode, String[] msgArguments, List<String> attatchmentFileNames);

}
