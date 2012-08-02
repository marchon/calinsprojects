package ro.oneandone.bulk;

import java.io.Serializable;
import java.util.List;

public interface BulkJobExecutor {
	<T extends Serializable> String submitBulkJobs(List<JobPreparation<T>> bulkJobs);
	List<JobExecutionStatus> getBulkJobsExecutionStatus(String bulkId);
}
