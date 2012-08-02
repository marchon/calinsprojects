package ro.oneandone.bulk;

import java.io.Serializable;
import java.util.List;

public interface BulkJobManager {
	<I extends Serializable, O extends Serializable> String submitBulkJobs(List<JobPreparation<I, O>> bulkJobs);
	List<JobExecutionStatus<?>> getBulkJobsExecutionStatus(String bulkId);
}
