package ro.oneandone.bulk;

import java.io.Serializable;


public class JobPreparation<T extends Serializable> {
	private Class<? extends Job<T>> job;
	private T input;
	
	public JobPreparation() {
	}

	public JobPreparation(Class<? extends Job<T>> job, T input) {
		super();
		this.job = job;
		this.input = input;
	}

	public Class<? extends Job<T>> getJob() {
		return job;
	}

	public void setJob(Class<? extends Job<T>> job) {
		this.job = job;
	}

	public T getInput() {
		return input;
	}

	public void setInput(T input) {
		this.input = input;
	}
}
