package com.indolence.garbage_assistant;

// ohos相关接口包
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.MessageOption;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataAbility extends Ability {
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
                    String dataStr = data.readString();
                    RequestParam param = new RequestParam();
                    try {
                        param = ZSONObject.stringToClass(dataStr, RequestParam.class);
                    } catch (RuntimeException e) {
                        HiLog.error(LABEL, "convert failed.");
                    }
                    HiLog.info(LABEL,"hello!!!");
                    // 返回结果当前仅支持String，对于复杂结构可以序列化为ZSON字符串上报
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("code", SUCCESS);
                    result.put("abilityResult", param.getFirstNum() + param.getSecondNum());
                    reply.writeString(ZSONObject.toZSONString(result));
                    break;
                }
                default: {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("abilityError", ERROR);
                    reply.writeString(ZSONObject.toZSONString(result));
                    return false;
                }
            }
            HiLog.info(LABEL,"hahahhahahahha");
            return true;
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
}
//    private List<String> selectPic() {
//
//        List imgUrlList = new ArrayList();
//        String[] permission = {"ohos.permission.READ_USER_STORAGE"};
//        this.requestPermissionsFromUser(permission, 0);
//
//        DataAbilityHelper helper = DataAbilityHelper.creator(this.getContext());
//        try {
//            DataAbilityPredicates predicates = new DataAbilityPredicates();
//            // 设置查询过滤条件
//            // columns为null，查询记录所有字段，当前例子表示查询id字段
//            ResultSet result = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,  new String[]{AVStorage.Images.Media.ID}, null);
//            //HiLog.info("label","选择图片get :"+result);
//           // HiLog.info("label","选择图片get1 :"+ result.goToNextRow());
//
//            if (result == null) {
//                return imgUrlList;
//            }
//            while (result.goToNextRow()) {
//                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Images.Media.ID)); // 获取id字段的值
//                //HiLog.info("label","选择图片ID :"+ mediaId);
//                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,mediaId+ "");
//                imgUrlList.add(uri.toString());
//            }
//            result.close();
//        } catch (DataAbilityRemoteException e) {
//            // ...
//        }
//        return imgUrlList;
//
//    }
