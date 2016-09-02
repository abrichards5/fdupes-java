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

package fdups;

import org.slf4j.Logger;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.slf4j.LoggerFactory.getLogger;

class Md5SumCommandChecker {

    private static final Logger LOGGER = getLogger(Md5SumCommandChecker.class);

    Optional<String> getBinaryName() {
        Optional<String> binaryName;

        try {
            final Path path = newTempFile();

            if (checkCommand(path, "md5sum")) {
                binaryName = Optional.of("md5sum");
            } else if (checkCommand(path, "md5")) {
                binaryName = Optional.of("md5");
            } else {
                binaryName = Optional.empty();
            }
        } catch (final Throwable ignored1) {
            binaryName = Optional.empty();
        }

        if (binaryName.isPresent()) {
            LOGGER.info("Native MD5 command found at [{}]", binaryName.get());
        } else {
            LOGGER.warn("No native MD5 command found (slower JVM implementation will be used)");
        }

        return binaryName;
    }

    private Path newTempFile() throws IOException {
        final String uuid = randomUUID().toString();

        final Path path = Files.write(
            Files.createTempFile(Md5SumCommandChecker.class.getName(), uuid),
            uuid.getBytes(UTF_8)
        );
        path.toFile().deleteOnExit();

        return path;
    }

    private boolean checkCommand(final Path path, final String command) {
        try {
            new ProcessExecutor().command(command, path.toString())
                                 .exitValueNormal()
                                 .execute()
                                 .getExitValue();
            return true;
        } catch (final Exception ignored) {
            return false;
        }
    }

}