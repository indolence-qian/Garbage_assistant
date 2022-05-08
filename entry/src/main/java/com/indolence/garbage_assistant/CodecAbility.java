/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.indolence.garbage_assistant;

import com.indolence.garbage_assistant.slice.CodecAbilitySlice;
import com.indolence.garbage_assistant.slice.MainAbilitySlice;
import com.indolence.garbage_assistant.utils.LogUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.window.service.WindowManager;
import ohos.security.SystemPermission;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static ohos.bundle.IBundleManager.PERMISSION_GRANTED;

/**
 * CodecAbility
 *
 * @since 2021-04-09
 */
public class CodecAbility extends Ability {
    private static final String TAG = CodecAbilitySlice.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    public void onStart(Intent intent) {
        WindowManager.getInstance().getTopWindow().get().setTransparent(true);
        requestCameraPermission();
        super.onStart(intent);
        super.setMainRoute(CodecAbilitySlice.class.getName());
    }

    private void requestCameraPermission() {
        List<String> permissions =
                new LinkedList<>(Arrays.asList(SystemPermission.CAMERA));
        permissions.removeIf(permission ->
                verifySelfPermission(permission) == PERMISSION_GRANTED || !canRequestPermission(permission));
        if (!permissions.isEmpty()) {
            requestPermissionsFromUser(permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }
        for (int grantResult : grantResults) {
            if (grantResult != PERMISSION_GRANTED) {
                LogUtil.info(TAG, grantResult + " is denied , Some functions may be affected.");
            }
        }
    }
}
