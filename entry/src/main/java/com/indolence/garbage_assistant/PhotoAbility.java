package com.indolence.garbage_assistant;

// ohos相关接口包
import ohos.ace.ability.AceInternalAbility;
import ohos.app.AbilityContext;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhotoAbility extends AceInternalAbility {
    private static final String BUNDLE_NAME = "com.example.hiaceservice";
    private static final String ABILITY_NAME = "com.example.hiaceservice.ComputeInternalAbility";
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;
    private static final int PLUS = 100;
    // 定义日志标签
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");

    private static PhotoAbility instance;
    private AbilityContext abilityContext;

    // 如果多个Ability实例都需要注册当前InternalAbility实例，需要更改构造函数，设定自己的bundleName和abilityName
    public PhotoAbility() {
        super(BUNDLE_NAME, ABILITY_NAME);
    }

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

                // 返回结果当前仅支持String，对于复杂结构可以序列化为ZSON字符串上报
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", SUCCESS);
                result.put("abilityResult", "hello world");
                // SYNC
                if (option.getFlags() == MessageOption.TF_SYNC) {
                    reply.writeString(ZSONObject.toZSONString(result));
                } else {
                    // ASYNC
                    MessageParcel responseData = MessageParcel.obtain();
                    responseData.writeString(ZSONObject.toZSONString(result));
                    IRemoteObject remoteReply = reply.readRemoteObject();
                    try {
                        remoteReply.sendRequest(0, responseData, MessageParcel.obtain(), new MessageOption());
                    } catch (RemoteException exception) {
                        return false;
                    } finally {
                        responseData.reclaim();
                    }
                }
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

    /**
     * Internal ability 注册接口。
     */
    public static void register(AbilityContext abilityContext) {
        instance = new PhotoAbility();
        instance.onRegister(abilityContext);
    }

    private void onRegister(AbilityContext abilityContext) {
        this.abilityContext = abilityContext;
        this.setInternalAbilityHandler((code, data, reply, option) -> {
            return this.onRemoteRequest(code, data, reply, option);
        });
    }

    /**
     * Internal ability 注销接口。
     */
    public static void unregister() {
        instance.onUnregister();
    }

    private void onUnregister() {
        abilityContext = null;
        this.setInternalAbilityHandler(null);
    }
}