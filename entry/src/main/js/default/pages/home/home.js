import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World'
    },
    onShow() {
        this.title=this.$app.$def.data.username;
        if(this.title.length==0) {
            router.push({
                uri: 'pages/index/index',
            });
        }
    },
    to_mine() {
        router.clear();
        router.push ({
            uri: 'pages/mine/mine',
        });
    }
}
