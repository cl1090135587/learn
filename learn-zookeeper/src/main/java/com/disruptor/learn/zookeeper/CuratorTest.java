package com.disruptor.learn.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Executors;

public class CuratorTest {
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.16.83.252:12181")
            .sessionTimeoutMs(50000)
            .connectionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        /**
         * 创建会话
         */
        client.start();

        /**
         * 创建节点
         * 注意:
         * 1 除非指明创建节点的类型,默认是持久节点
         * 2 ZooKeeper规定:所有非叶子节点都是持久节点,所以递归创建出来的节点,只有最后的数据节点才是指定类型的节点,其父节点是持久节点
         */
        //创建一个初始内容为空的节点
        client.create().forPath("/China");
        client.create().forPath("/America", "zhangsan".getBytes());
        //创建一个初始内容为空的临时节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/France");
        //递归创建,/Russia是持久节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/Russia/car", "haha".getBytes());

        /**
         * 异步创建节点
         * 注意:如果自己指定了线程池,那么相应的操作就会在线程池中执行,如果没有指定,那么就会使用Zookeeper的EventThread线程对事件进行串行处理
         */
        client.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("当前线程:" + Thread.currentThread().getName() + ",code:" + event.getResultCode()
                        + ",type:" + event.getType());
            }
        }, Executors.newFixedThreadPool(10)).forPath("/async-curator-my");

        client.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("当前线程:" + Thread.currentThread().getName() + ",code:" + event.getResultCode()
                        + ",type:" + event.getType());
            }
        }).forPath("/async-curator-zookeeper");

        /**
         * 获取节点内容
         */
        byte[] data = client.getData().forPath("/America");
        System.out.println(new String(data));
        //传入一个旧的stat变量,来存储服务端返回的最新的节点状态信息
        byte[] data2 = client.getData().storingStatIn(new Stat()).forPath("/America");
        System.out.println(new String(data2));
        /**
         * 更新数据
         */
//        Stat stat = client.setData().forPath("/America");
//        client.setData().withVersion(4).forPath("/America", "lisi".getBytes());

        /**
         * 删除节点
         */
        //只能删除叶子节点
//        client.delete().forPath("/China");
//        //删除一个节点,并递归删除其所有子节点
//        client.delete().deletingChildrenIfNeeded().forPath("/Russia");
//        //强制指定版本进行删除
//        client.delete().withVersion(5).forPath("/America");
//        //注意:由于一些网络原因,上述的删除操作有可能失败,使用guaranteed(),如果删除失败,会记录下来,只要会话有效,就会不断的重试,直到删除成功为止
//        client.delete().guaranteed().forPath("/America");
//
//        Thread.sleep(Integer.MAX_VALUE);
    }
}
