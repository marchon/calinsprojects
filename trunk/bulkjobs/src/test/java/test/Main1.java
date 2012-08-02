package test;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.hazelcast.core.Hazelcast;
import ro.oneandone.bulk.BulkJobManager;
import ro.oneandone.bulk.JobExecutionStatus;
import ro.oneandone.bulk.cluster.ClusterAwareBulkJobManager;

public class Main1 {
	public static void main(String[] args) throws IOException {
        Hazelcast.getDefaultInstance();
		System.out.println("Type bulk id: ");
		Scanner sc = new Scanner(System.in);
		String id = sc.next();
		
		BulkJobManager manager = getExecutor();
		
		do {
			List<JobExecutionStatus<?>> statuses = manager.getBulkJobsExecutionStatus(id);
			for (JobExecutionStatus<?> jobStat : statuses) {
                System.out.println(jobStat);
            }
		} while(sc.next().trim().equals("."));
		
		System.exit(0);
	}

	private static BulkJobManager getExecutor() {
		return new ClusterAwareBulkJobManager();
	}
}
