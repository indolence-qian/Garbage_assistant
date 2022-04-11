import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World'
    },
    to_mine() {
        router.clear();
        router.push ({
            uri: 'pages/mine/mine',
        });
    },
    take_photo() {
        router.push ({
            uri: 'pages/get_camera/get_camera',
        })
    }
}
