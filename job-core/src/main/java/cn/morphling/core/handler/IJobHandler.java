package cn.morphling.core.handler;

/**
 * @author kongxiangshuai
 */
public abstract class IJobHandler {

    /**
     * 执行
     * @throws Exception
     */
    public abstract void execute() throws Exception;

    public void init() throws Exception {

    }

    public void destroy() throws Exception {

    }

}
