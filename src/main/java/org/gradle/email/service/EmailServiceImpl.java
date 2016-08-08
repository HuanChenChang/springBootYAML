package org.gradle.email.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Value("${common.email.enabled}")
    private boolean enabled;
	
	@Value("${common.email.from}")
    private String mailFrom;
    
    @Value("${common.email.from.alias}")
    private String mailFromAlias;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
    private MessageSource messageSource;
    	
	public void sendMailAttatchmentFile(String[] emails, String[] ccEmails, Locale locale, String subjectCode, String contentCode, String[] msgArguments, List<String> attatchmentFileNames){
		LOG.info("sendMail start ");
		
		if (!enabled) {
            LOG.warn("email function is disabled.");
            return;
        }
        
		if(emails == null || emails.length <= 0) {
            LOG.warn("email address is empty.");
            return;
        }
        
        if(ccEmails == null) {
        	ccEmails = new String[0];
        }
        String subjectMessage = messageSource.getMessage(subjectCode, msgArguments, locale);
        String contentMessage = messageSource.getMessage(contentCode, msgArguments, locale);
        try {
			MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            messageHelper.setSubject(subjectMessage);
            messageHelper.setTo(emails);
            messageHelper.setCc(ccEmails);
            messageHelper.setFrom(new InternetAddress(mailFrom, mailFromAlias));            
            messageHelper.setText(contentMessage);
            
            for (String fileName : attatchmentFileNames) {
            	FileSystemResource file = new FileSystemResource(fileName);
            	messageHelper.addAttachment(file.getFilename(), file);
            }
            
            this.mailSender.send(mimeMessage);          
            
            LOG.debug("send email with subject '{}' to {} cc {}", subjectMessage, Arrays.toString(emails), Arrays.toString(ccEmails));
 
		} catch (Exception ex) {
            LOG.error("send email failed to '{}' with subject '{}' and content '{}'", Arrays.toString(emails), subjectMessage, contentMessage, ex);
            ex.printStackTrace();
        }
		LOG.info("sendMail end ");
        
	}
}
