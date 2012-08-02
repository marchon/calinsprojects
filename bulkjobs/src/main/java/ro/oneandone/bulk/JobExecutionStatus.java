package ro.oneandone.bulk;

import java.io.Serializable;

public class JobExecutionStatus<O> implements Serializable {
	private JobStatus status;
	private O output;

	public JobExecutionStatus() {
	}

    public JobExecutionStatus(O output, JobStatus status) {
        this.output = output;
        this.status = status;
    }

    public O getOutput() {
        return output;
    }

    public void setOutput(O output) {
        this.output = output;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobExecutionStatus{" +
                "status=" + status +
                ", output=" + output +
                '}';
    }
}
