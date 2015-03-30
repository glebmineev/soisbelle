package ru.spb.soisbelle.showcase

/**
 * Created by gleb on 24.11.14.
 */
public final class ArrayChunkUtils {

  public static long[][] chunkArray(long[] array, int chunkSize) {
    int numOfChunks = (int)Math.ceil((double)array.length / chunkSize);
    long[][] output = new long[numOfChunks][];

    for(int i = 0; i < numOfChunks; ++i) {
      int start = i * chunkSize;
      int length = Math.min(array.length - start, chunkSize);

      long[] temp = new long[length];
      System.arraycopy(array, start, temp, 0, length);
      output[i] = temp;
    }

    return output;
  }

}
