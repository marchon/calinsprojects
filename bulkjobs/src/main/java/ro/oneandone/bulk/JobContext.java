package ro.oneandone.bulk;

import java.io.Serializable;


public interface JobContext<I extends Serializable, O extends Serializable> {
	public I getInput();
	public void setOrUpdateOutput(O output);
}
