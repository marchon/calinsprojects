package test;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import ro.oneandone.bulk.BulkJobExecutor;
import ro.oneandone.bulk.JobExecutionStatus;

public class Main1 {
	public static void main(String[] args) throws IOException {
		System.out.println("Type bulk id: ");
		Scanner sc = new Scanner(System.in);
		String id = sc.next();
		
		BulkJobExecutor executor = getExecutor();
		
		do {
			List<JobExecutionStatus> statuses = executor.getBulkJobsExecutionStatus(id);
			System.out.println(statuses);
		} while(!sc.next().equals("."));
		
		System.exit(0);
	}

	private static BulkJobExecutor getExecutor() {
		return null;
	}
}
