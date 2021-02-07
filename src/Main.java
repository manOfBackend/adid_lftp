import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    final static int threadCount = 4;

    public static void main(String[] args) throws IOException {

        final long fileLength = getFileLength("D:\\1200x1920-Wallpapers-029.jpg");

        System.out.println(fileLength);

        List<FileChunk> fileChunks = new ArrayList<>();

        long sizePerChunk = (long) Math.ceil((double) fileLength / threadCount);

        long offset = 0;

        for (int i = 0; i < threadCount; i++) {
            FileChunk fileChunk = new FileChunk("d:\\1200x1920-Wallpapers-029.jpg", offset,
                    Math.min(offset + sizePerChunk, fileLength));
            offset += sizePerChunk;
            fileChunks.add(fileChunk);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        while(true) {
            List<FileChunk> remains = new ArrayList<>();

            for (FileChunk fc : fileChunks) {
                if (fc.getOffset() < fc.getLimit()){
                    System.out.println(fc.getOffset() + ", " + fc.getLimit());
                    remains.add(fc);
                }

            }
            if (remains.isEmpty()) break;

            for (FileChunk fc : remains) {
                FileIO.getByteFromChunk(fc, executorService).thenAccept((bytes -> {
                    // TODO: Socket 전송
                    System.out.println("완료");
                }));
            }

        }
        executorService.shutdown();








    }

    private static long getFileLength(String fileName) {
        File file = new File(fileName);

        return file.length();
    }


}
