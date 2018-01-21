package util;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by enum on 2017/12/3.
 */
public class PicUtil {

    public static boolean savePic(String picUrl, String fileName, String filePath){
        //先判断图片是否已经存在
        File isEx = new File(filePath , fileName);
        if (isEx.exists()){
            System.out.println("-----" + filePath + File.separator + fileName + "   已存在，已忽略保存该文件！");
            return false;
        }
        if (StringUtils.isBlank(picUrl)){
            throw new RuntimeException("图片地址为空！");
        }
        byte[] picByte = getImageFromNetByUrl(picUrl);
        if(null != picByte && picByte.length > 0){
            System.out.println("读取到：" + picByte.length + " 字节");
            try {
                writeImageToDisk(picByte, fileName, filePath);
            } catch (Exception e) {
                throw new RuntimeException("保存图片到本地发出错误！", e);
            }
        }else{
            System.out.println("没有从 " + picUrl + "获得内容");
        }
        return true;
    }

    public static void writeImageToDisk(byte[] img, String fileName, String filePath) throws Exception{
        if (StringUtils.isBlank(filePath)){
            filePath = "F" + File.separator + System.currentTimeMillis();
        }
        File fileDir = new File(filePath);
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file = new File(filePath , fileName);
        FileOutputStream fops = new FileOutputStream(file);
        fops.write(img);
        fops.flush();
        fops.close();
    }
    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            System.out.println("连接图片地址发生错误!" + strUrl);
            return null;
        }
    }

    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
