package uk.minersonline.minestom.lobby;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Serialisation {
	public static byte[] serialise(int id) {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		try(DataOutputStream out = new DataOutputStream(array)) {
			out.write(0);
			out.writeInt(id);
		} catch (IOException e) {
			throw new RuntimeException("unexpected error", e);
		}
		return array.toByteArray();
	}
}
