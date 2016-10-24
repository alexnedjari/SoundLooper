package volume;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PathGenerator {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		File sortie = new File("testVolume.sql");
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(sortie));
		// File path creation
		list(new File("F:/musique/MP3"), 0, stream);

		// tag creation
		createTag(0, -1, 1, stream);

		stream.close();
		System.out.println("créé : " + sortie.getAbsolutePath());
	}

	private static int createTag(int tagId, int tagIdParent, int level,
			BufferedOutputStream stream) throws IOException,
			InterruptedException {
		stream.write(new String(
				"INSERT INTO tag (id, name, id_parent) VALUES (" + tagId + ", "
						+ tagId + ", " + tagIdParent + ");\n").getBytes());
		if (level > 4) {
			return tagId;
		}
		for (int i = 0; i < 10; i++) {
			tagId = createTag(tagId + 1, tagId, level + 1, stream);
			Thread.sleep(1);
		}
		return tagId;
	}

	private static void list(File file, int currentId,
			BufferedOutputStream stream) throws IOException,
			InterruptedException {
		File[] listFiles = file.listFiles();
		for (File child : listFiles) {
			if (child.isDirectory()) {
				list(child, currentId, stream);
			} else {
				if (child.getName().endsWith("mp3")
						&& !child.getAbsolutePath().contains("'")) {
					Long time = new Date().getTime();
					stream.write(new String(
							"INSERT INTO song(id,file, lastuse, isfavorite) VALUES ("
									+ time.intValue() + ", '"
									+ child.getAbsolutePath()
									+ "', curdate(), '1');\n").getBytes());
					Thread.sleep(1);
				}
			}
			if (currentId > 20000) {
				return;
			}
		}
	}
}
