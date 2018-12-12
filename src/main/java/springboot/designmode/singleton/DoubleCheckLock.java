package springboot.designmode.singleton;

/**
 * @author: tangjie03@baidu.com
 * @Date: 2018-12-12 15:01
 * @Description: 双检器
 */
public class DoubleCheckLock {
    private static DoubleCheckLock instance = null;

    private DoubleCheckLock() {
    }

    public static DoubleCheckLock getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLock.class) {
                if (instance == null) {
                    instance = new DoubleCheckLock();
                }
            }
        }
        return instance;
    }

}
