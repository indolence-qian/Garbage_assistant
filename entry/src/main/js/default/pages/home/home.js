import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World',
        text: "",
    },
    to_mine() {
        router.replace ({
            uri: 'pages/mine/mine',
        });
    },
    to_guide() {
        router.replace({
            uri: 'pages/guide/guide',
        });
    },
    take_photo() {
        router.push ({
            uri: 'pages/get_camera/get_camera',
        })
    },
    input(e) {
        this.text=e.value;
    },
    attention() {
      prompt.showToast({message:"正在开发中~~~~~"});

        this.jumpJavaAbility();
    },
    search(e) {
        router.push ({
            uri: 'pages/text_search/text_search',
            params:{
                query_text: this.text,
            }
        })
    },
    async jumpJavaAbility() {
        var action = {};
        action.bundleName = 'com.indolence.garbage_assistant';
        action.abilityName = 'com.indolence.garbage_assistant.CodecAbility';
        await FeatureAbility.startAbility(action);
    }
}

