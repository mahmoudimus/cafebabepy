package org.cafebabepy.evaluter.Interpret;

import java.util.*;

/**
 * Created by yotchang4s on 2018/09/01.
 */
public abstract class Yielder<T> {
    private ThreadLocal<Context<T>> context = new ThreadLocal<>();

    public static <T> YielderIterable<T> newIterable(final Yielder<T> yielder) {
        return new YielderIterable<>(yielder);
    }

    protected final void yield(T returnValue) {
        this.context.get().yield(returnValue);
    }

    public abstract void run();

    private static class Context<T> {
        private final Queue<T> queue;
        private final Object lock = new Object();

        private boolean endReceived;
        private RuntimeException thrownException;

        Context() {
            this.queue = new ArrayDeque<>();
        }

        synchronized void yield(T returnValue) {
            try {
                if (this.endReceived) {
                    throw new IllegalStateException("yield() end has been called.");
                }

                this.queue.offer(returnValue);

            } catch (RuntimeException e) {
                this.thrownException = e;
            }
        }

        synchronized void yieldEnd() {
            synchronized (lock) {
                this.endReceived = true;
                lock.notifyAll();
            }
        }

        T pop() {
            return queue.poll();
        }
    }

    public static class YielderIterable<T> implements Iterable<T> {
        private final Context<T> context;
        private final Thread thread;

        private boolean iteratorCalled;

        private YielderIterable(Yielder<T> yielder) {
            this.context = new Context<>();
            this.thread = new Thread(() -> {
                try {
                    yielder.context.set(YielderIterable.this.context);
                    yielder.run();

                } catch (RuntimeException e) {
                    if (this.context.thrownException != null) {
                        e.addSuppressed(this.context.thrownException);
                    }

                    this.context.thrownException = e;

                } finally {
                    context.yieldEnd();
                    yielder.context.remove();
                }
            });

            this.thread.start();
        }

        @Override
        public synchronized Iterator<T> iterator() {
            if (iteratorCalled) {
                throw new IllegalStateException("iterator() has been called");
            }

            iteratorCalled = true;

            return new Iterator<T>() {
                T nextItem;

                @Override
                public boolean hasNext() {
                    if (!context.endReceived) {
                        synchronized (context.lock) {
                            if (!context.endReceived) {
                                try {
                                    context.lock.wait();

                                } catch (InterruptedException ignore) {
                                }
                            }
                        }
                    }

                    if (nextItem != null) {
                        return true;
                    }

                    nextItem = context.pop();

                    return nextItem != null;
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }

                    T result = nextItem;
                    nextItem = null;

                    return result;
                }
            };
        }

        public final Optional<RuntimeException> thrownException() {
            RuntimeException e = this.context.thrownException;
            if(e == null) {
                return Optional.empty();
            }

            return Optional.of(e);
        }
    }
}
