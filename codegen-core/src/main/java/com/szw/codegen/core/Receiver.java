package com.szw.codegen.core;

import java.util.function.Consumer;

/**
 * 接收器，接收生成的代码，进行下一步处理。例如写入本地文件、写入zip、传输进网络流、前端展示等等。
 *
 * @author SZW
 */
@FunctionalInterface
public interface Receiver<R> extends Consumer<R> {
}
