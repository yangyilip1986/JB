/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.louding.frame.http;

import java.io.File;
import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

/**
 * HttpClient请求的配置类<br>
 * 
 * <b>创建时间</b> 2014-6-5
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.2
 */
public class HttpConfig {
    public String cachePath = "KJLibrary/cache"; // 缓存文件夹

    public long cacheTime = 0; // 缓存时间0分钟,默认不反悔

    public int timeOut = 20000; // 超时设置，包括读超时、写超时、socket链接超时

    public int maxConnections = 10;// http请求最大并发连接数

    public int maxRetries = 5;// 错误尝试次数，错误异常表请在RetryHandler添加

    public int socketBuffer = 8192;// 8kb
    
    public boolean isJson = true;// 是否以json格式post

    public SSLSocketFactory sslSocketFactory = null;
    public HashMap<String, String> httpHeader = null;

    public File savePath;

    public HttpConfig() {
        httpHeader = new HashMap<String, String>();
        httpHeader.put("Content-Type","application/json;charset=utf-8");
        httpHeader.put("Charset", "UTF-8");// utf8其实是冗余的，因为在android中默认就是utf8
    }
}
