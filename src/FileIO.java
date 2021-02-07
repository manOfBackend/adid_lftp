import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FileIO {
    public static CompletableFuture<Void> getByteFromChunk(FileChunk chunk, ExecutorService executorService) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            Optional<byte[]> buffer = chunk.getBuffer();
            int len = buffer.get().length;

            System.out.println(Thread.currentThread().getName() + ", File I/O Process " + buffer.get().hashCode());
            return buffer;
        }, executorService).thenAcceptAsync((bytes -> {
            // TODO: 소켓 전송
            System.out.println(Thread.currentThread().getName() + ", Socket Process " + bytes.hashCode());
        }), executorService);

        /*
        new Thread(() -> {
            Optional<byte[]> buffer = chunk.getBuffer();
            System.out.println(Thread.currentThread().getName() + ", getByteFromChunk");
            future.complete(buffer);
        }).start();

         */
        return future;
    }
}
