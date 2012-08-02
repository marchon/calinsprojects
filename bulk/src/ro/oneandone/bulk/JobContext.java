package ro.oneandone.bulk;

import java.io.Serializable;


public interface JobContext<T extends Serializable> {
	public T getInput();
	public <P extends Serializable> void putOutputAttribute(String name, P value);
}
