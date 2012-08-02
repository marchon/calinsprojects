package ro.oneandone.bulk;

import java.io.Serializable;
import java.util.Map;

public class JobExecutionStatus {
	private JobStatus status;
	private Map<String, ? extends Serializable> outputAttributes;

	public JobExecutionStatus() {
	}

	public JobExecutionStatus(JobStatus status,
			Map<String, ? extends Serializable> outputAttributes) {
		super();
		this.status = status;
		this.outputAttributes = outputAttributes;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public Map<String, ? extends Serializable> getOutputAttributes() {
		return outputAttributes;
	}

	public void setOutputAttributes(
			Map<String, ? extends Serializable> outputAttributes) {
		this.outputAttributes = outputAttributes;
	}

	@Override
	public String toString() {
		return "JobExecutionStatus [status=" + status + ", outputAttributes="
				+ outputAttributes + "]";
	}
}
