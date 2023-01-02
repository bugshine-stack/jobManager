package cn.morphling.core.biz.task;

/**
 * @author kongxiangshuai
 */
public class JobExecutionException extends Exception {

    public JobExecutionException() {
        super();
    }

    public JobExecutionException(Throwable throwable) {
        super(throwable);
    }

    public JobExecutionException(String message) {
        super(message);
    }

    public JobExecutionException(JobExecutionException e1) {
        super(e1);
    }
}
