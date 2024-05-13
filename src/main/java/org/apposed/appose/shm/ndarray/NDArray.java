package org.apposed.appose.shm.ndarray;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.apposed.appose.shm.Shm;
import org.apposed.appose.shm.ShmInterface;

public class NDArray implements Closeable {

	private final ShmInterface sharedMemory;
	private final DType dType;
	private final Shape shape;

	public NDArray(final ShmInterface sharedMemory, final DType dType, final Shape shape) {
		this.sharedMemory = sharedMemory;
		this.dType = dType;
		this.shape = shape;
	}

	public NDArray(final DType dType, final Shape shape) {
		this(
			new ShmInterface(null,true,safeInt(shape.numElements() * dType.bytesPerElement())),
			dType, shape);
	}

	public DType dType() {
		return dType;
	}

	public Shape shape() {
		return shape;
	}

	public ShmInterface shm() {
		return sharedMemory;
	}

	public ByteBuffer buffer() {
		final long length = shape.numElements() * dType.bytesPerElement();
		return sharedMemory.pointer().getByteBuffer(0, length);
	}

	@Override
	public void close() throws IOException {
		sharedMemory.close(); /* TODO reference counting or check for existence */
	}

	private static int safeInt(final long value) {
		if (value > Integer.MAX_VALUE) throw new IllegalArgumentException("value too large");
		return (int) value;
	}
}
