import router from '@system.router';
export default {
    data: {
        title: 'World',
    },
    onInit() {
        this.username=this.$app.$def.data.username;
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
