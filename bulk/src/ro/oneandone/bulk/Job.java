package ro.oneandone.bulk;

import java.io.Serializable;

public interface Job<T extends Serializable> {
	void execute(JobContext<T> ctx);
}
