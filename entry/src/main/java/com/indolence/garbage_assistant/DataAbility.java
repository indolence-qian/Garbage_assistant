package com.indolence.garbage_assistant;

// ohos相关接口包
import com.alibaba.fastjson.JSONObject;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.ComponentProvider;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVStorage;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.MessageOption;
import ohos.security.SystemPermission;
import ohos.utils.net.Uri;
import ohos.utils.system.SystemCapability;
import ohos.utils.zson.ZSONObject;

import java.io.*;
import com.alibaba.fastjson.JSON;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.*;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataAbility extends Ability {
    private static final int imgRequestCode = 101;
    Image photo ;
    // 定义日志标签
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");

    private MyRemote remote = new MyRemote();

    // FA在请求PA服务时会调用Ability.connectAbility连接PA，连接成功后，需要在onConnect返回一个remote对象，供FA向PA发送消息
    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }
    class MyRemote extends RemoteObject implements IRemoteBroker {
        private static final int SUCCESS = 0;
        private static final int ERROR = 1;
        private static final int PLUS = 1001;
        private static final int PUSH = 10001;
        private static final int BASE = 64;

        MyRemote() {
            super("MyService_MyRemote");
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            switch (code) {
                case PLUS: {
                    List<String> res = DataAbility.selectPic();
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", SUCCESS);
                    result.put("abilityResult", res);
                    reply.writeString(ZSONObject.toZSONString(result));
                    HiLog.info(LABEL,"hahahahahhahahah   "+reply);
                    // String[] permission = {"ohos.permission.READ_USER_STORAGE"};
                    //MainAbility.mActivity.requestPermissionsFromUser(permission, 0);
                  //  Intent intent = new Intent();
                   // Operation opt=new Intent.OperationBuilder().withAction("android.intent.action.GET_CONTENT").build();
                    //intent.setOperation(opt);
                   // intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                   // intent.setType("image/*");
                    //startAbilityForResult(intent,imgRequestCode);
                    //DataAbility.selectPic();
                    break;
                }
                case PUSH: {
                    String dataStr=data.readString();
                    requestDistributedPermission(MainAbility.mActivity.getContext());
                    DataAbilityHelper helper = DataAbilityHelper.creator(MainAbility.mActivity.getContext());
                    Uri uri = Uri.parse(dataStr);
                    HiLog.info(LABEL,"hahahahahhahahah   "+uri);
                    FileInputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(helper.openFile(uri, "r"));
                    } catch (FileNotFoundException | DataAbilityRemoteException e) {
                        e.printStackTrace();
                        //LogUtil.info("WRYCHH", "Exception " + e.getMessage());
                    }
                    String result = sendPUTJson(inputStream);
                    reply.writeString(result);
                }
                case BASE: {

                    String dataStr = data.readString();
                    HiLog.info(LABEL,"message: "+dataStr);
                    requestDistributedPermission(MainAbility.mActivity.getContext());
                    DataAbilityHelper helper = DataAbilityHelper.creator(MainAbility.mActivity.getContext());
                    Uri uri = Uri.parse(dataStr);
                    HiLog.info(LABEL,"hahahahahhahahah   "+uri);
                    FileInputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(helper.openFile(uri, "r"));
                    } catch (FileNotFoundException | DataAbilityRemoteException e) {
                        e.printStackTrace();
                        //LogUtil.info("WRYCHH", "Exception " + e.getMessage());
                    }

                    byte[] bytes = readInputStream(inputStream);
                    HiLog.info(LABEL,"bytes  =  "+bytes);
                    String picData = Base64.getEncoder().encodeToString(bytes);
//                    ZSONObject zsonObject = new ZSONObject();
//                    ZSONObject body = new ZSONObject();
//                    body.put("index", "0");
//                    body.put("pic", "data:image/png;base64," + picData);
//                    body.put("type", "11");
//                    body.toString();
//                    zsonObject.put(HttpUtil.BODY, body);
//                    zsonObject.put(HttpUtil.PATH_KEY, "api/pictures/base64");
//                    HttpUtil.httpPost(zsonObject);

                    Date time = new Date();
                    String hashStr=MD5Utils.stringToMD5("0cfe3897c87c64ffe3c3de05f44ad145"+time.getTime());
                    String result = sendPostJson("https://aiapi.jd.com/jdai/garbageImageSearch?appkey=4019e6bfb68a7c0a52e83d10e68e16a2&timestamp="+time.getTime()+"&sign="+hashStr,picData);


                    reply.writeString(result);
                    break;
//                    Base64Util base=new Base64Util();
//                    String dataStr = data.readString();
//                    HiLog.info(LABEL,"hahahahahhahahah   "+dataStr);
//                    String baseStr =base.imageUrlToBase64(dataStr);
//                    HiLog.info(LABEL,"hahahahahhahahah   "+baseStr);
//                    Map<String, Object> result = new HashMap<>();
//                    result.put("code", SUCCESS);
//                    result.put("baseStr", baseStr);
//                    reply.writeString(ZSONObject.toZSONString(result));
                }
                default: {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("abilityError", ERROR);
                    reply.writeString(ZSONObject.toZSONString(result));
                    return false;
                }
            }

            return true;
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
    public static List<String> selectPic() {

        List imgUrlList = new ArrayList();
        String[] permission = {"ohos.permission.READ_USER_STORAGE"};
        MainAbility.mActivity.requestPermissionsFromUser(permission, 0);

        DataAbilityHelper helper = DataAbilityHelper.creator(MainAbility.mActivity.getContext());
        try {
            DataAbilityPredicates predicates = new DataAbilityPredicates();
            // 设置查询过滤条件
            // columns为null，查询记录所有字段，当前例子表示查询id字段
            ResultSet result = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,  new String[]{AVStorage.Images.Media.ID}, null);
            HiLog.info(LABEL,"选择图片get :"+result);
            HiLog.info(LABEL,"选择图片get1 :"+ result.goToNextRow());

            if (result == null) {
                return imgUrlList;
            }
            while (result.goToNextRow()) {
                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Images.Media.ID)); // 获取id字段的值
                HiLog.info(LABEL,"选择图片ID :"+ mediaId);
                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,mediaId+ "");
                imgUrlList.add(uri.toString());
            }
            result.close();
        } catch (DataAbilityRemoteException e) {
            // ...
        }
        return imgUrlList;

    }
    //发送POST请求
    public static String sendPostJson( String target,String data) {
        HiLog.info(LABEL,"nono  "+target);
        String httpresult = null;
        try{
            //1.设置请求的网址
            URL url = new URL(target);
            //2.获取连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //3.设置请求方法
            conn.setRequestMethod("POST");
            //设置连接超时时间
            conn.setConnectTimeout(1000 * 60);
            //设置读取超时时间
            conn.setReadTimeout(1000 * 60);
            //设置请求头
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //允许写出
            conn.setDoOutput(true);
            //允许读入
            conn.setDoInput(true);
            conn.connect();

            try (PrintWriter writer = new PrintWriter(conn.getOutputStream())) {
                Map<String, String> foo = new HashMap<>();
                foo.put("imgBase64", data);
                foo.put("cityId", "310000");
                writer.write(JSONObject.toJSONString(foo));
                writer.flush();
            }

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inStream=conn.getInputStream();
                //将读入内容转换成字符串
                httpresult = new String(readInputStream(inStream), "UTF-8");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        HiLog.info(LABEL,"  no  "+ httpresult);
        return httpresult;
    }


    //发送PUT请求
    public static String sendPUTJson(FileInputStream inputStream) {
        Date time = new Date();
        byte[] bytes = readInputStream(inputStream);
        String httpresult = null;
        try{
            //1.设置请求的网址
            URL url = new URL( "https://api2.bmob.cn/2/files/"+time.getTime()+".jpg");
            //2.获取连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //3.设置请求方法
            conn.setRequestMethod("POST");
            //设置连接超时时间
            conn.setConnectTimeout(1000 * 60);
            //设置读取超时时间
            conn.setReadTimeout(1000 * 60);
            //设置请求头
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("X-Bmob-Application-Id","1f2dd70b89cd3c240cb1afaf361fe41b");
            conn.setRequestProperty("X-Bmob-REST-API-Key","9702909da94801b56e9d488842516bb3");
            //允许写出
            conn.setDoOutput(true);
            //允许读入
            conn.setDoInput(true);
            // 发送请求参数
            DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
            dos.write(bytes);

            // flush输出流的缓冲
            dos.flush();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inStream=conn.getInputStream();
                //将读入内容转换成字符串
                httpresult = new String(readInputStream(inStream), "UTF-8");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        HiLog.info(LABEL,"  no  "+ httpresult);
        return httpresult;
    }




    //读取输入流
    private static byte[] readInputStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            inputStream.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 向用户申请相关权限的授权
    public static boolean requestDistributedPermission(Context context) {
        String[] permissions = {
                SystemPermission.READ_MEDIA,
        };
        List<String> permissionList = Arrays.stream(permissions)
                .filter(permission -> context.verifySelfPermission(permission) != IBundleManager.PERMISSION_GRANTED)
                .collect(Collectors.toList());
        if (permissionList.isEmpty()) {
            return true;
        }
        // 向用户申请相关权限的授权
        context.requestPermissionsFromUser(permissionList.toArray(new String[permissionList.size()]),
                0);
        return false;
    }


    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }


    /*选择图片回调*/
    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        if(requestCode==imgRequestCode && resultData!=null)
        {
            //选择的Img对应的Uri
            String chooseImgUri=resultData.getUriString();
            //定义数据能力帮助对象
            DataAbilityHelper helper=DataAbilityHelper.creator(getContext());
            //定义图片来源对象
            ImageSource imageSource = null;
            //获取选择的Img对应的Id
            String chooseImgId=null;
            //如果是选择文件则getUriString结果为dataability:///com.android.providers.media.documents/document/image%3A437，其中%3A437是":"的URL编码结果，后面的数字就是image对应的Id
            //如果选择的是图库则getUriString结果为dataability:///media/external/images/media/262，最后就是image对应的Id
            //这里需要判断是选择了文件还是图库
            if(chooseImgUri.lastIndexOf("%3A")!=-1){
                chooseImgId = chooseImgUri.substring(chooseImgUri.lastIndexOf("%3A")+3);
            }
            else {
                chooseImgId = chooseImgUri.substring(chooseImgUri.lastIndexOf('/')+1);
            }
            //获取图片对应的uri，由于获取到的前缀是content，我们替换成对应的dataability前缀
            Uri uri=Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,chooseImgId);
            try {
                //读取图片
                FileDescriptor fd = helper.openFile(uri, "r");
                imageSource = ImageSource.create(fd, null);
                //创建位图
                PixelMap pixelMap = imageSource.createPixelmap(null);
                //设置图片控件对应的位图
                photo.setPixelMap(pixelMap);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (imageSource != null) {
                    imageSource.release();
                }
            }
        }
    }

}


