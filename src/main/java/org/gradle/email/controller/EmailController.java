package org.gradle.email.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.gradle.common.enums.Territory;
import org.gradle.common.util.DateTimeUtils;
import org.gradle.common.util.LocaleUtils;
import org.gradle.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {
	
	@Value("${common.dirs.tmp-dir}") 
    private String tmpDir;
	
	@Value("${common.email.to}")
	private String[] emails;
	
	@Value("${common.email.cc}")
	private String[] ccEmails;
	
	@Autowired
	private EmailService emailservice;
	
	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public @ResponseBody String email() {
		
		String fileName = "attachmentFileTest.txt";
		File file = new File(tmpDir, fileName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String dataEndDate = DateTimeUtils.convertToDateText(cal.getTime());
		
		String subjectCode = "bi.tvodandsvod.dailystatistics.subject";
		String contentCode = "bi.tvodandsvod.dailystatistics.content";
		String[] msgArguments = {dataEndDate};
		List <String> attatchmentFileNames = new ArrayList<String>();
		attatchmentFileNames.add(file.getPath());
		Locale locale = LocaleUtils.getLocale(Territory.TW);
		emailservice.sendMailAttatchmentFile(emails, ccEmails, locale, subjectCode, contentCode, msgArguments, attatchmentFileNames);		
		return "send mail test!!!";
	}
}
