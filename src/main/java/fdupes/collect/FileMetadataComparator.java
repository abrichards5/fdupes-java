/*
 * The MIT License (MIT)
 * Copyright (c) 2016 Christophe Bismuth
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fdupes.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import fdupes.immutable.FileMetadata;

import java.util.Comparator;

public class FileMetadataComparator implements Comparator<FileMetadata> {

    public static final FileMetadataComparator INSTANCE = new FileMetadataComparator();

    private FileMetadataComparator() {
        // NOT ALLOWED
    }

    @Override
    public int compare(final FileMetadata o1, final FileMetadata o2) {
        Preconditions.checkNotNull(o1, "null file metadata 1");
        Preconditions.checkNotNull(o2, "null file metadata 2");

        return ComparisonChain.start()
                              .compare(o1.getCreationTime(), o2.getCreationTime())
                              .compare(o1.getLastAccessTime(), o2.getLastAccessTime())
                              .compare(o1.getLastModifiedTime(), o2.getLastModifiedTime())
                              .compare(o1.getAbsolutePath(), o2.getAbsolutePath())
                              .result();
    }

}
