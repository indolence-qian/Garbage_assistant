package com.indolence.garbage_assistant;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.ace.ability.AceAbility;
import ohos.agp.window.service.WindowManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

public class CardRouter extends AceAbility {

    @Override
    public void onStart(Intent intent) {
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_ALLOW_EXTEND_LAYOUT);
        String url = "pages/get_camera/get_camera";
        setPageParams(url,null);
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
