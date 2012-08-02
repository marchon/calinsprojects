package ro.oneandone.bulk;

import java.io.Serializable;

public enum JobStatus implements Serializable {
	WAITING, STARTED, FAILED, DONE
}
