package org.apposed.appose;

import com.sun.jna.Platform;
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

	public static void main2(String[] args) throws IOException, InterruptedException {

		for (int j = 0; j < 10; ++j) {
			final DType dType = DType.FLOAT32;
			final NDArray ndArray = new NDArray(dType, new Shape(F_ORDER, 4, 3, 2));

			// fill with values 0..23 in flat iteration order
			final FloatBuffer buf = ndArray.buffer().asFloatBuffer();
			final long len = ndArray.shape().numElements();
			for (int i = 0; i < len; ++i) {
				buf.put(i, i);
			}

			final SharedMemory shm0 = ndArray.shm();
			System.out.println("shm0 = " + shm0);

			final String name = shm0.name();
			final long size = shm0.size();
			float v;

			Shm_Attempt attempt = new Shm_Attempt(name, false, size);
			System.out.println("attempt = " + attempt);
			v = attempt.getPointer().getFloat(5 * dType.bytesPerElement());
			System.out.println("v = " + v);
			Shm_Attempt attempt2 = new Shm_Attempt(name, false, size);
			System.out.println("attempt2 = " + attempt2);
			v = attempt2.getPointer().getFloat(5 * dType.bytesPerElement());
			System.out.println("v = " + v);
//			ndArray.close();


			attempt = new Shm_Attempt(null, true, size);
			System.out.println("attempt = " + attempt);
			v = attempt.getPointer().getFloat(5 * dType.bytesPerElement());
			System.out.println("v = " + v);
			attempt2 = new Shm_Attempt(attempt.name(), false, attempt.size());
			System.out.println("attempt2 = " + attempt2);
			v = attempt2.getPointer().getFloat(5 * dType.bytesPerElement());
			System.out.println("v = " + v);

		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		final DType dType = DType.FLOAT32;

		final NDArray ndArray = new NDArray(dType, new Shape(F_ORDER, 4, 3, 2));
		// fill with values 0..23 in flat iteration order
		final FloatBuffer buf = ndArray.buffer().asFloatBuffer();
		final long len = ndArray.shape().numElements();
		for (int i = 0; i < len; ++i) {
			buf.put(i, i);
		}

		final SharedMemory shm0 = ndArray.shm();
		System.out.println("shm0 = " + shm0);

		for (int i = 0; i < 10; ++i)
        {
            final int size = 4 * 3 * 2 * dType.bytesPerElement();
			float v;

			Shm_Attempt attempt = new Shm_Attempt(null, true, size);
            System.out.println("attempt = " + attempt);
            attempt.getPointer().setFloat(5 * dType.bytesPerElement(), 5);
			try {
				Shm_Attempt attempt2 = new Shm_Attempt(attempt.name(), false, attempt.size());
				System.out.println("attempt2 = " + attempt2);
				v = attempt2.getPointer().getFloat(5 * dType.bytesPerElement());
				System.out.println("v = " + v);
			} catch (Exception e)
			{
				e.printStackTrace(System.out);
			}
        }
    }

	private static final String PRINT_INPUT = "" + //
			"return  \"[\" + img.buffer().asFloatBuffer().get(5) + \"]\";\n";

}
