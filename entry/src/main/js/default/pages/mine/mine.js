import router from '@system.router';
import document from '@ohos.document';
import prompt from '@system.prompt';
export default {
    data: {
        username: "NULL",
    },
    onInit() {
      this.username=this.$app.$def.data.username;
    },
    to_home() {
        router.push ({
            uri: 'pages/home/home',
        });
    },
    change_password() {
        router.push ({
            uri: 'pages/change_password/change_password',
        });
    },
    async change_photo() {
        var actionData = {};
        actionData.firstNum = 1024;
        actionData.secondNum = 2048;

        var action = {};
        action.bundleName = 'com.indolence.garbage_assistant';
        action.abilityName = 'com.indolence.garbage_assistant.DataAbility';
        action.messageCode = 100;
        action.data=actionData;
        action.abilityType = 0;
        action.syncOption = 1;
        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);console.info("hello hello");
        if (ret.code == 0) {
            console.info('plus result is:' + JSON.stringify(ret.abilityResult));
        } else {
            console.error('plus error code:' + JSON.stringify(ret.code));
        }
    },
}
