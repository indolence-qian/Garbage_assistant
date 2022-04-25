import router from '@system.router';
export default {
    data: {
        title: 'World',
    },
    to_home() {
        router.replace ({
            uri: 'pages/home/home',
        });
    },
    to_mine() {
        router.replace ({
            uri: 'pages/mine/mine',
        });
    }
}
