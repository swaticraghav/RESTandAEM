package com.rest.aem.core.models;

import java.util.List;

import javax.jcr.Session;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

/* This custom step sends an email message.
 * */

@Component(service = WorkflowProcess.class, property = { "process.label=My Email Custom Step: Swati" })
public class CustomeStep implements WorkflowProcess {

	@Reference
	ResourceResolverFactory factory;

	@Reference
	MessageGatewayService service;

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {

		Email email = new SimpleEmail();
		try {
			email.addTo("swaticraghav@gmail.com");
			email.addCc("Suprabhat.Tiwari@epsilon.com");
			email.setFrom("swati.raghav@publicissapient.com");
			email.setSubject("AEM Custom Step");
			email.setMsg("This is an email to tell you that you are awesome!");
		} catch (EmailException e) {
			e.printStackTrace();
		}
		MessageGateway<Email> gateway = service.getGateway(Email.class);
		gateway.send(email);

	}

}
