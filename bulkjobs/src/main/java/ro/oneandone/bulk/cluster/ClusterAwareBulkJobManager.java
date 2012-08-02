package ro.oneandone.bulk.cluster;

import com.hazelcast.core.AtomicNumber;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import ro.oneandone.bulk.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 02.08.2012
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class ClusterAwareBulkJobManager implements BulkJobManager {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    public <I extends Serializable, O extends Serializable> String submitBulkJobs(List<JobPreparation<I, O>> bulkJobs) {
        AtomicNumber generator = Hazelcast.getAtomicNumber("Group-Id-Generator");
        String bulkId = Long.toString(generator.getAndAdd(1));

        //TODO: recycle jobs
        IList<JobExecutionStatus<O>> jobExecutionStatusList = Hazelcast.getList(bulkId);

        for (int i = 0; i < bulkJobs.size(); i++) {
            JobPreparation<I, O> jobPreparation = bulkJobs.get(i);

            JobExecutionStatus<O> jobExecutionStatus = new JobExecutionStatus<O>();
            jobExecutionStatusList.add(jobExecutionStatus);
            ClusterAwareJobContext<I, O> ctx = new ClusterAwareJobContext<I, O>(i, jobExecutionStatusList, jobExecutionStatus, jobPreparation.getInput());

            Runnable runnable = new JobRunnable<I, O>(jobPreparation.getJob(), ctx);

            //TODO: check if it memory gets cleared
            executorService.execute(runnable);
        }

        return bulkId;
    }

    @Override
    public List<JobExecutionStatus<?>> getBulkJobsExecutionStatus(String bulkId) {
        IList<JobExecutionStatus<?>> jobExecutionStatusList = Hazelcast.getList(bulkId);
        return Collections.unmodifiableList(jobExecutionStatusList);
    }

    private static class JobRunnable<I extends Serializable, O extends Serializable> implements Runnable {
        private Class<? extends Job<I, O>> job;
        private ClusterAwareJobContext<I, O> ctx;

        private JobRunnable(Class<? extends Job<I, O>> job, ClusterAwareJobContext<I, O> ctx) {
            this.job = job;
            this.ctx = ctx;
            
            this.ctx.setOrUpdateStatus(JobStatus.WAITING);
        }

        @Override
        public void run() {
            ctx.setOrUpdateStatus(JobStatus.STARTED);
                
            try {
                Job<I, O> jobInstance = job.newInstance();
                jobInstance.execute(ctx);
                ctx.setOrUpdateStatus(JobStatus.DONE);
            } catch (Throwable t) {
                ctx.setOrUpdateStatus(JobStatus.FAILED);    
            } finally {
            }
        }
    }
}
