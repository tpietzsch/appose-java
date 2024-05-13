package org.apposed.appose;

import org.apposed.appose.shm.SharedMemory;
import org.apposed.appose.shm.Shm_Attempt;
import org.apposed.appose.shm.ndarray.DType;
import org.apposed.appose.shm.ndarray.NDArray;
import org.apposed.appose.shm.ndarray.Shape;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.apposed.appose.shm.ndarray.Shape.Order.F_ORDER;

public class PlaygroundShm {

	public static void main(String[] args) throws IOException, InterruptedException {

		final DType dType = DType.FLOAT32;
		final NDArray ndArray = new NDArray(dType, new Shape(F_ORDER, 4, 3, 2));

		// fill with values 0..23 in flat iteration order
		final FloatBuffer buf = ndArray.buffer().asFloatBuffer();
		final long len = ndArray.shape().numElements();
		for ( int i = 0; i < len; ++i ) {
			buf.put(i, i);
		}


		final SharedMemory shm0 = ndArray.shm();

		final String name = shm0.name();
		final long size = shm0.size();
		System.out.println("shm0 name = " + name);
		System.out.println("shm0 size = " + size);

		Shm_Attempt attempt = new Shm_Attempt(name, false, size);
		System.out.println("attempt = " + attempt);

		ndArray.close();
	}

	private static final String PRINT_INPUT = "" + //
			"return  \"[\" + img.buffer().asFloatBuffer().get(5) + \"]\";\n";

}
