package com.example.gochat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHtml {

    public static String GET_USER_INFORMATION="https://www.ddstudy.top/InfoOutput/";

    public static String touchHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            return "yes";
        }
        return "no";
    }
    public static String getHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            String html = new String(data, "UTF-8");
            return html;
        }
        return "1235";
    }
    public static String[] splitHtml(String html,String s1,String s2){
        String str=(html.split("<p id=\"weChatNumber\">")[1]).split("</p>")[0];
        String text_begin[]=str.split(s1);
        String text[]=new String[text_begin.length-1];
        for(int i=1;i<text_begin.length;i++){
            text[i-1]=text_begin[i].split(s2)[0];
        }
        return text;
    }
    public static class StreamTool {
        //从流中读取数据
        public static byte[] read(InputStream inStream) throws Exception{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024*1024];
            int len = 0;
            while((len = inStream.read(buffer)) != -1)
            {
                outStream.write(buffer,0,len);
            }
            inStream.close();
            return outStream.toByteArray();
        }
    }
}
