import router from '@system.router';
export default {
    data: {
        title: 'World',
    },
    to_home() {
        router.push ({
            uri: 'pages/home/home',
        });
    },
    to_mine() {
        router.push ({
            uri: 'pages/mine/mine',
        });
    }
}
