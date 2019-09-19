package org.jd.shrink;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Main {

    /**
     * 参数： jarFile classList
     * 其中 classList文件名 来自 java -verbose:class -jar ... 执行后控制台打印出的类加载信息
     * classList文件 内容示例：
     * [Loaded io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe from file:/E:/git/http-proxy/security-http-proxy/target/proxy.jar]
     * [Loaded io.netty.channel.ChannelConfig from file:/E:/git/http-proxy/security-http-proxy/target/proxy.jar]
     * [Loaded io.netty.channel.socket.SocketChannelConfig from file:/E:/git/http-proxy/security-http-proxy/target/proxy.jar]
     * [Loaded io.netty.channel.socket.nio.NioSocketChannel$NioSocketChannelUnsafe from file:/E:/git/http-proxy/security-http-proxy/target/proxy.jar]
     * [Loaded io.netty.channel.ChannelHandlerAdapter from file:/E:/git/http-proxy/security-http-proxy/target/proxy.jar]
     */
    public static void main(String[] a) throws Exception {

        JarFile jar = new JarFile(a[0]);
        try (
                FileReader classListReader = new FileReader(a[1]);
                JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(a[0] + ".shrunken.jar"))
        ) {
            HashSet<String> names = new HashSet<>();
            for (String line : IOUtils.readLines(classListReader)) {
                if (!line.endsWith("/proxy.jar]"))
                    continue;
                String[] split = StringUtils.split(line);
                String name = StringUtils.replace(split[1], ".", "/") + ".class";
                names.add(name);
            }
            Enumeration<JarEntry> entries = jar.entries();
            int i = 0;
            while (entries.hasMoreElements()) {
                i++;
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith("META-INF/") || name.indexOf('/') < 0 || names.contains(name)) {//META-INF/下的文件和根目录下的文件
                    JarEntry newEntry = new JarEntry(name);
                    jarOutputStream.putNextEntry(newEntry);
                    IOUtils.copy(jar.getInputStream(entry), jarOutputStream);
                    jarOutputStream.closeEntry();
                }
            }
            jarOutputStream.finish();
            jarOutputStream.flush();
            System.out.println("精简完毕，共" + i + "个类，保留" + names.size() + "个类");
        }


    }
}
