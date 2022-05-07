import pasteboard from '@ohos.pasteboard';
import router from '@system.router';
import featureAbility from '@ohos.ability.featureAbility';
import document from '@ohos.document';
import prompt from '@system.prompt';
import wantConstant from '@ohos.ability.wantConstant';
// abilityType: 0-Ability; 1-Internal Ability
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
// syncOption(Optional, default sync): 0-Sync; 1-Async
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;
const ACTION_MESSAGE_CODE_PLUS = 1001;
export default {
    data: {
        username: "未登录",
        avatar: "",
    },
    onShow() {
        this.avatar=this.$app.$def.data.photo;
        if(this.username=="未登录") this.username=this.$app.$def.data.username;
    },
    to_home() {
        router.replace ({
            uri: 'pages/home/home',
        });
    },
    to_guide() {
        router.replace ({
            uri: 'pages/guide/guide',
        });
    },
    change_password() {
        router.push ({
            uri: 'pages/change_password/change_password',
        });
    },
    feedback() {
      router.push({
          uri: 'pages/feedback/feedback',
      })
    },
    updata() {
        prompt.showToast({message:"已是最新版本！"});
    },
    change_photo()
    {
//        var str = {
//            "want": {
//                "action": wantConstant.Action.ACTION_CHOOSE,
//            },
//        };
//        featureAbility.startAbility(str, (err, data) => {
//            if (err) {
//                console.error('Operation failed. Cause:' + JSON.stringify(err));
//                return;
//            }
//            console.info('Operation successful. Data: ' + JSON.stringify(data))
//        });
        this.plus();
    },
    plus: async function () {
        var action = {};//添加数据action
        action.bundleName = 'com.indolence.garbage_assistant';
        action.abilityName = 'com.indolence.garbage_assistant.DataAbility';
        action.messageCode = ACTION_MESSAGE_CODE_PLUS;
        action.abilityType = ABILITY_TYPE_EXTERNAL;
        action.syncOption = ACTION_SYNC;
        var result = await FeatureAbility.callAbility(action);//异步调用java端文件并返回result
        var ret = JSON.parse(result);//解析json语句，获得结果
        if (ret.code == 0) {
            console.info('plus result is:' + JSON.stringify(ret.abilityResult));
            console.info('plus result is1:' +ret.abilityResult[0]);
            this.avatar=ret.abilityResult[0];
            if(ret.abilityResult.length>0) {
                this.$app.$def.data.album_array = ret.abilityResult;
                router.push({
                    uri: 'pages/album/album'
                });
            }
        } else {
            console.error('plus error code:' + JSON.stringify(ret.code));
        }
    },
    share() {
        var pasteData = pasteboard.createPlainTextData("https://github.com/indolence-qian/Garbage_assistant");//粘贴信息
        var systemPasteboard = pasteboard.getSystemPasteboard();//获取剪切板权限
        systemPasteboard.setPasteData(pasteData).then((data) => {//将输入写入并提示用户
            console.info('setPasteData success.');
            prompt.showToast({
                message:"内容已复制到剪切板，去浏览器粘贴浏览吧！",
            })
        }).catch((error) => {
            console.error('Failed to setPasteData. Cause: ' + error.message);
        });
    },
    about() {
        router.push ({
            uri: 'pages/about/about',
        });
    }
}
