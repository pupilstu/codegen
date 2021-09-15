package com.szw.codegen.core.receiver;

import com.szw.codegen.basic.Receiver;
import com.szw.codegen.basic.exception.InvalidConfigException;
import com.szw.codegen.core.model.common.TextFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author SZW
 * @date 2021/8/29
 */
@Slf4j
@RequiredArgsConstructor
public class TextFileReceiver implements Receiver<TextFile> {
	public static class TextFileReceiverConfig {
		/**
		 * output根目录，目标文件的根路径（如果目标文件的路径是相对路径）
		 */
		private String outputDir = Paths.get ("/").toString ();

		public String getOutputDir() {
			return outputDir;
		}

		public void setOutputDir(String directory) throws InvalidConfigException, IOException {
			Path path = Paths.get (directory);
			if (Files.notExists (path)) {
				Files.createDirectories (path);
			} else if (!Files.isDirectory (path)) {
				throw new InvalidConfigException ("output-dir", "it is not a directory");
			} else {
				outputDir = path.toString ();
			}
		}
	}

	private final TextFileReceiverConfig receiverConfig;

	@Override
	public Object getConfig() {
		return receiverConfig;
	}

	@Override
	public void receive(TextFile result) {
		Path path = Paths.get (result.getParentDir (), result.getFilename ());
		if (!path.isAbsolute ()) {
			path = Paths.get (receiverConfig.getOutputDir (), result.getParentDir (), result.getFilename ());
		}
		try {
			if (Files.notExists (path)) {
				Path parent = path.getParent ();
				if (Files.notExists (parent)) {
					Files.createDirectories (parent);
				}
				Files.createFile (path);
			}
			Files.write (path, result.getContent ().getBytes (StandardCharsets.UTF_8));
		} catch (IOException e) {
			log.error ("An error occur in create and write file '{}'.", path);
			e.printStackTrace ();
		}
	}
}
