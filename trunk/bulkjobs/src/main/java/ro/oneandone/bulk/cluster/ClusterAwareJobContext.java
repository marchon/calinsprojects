package ro.oneandone.bulk.cluster;

import com.hazelcast.core.IList;
import ro.oneandone.bulk.JobContext;
import ro.oneandone.bulk.JobExecutionStatus;
import ro.oneandone.bulk.JobStatus;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 02.08.2012
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class ClusterAwareJobContext<I extends Serializable, O extends Serializable> implements JobContext<I, O> {
    private int jobIndex;
    private IList<JobExecutionStatus<O>> jobExecutionStatusList;
    private JobExecutionStatus<O> jobExecutionStatus;
    private I input;

    public ClusterAwareJobContext(int jobIndex, IList<JobExecutionStatus<O>> jobExecutionStatusList, JobExecutionStatus<O> jobExecutionStatus, I input) {
        this.jobIndex = jobIndex;
        this.jobExecutionStatusList = jobExecutionStatusList;
        this.jobExecutionStatus = jobExecutionStatus;
        this.input = input;
    }

    @Override
    public I getInput() {
        return input;
    }

    @Override
    public void setOrUpdateOutput(O output) {
        jobExecutionStatus.setOutput(output);

        //update it on cluster
        jobExecutionStatusList.set(jobIndex, jobExecutionStatus);
    }

    public void setOrUpdateStatus(JobStatus jobStatus) {
        jobExecutionStatus.setStatus(jobStatus);

        //update it on cluster
        jobExecutionStatusList.set(jobIndex, jobExecutionStatus);
    }
}
