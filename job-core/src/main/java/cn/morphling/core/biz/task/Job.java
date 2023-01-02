package cn.morphling.core.biz.task;

/**
 * @author kongxiangshuai
 */
public interface Job {

    /**
     * 执行
     * @param arg0 参数
     * @throws JobExecutionException 异常
     */
    void execute(JobExecutionContext arg0) throws JobExecutionException;

}
