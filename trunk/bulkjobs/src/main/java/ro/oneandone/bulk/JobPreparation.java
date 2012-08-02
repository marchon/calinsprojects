package ro.oneandone.bulk;

import java.io.Serializable;


public class JobPreparation<I extends Serializable, O extends Serializable> {
	private Class<? extends Job<I, O>> job;
	private I input;
	
	public JobPreparation() {
	}

    public JobPreparation(Class<? extends Job<I, O>> job, I input) {
        this.job = job;
        this.input = input;
    }

    public Class<? extends Job<I, O>> getJob() {
        return job;
    }

    public void setJob(Class<? extends Job<I, O>> job) {
        this.job = job;
    }

    public I getInput() {
        return input;
    }

    public void setInput(I input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "JobPreparation{" +
                "job=" + job +
                ", input=" + input +
                '}';
    }
}
