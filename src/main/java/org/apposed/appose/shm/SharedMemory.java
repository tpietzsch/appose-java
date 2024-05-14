package org.apposed.appose.shm;

import com.sun.jna.Pointer;

import java.io.IOException;

public final class SharedMemory {

    private final Impl impl;

    public SharedMemory(String name, boolean create, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("'size' must be a positive integer");
        }
        if (create && size == 0) {
            throw new IllegalArgumentException("'size' must be a positive number different from zero");
        }
        if (!create && name == null) {
            throw new IllegalArgumentException("'name' can only be null if create=true");
        }
        switch (ShmUtils.os) {
            case OSX:
                impl = new ShmMacOS(name, create, size);
                break;
            case LINUX:
                impl = new ShmLinux(name, create, size);
                break;
            case WINDOWS:
            default:
                throw new UnsupportedOperationException("not implemented for " + ShmUtils.os);
        }
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
        return impl.pointer();
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
        // TODO
        throw new UnsupportedOperationException();
//		impl.unlink();
    }

    @Override
    public String toString() {
        return "SharedMemory{impl=" + impl + '}';
    }

    interface Impl {
        String name();

        int size();

        Pointer pointer();

        void close() throws IOException;
    }


}

