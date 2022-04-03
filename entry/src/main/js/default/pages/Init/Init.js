import router from '@system.router';
export default {
    data: {
        title: 'World'
    },
    onShow() {
        sleep(1000);
        this.title=this.$app.$def.data.username;
        if(this.title.length==0) {
            router.push({
                uri: 'pages/index/index',
            });
        }
        else {
            router.push({
                uri: 'pages/home/home',
            })
        }
    },
}
function sleep(delay) {
    var start=(new Date()).getTime();
    while((new Date()).getTime()-start<delay) continue;
}
