package com.indolence.garbage_assistant;

import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceAbility;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class CardRouter extends AceAbility {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, CardRouter.class.getName());

    @Override
    public void onStart(Intent intent) {
        HiLog.info(TAG,"嘿嘿嘿！");
        setInstanceName("default");
        setPageParams("pages/home/home", null);
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
