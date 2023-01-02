package cn.morphling.core.biz;

import cn.morphling.core.biz.task.JobExecutionContext;
import cn.morphling.core.biz.task.JobExecutionException;
import cn.morphling.core.biz.task.ProcessFlowJob;
import cn.morphling.core.biz.task.TaskNode;

import java.util.*;

/**
 * @author kongxiangshuai
 */
public class TaskMain {

    static class InquiryJobExecutionContext extends JobExecutionContext {

        Map<String, Object> param = new HashMap<>();
        boolean getValid() {
            return true;
        }

        public Map<String, Object> getParam() {
            return param;
        }

        public void setParam(Map<String, Object> param) {
            this.param = param;
        }
    }

    public static void main(String[] args) throws JobExecutionException {
        InquiryJobExecutionContext context = new InquiryJobExecutionContext();
        context.setParam(Collections.singletonMap("test", 1));
        ProcessFlowJob flowJob = new ProcessFlowJob() {
            @Override
            public void except(TaskNode taskNode, JobExecutionContext context, Exception e) {
                System.out.printf("task [%s] error, execute except [%s] \n", taskNode.getName(), e.getMessage());
            }
        };

        flowJob.next("stage1", c -> System.out.println("test stage1"));
        flowJob.next(new TaskNode("stage2", null) {
            @Override
            public boolean valid(JobExecutionContext context) {
                InquiryJobExecutionContext context1 = (InquiryJobExecutionContext) context;
                return context1.getValid();
            }

            @Override
            public void run(JobExecutionContext param) throws Exception {
                try {
                    InquiryJobExecutionContext param1 = (InquiryJobExecutionContext) param;
                    Map<String, Object> p = new HashMap<>(param1.getParam());
//                    Map<String, Object> p = param1.getParam();
                    p.put("xxx", 123);
                    param1.setParam(p);
                    System.out.println(param1.getParam());
                } catch (Exception e) {
                    throw new Exception(e.getMessage(), e.getCause());
                }
            }
        });
        flowJob.next(new TaskNode("compute", null) {

            @Override
            public boolean valid(JobExecutionContext context) {
                return false;
            }

            @Override
            public void run(JobExecutionContext param) throws Exception {
                System.out.println("mock exception ");
                throw new RuntimeException();
            }

            @Override
            public void rollback(JobExecutionContext context) throws Exception {
                System.out.printf("[%s] execute rollback \n", getName());
            }
        });

        flowJob.next(new TaskNode("save", null) {
            @Override
            public void run(JobExecutionContext param) throws Exception {
                InquiryJobExecutionContext param1 = (InquiryJobExecutionContext) param;
                System.out.printf("save [%s]\n", param1.getParam());
            }

            @Override
            public void rollback(JobExecutionContext context) throws Exception {
                System.out.printf("[%s] execute rollback \n", getName());
            }
        });

        flowJob.execute(context);
    }
}
