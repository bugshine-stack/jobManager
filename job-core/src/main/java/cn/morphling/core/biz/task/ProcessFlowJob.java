package cn.morphling.core.biz.task;

import java.util.function.Consumer;

/**
 * @author kongxiangshuai
 */
public class ProcessFlowJob implements Job {

    private TaskNode head;
    private TaskNode tail;
    private int size;

    private boolean terminal = false;
    
    /**
     * 处理异常
     * @param taskNode    节点
     * @param context 上下文参数
     * @param e       Exception
     */
    public void except(TaskNode taskNode, JobExecutionContext context, Exception e) {}

    public void terminal() {
        this.terminal = true;
    }

    public ProcessFlowJob() {
    }

    public TaskNode next(String name, Consumer<JobExecutionContext> consumer) {
        return next(new TaskNode(name, consumer));
    }

    public TaskNode next(TaskNode node) {
        if (this.size == 0){
            head = node;
            tail = head;
        }else{
            TaskNode last = tail;
            last.setNext(node);
            this.tail = node;
        }
        this.size ++;
        return node;
    }

    /**
     * 运行任务
     * @throws JobExecutionException 异常
     */
    private void processNode(TaskNode taskNode, JobExecutionContext context) throws JobExecutionException{
        try {
            if (taskNode.valid(context)) {
                taskNode.run(context);
            }
        } catch (Exception e) {
            try {
                taskNode.rollback(context);
            } catch (Exception ex) {
                throw new JobExecutionException(ex.getMessage());
            }
            throw new JobExecutionException(e.getMessage());
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TaskNode tmp = head;
        while (!terminal && tmp != null) {
            try {
                processNode(tmp, context);
            } catch (JobExecutionException e) {
                except(tmp, context, e);
                break;
            }
            tmp = tmp.getNext();
        }
    }
}
