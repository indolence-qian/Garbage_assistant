package com.indolence.garbage_assistant;

// ohos相关接口包
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.ComponentProvider;
import ohos.agp.components.Image;
import ohos.app.Context;
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
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        MyRemote() {
            super("MyService_MyRemote");
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            switch (code) {
                case PLUS: {
                    //DataAbility.selectPic();
                    String[] permission = {"ohos.permission.READ_USER_STORAGE"};
                    MainAbility.mActivity.requestPermissionsFromUser(permission, 0);
                    Intent intent = new Intent();
                    Operation opt=new Intent.OperationBuilder().withAction("android.intent.action.GET_CONTENT").build();
                    intent.setOperation(opt);
                    intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                    intent.setType("image/*");
                    startAbilityForResult(intent, imgRequestCode);
                    break;
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

