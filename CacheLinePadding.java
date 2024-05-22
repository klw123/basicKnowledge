package 缓存行问题;

/**
 * @Description 缓存行对齐。参考链接：https://juejin.cn/post/7070041824596852749
 * @Author klw123
 * @Date 2024/5/22 15:42
 **/
public class CacheLinePadding {
    // 把整个T类大小做成64字节，刚好占一个完整缓存行
    public static class T {
        private long p1, p2, p3, p4, p5, p6, p7;// 占7*8字节，为了缓存行对齐
        private long x=0L;// 占8字节
    }

    // 初始化两个T对象
    // array[0] 和array[1] 在不同的缓存行中
    private static T[] array = new T[2];
    static {
        array[0] = new T();
        array[1] = new T();
    }

    public static void main(String args[]) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 200000000; i+=2){
                array[0].x = i;
            }
            System.out.println("线程1循环遍历结束，array[1].x值为：");
            System.out.println(array[0].x);
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0; i < 200000000; i+=2){
                array[1].x = i;
            }
            System.out.println("线程1循环遍历结束，array[1].x值为：");
            System.out.println(array[1].x);
        });

        long startTime=System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long endTime=System.nanoTime();
        System.out.println("耗时："+(endTime-startTime)/1000000+"ms");
        System.out.println("array[0].x值为：");
        System.out.println(array[0].x);
        System.out.println("array[1].x值为：");
        System.out.println(array[1].x);

    }



}
