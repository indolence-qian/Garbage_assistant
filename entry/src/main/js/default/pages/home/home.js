import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World',
        text: ""
    },
    to_mine() {
        router.clear();
        router.push ({
            uri: 'pages/mine/mine',
        });
    },
    to_guide() {
        router.push({
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
        router.push ({
            uri: 'pages/text_search/text_search',
            params:{
                query_text: this.text,
            }
        })
    }
}
