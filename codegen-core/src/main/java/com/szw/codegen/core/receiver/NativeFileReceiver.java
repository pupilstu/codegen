package com.szw.codegen.core.receiver;

import com.szw.codegen.core.Receiver;
import com.szw.codegen.core.entity.Code;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Data
@Slf4j
@NoArgsConstructor
public class NativeFileReceiver implements Receiver {
	private String outputDir;

	public NativeFileReceiver(String outputDir) {
		this.outputDir = outputDir;
	}

	@Override
	public void receive(Code result) {
		Path path = Paths.get (result.getParentDir (), result.getFilename ());
		if (!path.isAbsolute ()) {
			path = Paths.get (outputDir, result.getParentDir (), result.getFilename ());
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
			e.printStackTrace ();
		}
	}
}
