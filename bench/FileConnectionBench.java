package benchmark;

import com.sun.cldchi.jvm.JVM;
import com.sun.midp.crypto.SecureRandom;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.mozilla.MemorySampler;

public class FileConnectionBench {
  private static final byte[] b = new byte[1024];

  void runBenchmark() {
    try {
      long start;

      String privateDir = System.getProperty("fileconn.dir.private");
      String filename = String.valueOf(System.currentTimeMillis());

      start = JVM.monotonicTimeMillis();
      FileConnection file = (FileConnection)Connector.open(privateDir + filename);
      System.out.println("Writing to file " + privateDir + filename);
      file.create();
      System.out.println("FileConnection.open/create time: " + (JVM.monotonicTimeMillis() - start));

      start = JVM.monotonicTimeMillis();
      OutputStream out = file.openOutputStream();
      System.out.println("FileConnection.openOutputStream time: " + (JVM.monotonicTimeMillis() - start));

      start = JVM.monotonicTimeMillis();
      for (int i = 0; i < 1000; i++) {
        out.write(b);
      }
      System.out.println("OutputStream.write time: " + (JVM.monotonicTimeMillis() - start));

      out.close();
      file.close();
    } catch (Exception e) {
      System.out.println("Unexpected exception: " + e);
      e.printStackTrace();
    }
  }

  public static void main(String args[]) {
    try {
      SecureRandom rnd = SecureRandom.getInstance(SecureRandom.ALG_SECURE_RANDOM);
      rnd.nextBytes(b, 0, 1024);
    } catch (Exception e) {
      System.out.println("Unexpected exception: " + e);
      e.printStackTrace();
    }

    FileConnectionBench bench = new FileConnectionBench();

    MemorySampler.sampleMemory();
    bench.runBenchmark();
    MemorySampler.sampleMemory();
  }
}
