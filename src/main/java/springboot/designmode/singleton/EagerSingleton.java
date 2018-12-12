package springboot.designmode.singleton;

/**
 * @author: tangjie03@baidu.com
 * @Date: 2018-12-12 15:00
 * @Description: 饿汉模式
 */
public class EagerSingleton {

    private EagerSingleton() {
    }

    private static EagerSingleton instance = new EagerSingleton();

    private static EagerSingleton getInstance() {
        return instance;
    }
}
