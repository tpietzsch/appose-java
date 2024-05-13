package org.apposed.appose.shm;

import com.sun.jna.Pointer;
import org.apposed.appose.SharedMemory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class ShmInterface {

    private final SharedMemoryArrayMacOS impl;

    public ShmInterface(String name, boolean create, long size) throws FileAlreadyExistsException {
        impl = create ? new SharedMemoryArrayMacOS((int) size) : new SharedMemoryArrayMacOS("/" + name, (int) size);
    }

    /**
     * Unique name that identifies the shared memory block.
     *
     * @return The name of the shared memory.
     */
    public String name() {
        return impl.name();
    }

    /**
     * Size in bytes.
     *
     * @return The length in bytes of the shared memory.
     */
    public long size() {
        return impl.size();
    }

    /**
     * JNA pointer to the shared memory segment.
     *
     * @return the pointer to the shared memory segment
     */
    public Pointer pointer() {
        return impl.getPointer();
    }

    /**
     * Closes access to the shared memory from this instance but does
     * not destroy the shared memory block.
     */
    public void close() {
        try {
            impl.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Requests that the underlying shared memory block be destroyed.
     * In order to ensure proper cleanup of resources, unlink should be
     * called once (and only once) across all processes which have access
     * to the shared memory block.
     */
    public void unlink() {
        throw new UnsupportedOperationException();
//		impl.unlink();
    }

    @Override
    public String toString() {
        return "ShmInterface{" +
                "impl =" + impl +
                '}';
    }

}
