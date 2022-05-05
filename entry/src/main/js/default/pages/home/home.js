import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World',
        text: ""
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
    search(e) {
        let reg = /^[\u4E00-\u9FA5]+$/;
        if(reg.test(e.value))
        {
            router.push ({
                uri: 'pages/text_search/text_search',
                params:{
                    query_text: this.text,
                }
            })
        }
        else {
            prompt.showToast({message:"非法字符！"});
        }
    }
}
