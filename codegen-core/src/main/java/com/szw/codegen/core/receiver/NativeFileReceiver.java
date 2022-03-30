package com.szw.codegen.core.receiver;

import com.szw.codegen.core.Receiver;
import com.szw.codegen.core.model.ResultFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件接收器，将生成的代码文件保存到本地
 *
 * @author SZW
 * @date 2021/8/29
 */
@NoArgsConstructor
public class NativeFileReceiver implements Receiver<ResultFile> {
	@Getter
	@Setter
	private String outputDir;

	@Override
	public void accept(ResultFile resultFile) {
		Path path = Paths.get (resultFile.getTargetDir (), resultFile.getFilename ());
		if (!path.isAbsolute ()) {
			path = Paths.get (outputDir, resultFile.getTargetDir (), resultFile.getFilename ());
		}
		try {
			if (Files.notExists (path)) {
				Path parent = path.getParent ();
				if (Files.notExists (parent)) {
					Files.createDirectories (parent);
				}
				Files.createFile (path);
			}
			Files.write (path, resultFile.getContent ().getBytes (StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}
}
