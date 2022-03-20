import router from '@system.router';

export default {
    data: {
        title: 'World'
    },
    to_mine() {
        router.push ({
            uri: 'pages/mine/mine',
        });
    }
}
