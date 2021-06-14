package cn.gong.easyetl.sync.channel;

import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.sync.entity.Record;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用有界阻塞队列，作为中间缓冲
 * @author gongxin
 * @date 2020/9/15
 */
@Slf4j
public class MemoryChannel implements Channel<Record>{

    private ArrayBlockingQueue<Record> queue;

    private ReentrantLock lock;

    private Condition produce, consumer;

    private Integer batchSize;

    public MemoryChannel(Configuration configuration) {
        this.queue = new ArrayBlockingQueue<>(configuration.getCapacity());
        batchSize = configuration.getBatchSize();
        lock = new ReentrantLock();
        produce = lock.newCondition();
        consumer = lock.newCondition();
    }

    @Override
    public void push(Record record) {
        try {
            this.queue.put(record);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("push data 中断异常！");
        }
    }

    @Override
    public void pushAll(Collection<Record> records) {
        try {
            lock.lockInterruptibly();
            while (records.size() > this.queue.remainingCapacity()) {
                produce.await(100L, TimeUnit.MILLISECONDS);
            }
            this.queue.addAll(records);
            consumer.signalAll();
        } catch (InterruptedException e) {
            log.error("push all data 中断异常！");
            Thread.currentThread().interrupt();
            throw new IllegalStateException();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public Record pull() {
        try {
            return this.queue.take();
        } catch (InterruptedException e) {
            log.error("pull data 中断异常！");
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void pullAll(Collection<Record> records) {
        assert records != null;
        records.clear();
        try {
            lock.lockInterruptibly();
            while (this.queue.drainTo(records, batchSize) <= 0) {
                consumer.await(100L, TimeUnit.MILLISECONDS);
            }
            produce.signalAll();
        } catch (InterruptedException e) {
            log.error("pull all data 中断异常！");
            Thread.currentThread().interrupt();
            throw new IllegalStateException();
        }  finally {
            lock.unlock();
        }
    }
}
