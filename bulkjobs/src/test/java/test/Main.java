package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hazelcast.core.Hazelcast;
import ro.oneandone.bulk.BulkJobManager;
import ro.oneandone.bulk.JobPreparation;
import ro.oneandone.bulk.cluster.ClusterAwareBulkJobManager;

public class Main {
	public static void main(String[] args) throws IOException {
        Hazelcast.getDefaultInstance();
		System.out.println("PRESS A KEY TO START...");
		System.in.read();
		
		BulkJobManager manager = getExecutor();
		List<JobPreparation<MyInputPojo, MyOutputPojo>> jobs = new ArrayList<JobPreparation<MyInputPojo, MyOutputPojo>>();
		
		for (int i = 0; i < 10; i++) {
			jobs.add(new JobPreparation<MyInputPojo, MyOutputPojo>(MyJob.class,
					new MyInputPojo(i + 1, "The result is: ")));
		}
		
		String bulkId = manager.submitBulkJobs(jobs);
		System.out.println(bulkId);
	}
	
	private static BulkJobManager getExecutor() {
		return new ClusterAwareBulkJobManager();
	}
}
