import router from '@system.router';
import document from '@ohos.document';
import prompt from '@system.prompt';
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
    },
    onInit() {
      if(this.username=="未登录") this.username=this.$app.$def.data.username;
    },
    to_home() {
        router.push ({
            uri: 'pages/home/home',
        });
    },
    to_guide() {
        router.push ({
            uri: 'pages/guide/guide',
        });
    },
    change_password() {
        router.push ({
            uri: 'pages/change_password/change_password',
        });
    },
    change_photo()
    {
        this.plus();
    },
    plus: async function () {
        var actionData = {};
        actionData.firstNum = 1024;
        actionData.secondNum = 2048;
        console.info("yes,good!");
        var action = {};
        action.bundleName = 'com.indolence.garbage_assistant';
        action.abilityName = 'com.indolence.garbage_assistant.DataAbility';
        action.messageCode = ACTION_MESSAGE_CODE_PLUS;
        action.data = actionData;
        action.abilityType = ABILITY_TYPE_EXTERNAL;
        action.syncOption = ACTION_SYNC;
        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);
        if (ret.code == 0) {
            console.info('plus result is:' + JSON.stringify(ret.abilityResult));
        } else {
            console.error('plus error code:' + JSON.stringify(ret.code));
        }
    }
}
