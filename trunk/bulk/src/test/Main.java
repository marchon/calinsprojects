package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.oneandone.bulk.BulkJobExecutor;
import ro.oneandone.bulk.JobPreparation;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("PRESS A KEY TO START...");
		System.in.read();
		
		BulkJobExecutor executor = getExecutor();
		List<JobPreparation<MyInputPojo>> jobs = new ArrayList<JobPreparation<MyInputPojo>>();
		
		for (int i = 0; i < 10; i++) {
			jobs.add(new JobPreparation<MyInputPojo>(MyJob.class,
					new MyInputPojo(i + 1, "The result is: ")));
		}
		
		String bulkId = executor.submitBulkJobs(jobs);
		System.out.println(bulkId);
	}
	
	private static BulkJobExecutor getExecutor() {
		return null;
	}
}
