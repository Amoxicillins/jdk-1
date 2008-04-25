/*
 * Copyright 2008 Sun Microsystems, Inc.  All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * @test
 * @bug 6661247
 * @summary Internal bug in 32-bit HotSpot optimizer while bit manipulations
 */

import java.util.Random;
import java.nio.*;

// This isn't a completely reliable test for 6661247 since the results
// depend on what the local schedule looks like but it does reproduce
// the issue in current builds.

public class Test {

    public static void test(boolean[] src, int srcPos, LongBuffer dest, long destPos, int count) {
        int countStart = (destPos & 63) == 0 ? 0 : 64 - (int)(destPos & 63);
        if (countStart > count)
            countStart = count;
        for (int srcPosMax = srcPos + countStart; srcPos < srcPosMax; srcPos++, destPos++) {
            if (src[srcPos])
                dest.put((int)(destPos >>> 6), dest.get((int)(destPos >>> 6)) | 1L << (destPos & 63));
            else
                dest.put((int)(destPos >>> 6), dest.get((int)(destPos >>> 6)) & ~(1L << (destPos & 63)));
        }
        count -= countStart;
        int cnt = count >>> 6;
        for (int k = (int)(destPos >>> 6), kMax = k + cnt; k < kMax; k++) {
            int low = (src[srcPos] ? 1 : 0)
                | (src[srcPos + 1] ? 1 << 1 : 0)
                | (src[srcPos + 2] ? 1 << 2 : 0)
                | (src[srcPos + 3] ? 1 << 3 : 0)
                | (src[srcPos + 4] ? 1 << 4 : 0)
                | (src[srcPos + 5] ? 1 << 5 : 0)
                | (src[srcPos + 6] ? 1 << 6 : 0)
                | (src[srcPos + 7] ? 1 << 7 : 0)
                | (src[srcPos + 8] ? 1 << 8 : 0)
                | (src[srcPos + 9] ? 1 << 9 : 0)
                | (src[srcPos + 10] ? 1 << 10 : 0)
                | (src[srcPos + 11] ? 1 << 11 : 0)
                | (src[srcPos + 12] ? 1 << 12 : 0)
                | (src[srcPos + 13] ? 1 << 13 : 0)
                | (src[srcPos + 14] ? 1 << 14 : 0)
                | (src[srcPos + 15] ? 1 << 15 : 0)
                | (src[srcPos + 16] ? 1 << 16 : 0)
                | (src[srcPos + 17] ? 1 << 17 : 0)
                | (src[srcPos + 18] ? 1 << 18 : 0)
                | (src[srcPos + 19] ? 1 << 19 : 0)
                | (src[srcPos + 20] ? 1 << 20 : 0)
                | (src[srcPos + 21] ? 1 << 21 : 0)
                | (src[srcPos + 22] ? 1 << 22 : 0)
                | (src[srcPos + 23] ? 1 << 23 : 0)
                | (src[srcPos + 24] ? 1 << 24 : 0)
                | (src[srcPos + 25] ? 1 << 25 : 0)
                | (src[srcPos + 26] ? 1 << 26 : 0)
                | (src[srcPos + 27] ? 1 << 27 : 0)
                | (src[srcPos + 28] ? 1 << 28 : 0)
                | (src[srcPos + 29] ? 1 << 29 : 0)
                | (src[srcPos + 30] ? 1 << 30 : 0)
                | (src[srcPos + 31] ? 1 << 31 : 0)
                ;
            srcPos += 32;
            int high = (src[srcPos] ? 1 : 0)        // PROBLEM!
                | (src[srcPos + 1] ? 1 << 1 : 0)
                | (src[srcPos + 2] ? 1 << 2 : 0)
                | (src[srcPos + 3] ? 1 << 3 : 0)
                | (src[srcPos + 4] ? 1 << 4 : 0)
                | (src[srcPos + 5] ? 1 << 5 : 0)
                | (src[srcPos + 6] ? 1 << 6 : 0)
                | (src[srcPos + 7] ? 1 << 7 : 0)
                | (src[srcPos + 8] ? 1 << 8 : 0)
                | (src[srcPos + 9] ? 1 << 9 : 0)
                | (src[srcPos + 10] ? 1 << 10 : 0)
                | (src[srcPos + 11] ? 1 << 11 : 0)
                | (src[srcPos + 12] ? 1 << 12 : 0)
                | (src[srcPos + 13] ? 1 << 13 : 0)
                | (src[srcPos + 14] ? 1 << 14 : 0)
                | (src[srcPos + 15] ? 1 << 15 : 0)
                | (src[srcPos + 16] ? 1 << 16 : 0)
                | (src[srcPos + 17] ? 1 << 17 : 0)
                | (src[srcPos + 18] ? 1 << 18 : 0)
                | (src[srcPos + 19] ? 1 << 19 : 0)
                | (src[srcPos + 20] ? 1 << 20 : 0)
                | (src[srcPos + 21] ? 1 << 21 : 0)
                | (src[srcPos + 22] ? 1 << 22 : 0)
                | (src[srcPos + 23] ? 1 << 23 : 0)
                | (src[srcPos + 24] ? 1 << 24 : 0)
                | (src[srcPos + 25] ? 1 << 25 : 0)
                | (src[srcPos + 26] ? 1 << 26 : 0)
                | (src[srcPos + 27] ? 1 << 27 : 0)
                | (src[srcPos + 28] ? 1 << 28 : 0)
                | (src[srcPos + 29] ? 1 << 29 : 0)
                | (src[srcPos + 30] ? 1 << 30 : 0)
                | (src[srcPos + 31] ? 1 << 31 : 0)
                ;
            srcPos += 32;
            dest.put(k, ((long)low & 0xFFFFFFFFL) | (((long)high) << 32));
            destPos += 64;
        }
        int countFinish = count & 63;
        for (int srcPosMax = srcPos + countFinish; srcPos < srcPosMax; srcPos++, destPos++) {
            if (src[srcPos])
                dest.put((int)(destPos >>> 6), dest.get((int)(destPos >>> 6)) | 1L << (destPos & 63));
            else
                dest.put((int)(destPos >>> 6), dest.get((int)(destPos >>> 6)) & ~(1L << (destPos & 63)));
        }
    }
    public static void main(String[] args) {
        Random r = new Random();
        int entries = 1000;
        boolean[] src = new boolean[entries * 64];
        long[] dest = new long[entries];
        long[] result = new long[entries];

        for (int c = 0; c < 2000; c++) {
            for (int i = 0; i < entries; i++) {
                long l = r.nextLong();
                for (int bit = 0; bit < 64; bit++) {
                    src[i * 64 + bit] = (l & (1L << bit)) != 0;
                }
                dest[i] = 0;
                result[i] = l;
            }
            test(src, 0, LongBuffer.wrap(dest, 0, dest.length), 0, src.length);
            for (int i = 0; i < entries; i++) {
                if (dest[i] != result[i]) {
                    throw new InternalError(i + ": " + Long.toHexString(dest[i]) + " != " + Long.toHexString(result[i]));
                }
            }
        }
    }
}
