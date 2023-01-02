package cn.morphling.core.biz.task;

import java.util.function.Consumer;

/**
 * @author kongxiangshuai
 */
public class TaskNode {

    private final String name;
    private final Consumer<JobExecutionContext> consumer;
    private TaskNode next;

    /**
     * 是否有效任务节点
     * @return 有效true/无效false
     */
    public boolean valid(JobExecutionContext context) {
        return true;
    }

    /**
     * 回滚
     * @param context 上下文参数
     * @throws JobExecutionException 异常
     */
    public void rollback(JobExecutionContext context) throws Exception {

    }

    public TaskNode(String name, Consumer<JobExecutionContext> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public void run(JobExecutionContext param) throws Exception {
        this.consumer.accept(param);
    }

    public String getName() {
        return name;
    }

    public void setNext(TaskNode next) {
        this.next = next;
    }

    public TaskNode getNext() {
        return next;
    }
}
