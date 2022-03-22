import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World'
    },
    onInit() {
        this.title=this.$app.$def.data.username;
        if(this.title.length==0) {
            router.push({
                uri: 'pages/index/index',
            });
        }
        console.info('hahahahaha'+this.title.length);
    },
    to_mine() {
        router.push ({
            uri: 'pages/mine/mine',
        });
    }
}
