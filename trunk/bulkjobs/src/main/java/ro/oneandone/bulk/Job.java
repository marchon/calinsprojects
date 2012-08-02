package ro.oneandone.bulk;

import java.io.Serializable;

public interface Job<I extends Serializable, O extends Serializable> {
	void execute(JobContext<I, O> ctx);
}
