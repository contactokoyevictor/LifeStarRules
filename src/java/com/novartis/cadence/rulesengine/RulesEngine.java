package com.novartis.cadence.rulesengine;

import com.novartis.cadence.database.User;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;


public class RulesEngine {

//=========================================================================================

	private KnowledgeBase kbase = null;
	StatefulKnowledgeSession ksession = null;
	KnowledgeRuntimeLogger logger = null;

//=========================================================================================
	public RulesEngine()
	{
		try{
			// load up the knowledge base
			kbase = readKnowledgeBase();
			ksession = kbase.newStatefulKnowledgeSession();
			logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
//=========================================================================================
	public void invokeRules(User user)
	{
		try{
			ksession.insert(user);
			System.out.println("Rules fired for User " + user.getUserID() + "\nWeek of Month : "  + user.getWeekOfMonth());
			System.out.println("Segment Code : " + user.getSegmentCode());
			System.out.println("Number of Weeks : " + user.getNumberOfWeek());
			System.out.println("Day of week : " + user.getDayOfWeek());
			ksession.fireAllRules();
			logger.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
//=========================================================================================

	private  KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("Cadence.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error: errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
//==========================================================================================
}
