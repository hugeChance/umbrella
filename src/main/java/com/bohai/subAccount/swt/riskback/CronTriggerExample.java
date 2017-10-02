/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package com.bohai.subAccount.swt.riskback;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;


/**
 * This Example will demonstrate all of the basics of scheduling capabilities of Quartz using Cron Triggers.
 * 
 * @author Bill Kratzer
 */
public class CronTriggerExample {

	private String[] strTime;
	
	private RiskManageBackView riskManageView;

	public CronTriggerExample(String[] strTime, RiskManageBackView riskManageView) {
		this.strTime = strTime;
		this.riskManageView = riskManageView;
	}

	public void run() throws Exception {
		Logger log = Logger.getLogger(CronTriggerExample.class);

		log.info("------- Initializing -------------------");

		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		log.info("------- Initialization Complete --------");

		log.info("------- Scheduling Jobs ----------------");

		// jobs can be scheduled before sched.start() has been called

		// job 1 will run every 20 seconds

		for(String timeStr : strTime){
			
			String h = timeStr.substring(0, 2);
			
			String m = timeStr.substring(3, 5);
			
			String s = timeStr.substring(6, 8);
			
			String cron = s + " " + m + " " + h + " * * ?";
			
			log.info("cron表达式："+cron);
			
			JobDetail job = newJob(SimpleJob.class).withIdentity("job"+cron, "group1").build();
			job.getJobDataMap().put("risk", riskManageView);
			
			CronTrigger trigger = newTrigger().withIdentity(cron, "group1")
					.withSchedule(cronSchedule(cron)).build();
			Date ft = sched.scheduleJob(job, trigger);
			
			log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
					+ trigger.getCronExpression());
		}


		log.info("------- Starting Scheduler ----------------");

		// All of the jobs have been added to the scheduler, but none of the
		// jobs
		// will run until the scheduler has been started
		sched.start();

		log.info("------- Started Scheduler -----------------");

		log.info("------- Waiting five minutes... ------------");

	}


}
