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

package com.indolence.garbage_assistant.slice;


import com.indolence.garbage_assistant.JNITools;
import com.indolence.garbage_assistant.ResourceTable;
import com.indolence.garbage_assistant.camera.api.CameraListener;
import com.indolence.garbage_assistant.camera.constant.CameraType;
import com.indolence.garbage_assistant.camera.constant.CaptureMode;
import com.indolence.garbage_assistant.camera.view.CameraView;
import com.indolence.garbage_assistant.camera.view.CaptureButton;
import com.indolence.garbage_assistant.manager.CodecPlayer;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.agp.components.Switch;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.service.WindowManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Optional;

/**
 * CodecAbilitySlice
 *
 * @since 2021-04-09
 */
public class CodecAbilitySlice extends AbilitySlice {
    private CameraListener cameraController;
    private CodecPlayer codecPlayer;
    private CaptureButton captureButton;
    private Image cameraSwitchButton;
    private Switch mirrorSwitch;
    public static JNITools mTrackingMobile;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");
    @Override
    protected void onStart(Intent intent) {
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_ALLOW_EXTEND_LAYOUT);
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_codec);
        initView();
        initListener();
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_button_capture) instanceof CaptureButton) {
            captureButton = (CaptureButton) findComponentById(ResourceTable.Id_button_capture);
        }
        if (findComponentById(ResourceTable.Id_image_camera_switch) instanceof Image) {
            cameraSwitchButton = (Image) findComponentById(ResourceTable.Id_image_camera_switch);
        }
        if (findComponentById(ResourceTable.Id_mirror_switch) instanceof Switch) {
            mirrorSwitch = (Switch) findComponentById(ResourceTable.Id_mirror_switch);
        }
        if (findComponentById(ResourceTable.Id_cameraview) instanceof CameraView) {
            CameraView cameraView = (CameraView) findComponentById(ResourceTable.Id_cameraview);
            cameraController = cameraView.getController();
            cameraController.setMode(CaptureMode.PUSH_FLOW);
        }
        if (findComponentById(ResourceTable.Id_remote_player) instanceof SurfaceProvider) {
            SurfaceProvider remoteSurfaceView = (SurfaceProvider) findComponentById(ResourceTable.Id_remote_player);
            Optional<SurfaceOps> optional = remoteSurfaceView.getSurfaceOps();
            optional.ifPresent(surfaceOps -> surfaceOps.addCallback(
                    new SurfaceOps.Callback() {
                        @Override
                        public void surfaceCreated(SurfaceOps surfaceOps) {
                            codecPlayer = new CodecPlayer(surfaceOps.getSurface());
                            cameraController.setCameraListener(codecPlayer);
                        }

                        @Override
                        public void surfaceChanged(
                                SurfaceOps surfaceOps, int tag, int width, int height) {
                        }

                        @Override
                        public void surfaceDestroyed(SurfaceOps surfaceOps) {
                        }
                    }));
        }
    }

    private void initListener() {
        if (mirrorSwitch != null) {
            mirrorSwitch.setCheckedStateChangedListener((absButton, isMirror) ->
                    cameraController.setMirrorEffect(isMirror));
        }
        if (cameraSwitchButton != null) {
            cameraSwitchButton.setClickedListener(component -> {
                codecPlayer.stop();
                cameraController.switchCamera(cameraController.isFrontCamera() ? CameraType.BACK : CameraType.FRONT);
            });
        }
        if (captureButton != null) {
            captureButton.setClickedListener(component -> captureToggle());
        }
    }

    private void captureToggle() {
        if (cameraController.isCapturing()) {
            cameraController.stopCapture();
            codecPlayer.stop();
        } else {
            cameraController.capture();
        }
    }

    @Override
    protected void onActive() {
        mTrackingMobile = new JNITools(getAbility());

        String modelPath = "resources/rawfile/gab.ms";
        boolean ret = mTrackingMobile.loadModelFromBuf(modelPath);
        HiLog.debug(TAG, "Loading model return value: " + ret);
        super.onActive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
