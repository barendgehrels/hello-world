package org.barend.hwsqlite

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// Copies a stream (for example opened by from assets) to a file specified by a filename
fun copyStream(inputStream : InputStream, outputFile : File) {
    val outputStream = FileOutputStream(outputFile)

    val buffer_size = 1024
    val buffer = ByteArray(buffer_size)
    var length = inputStream.read(buffer)
    while (length > 0) {
        outputStream.write(buffer, 0, length)
        length = inputStream.read(buffer)
    }

    outputStream.flush()
    outputStream.close()
}
