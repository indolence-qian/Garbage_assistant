package com.indolence.garbage_assistant.widget;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormState;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceAbility;
import ohos.bundle.ElementName;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Map;

public class FormAbility extends AceAbility {
    public static long formId = -1;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, FormAbility.class.getName());
    @Override
    public void onStart(Intent intent) {
        HiLog.info(LABEL_LOG,"嘿嘿嘿！");
        setInstanceName("default");
        setPageParams("pages/home/home", null);
        super.onStart(intent);
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {

        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, 0);
        String formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        int specificationId = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, 0);
        boolean tempFlag = intent.getBooleanParam(AbilitySlice.PARAM_FORM_TEMPORARY_KEY, false);
        HiLog.info(LABEL_LOG, "onCreateForm: " + formId + " " + formName + " " + specificationId);
        FormBindingData formBindingData = new FormBindingData("{\"temperature\": \"60°\"}");
        ProviderFormInfo formInfo = new ProviderFormInfo();
        formInfo.setJsBindingData(formBindingData);
        return formInfo;
    }
    @Override
    protected void onDeleteForm(long formId) {
        // 删除卡片实例数据
        super.onDeleteForm(formId);
    }

    @Override
    protected void onUpdateForm(long formId) {
        // 若卡片支持定时更新/定点更新/卡片使用方主动请求更新功能，则提供方需要覆写该方法以支持数据更新
        super.onUpdateForm(formId);
    }

    @Override
    protected void onTriggerFormEvent(long formId, String message) {
        // 若卡片支持触发事件，则需要覆写该方法并实现对事件的触发
        HiLog.info(LABEL_LOG,"果然是这里");
        HiLog.info(TAG, "handle card click event.");
        super.onTriggerFormEvent(formId, message);
    }

    @Override
    protected void onCastTempForm(long formId) {
        //使用方将临时卡片转换为常态卡片触发，提供方需要做相应的处理
        super.onCastTempForm (formId);
    }

    @Override
    protected void onEventNotify(Map<Long, Integer> formEvents) {
        //使用方发起可见或者不可见通知触发，提供方需要做相应的处理
        super.onEventNotify(formEvents);
    }

    @Override
    protected FormState onAcquireFormState(Intent intent) {
        ElementName elementName = intent.getElement();
        if (elementName == null) {
            HiLog.info(LABEL_LOG, "onAcquireFormState bundleName and abilityName are not set in intent");
            return FormState.UNKNOWN;
        }

        String bundleName = elementName.getBundleName();
        String abilityName = elementName.getAbilityName();

        String moduleName = intent.getStringParam(AbilitySlice.PARAM_MODULE_NAME_KEY);
        String formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        int specificationId = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, 0);
        if ("form_name2".equals(formName)) {
            return FormState.DEFAULT;
        }
        return FormState.READY;
    }
}
