import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class FileChunk {

    private long offset;

    private long limit;

    private final static int bufferSize = 4 * 1024;

    private final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

    private FileChannel channel = null;

    public FileChunk(String fileName, long offset, long limit) {
        this.offset = offset;
        this.limit = limit;

        Path path = Paths.get(fileName);

        try {
            channel = FileChannel.open(path, StandardOpenOption.READ);
            channel.position(offset);




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Optional<byte[]> getBuffer() {
        offset += bufferSize;
        try {
            int r = channel.read(buffer);
            if (r == -1) {
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        int remain = buffer.remaining();
        if (offset + remain > limit) {
            int l = (int)(limit - offset);
            buffer.limit(l);
        }
        final byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println(bytes.length);
        buffer.clear();
        return Optional.of(bytes);
    }


    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public FileChannel getChannel() {
        return channel;
    }

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
