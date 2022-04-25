import router from '@system.router';
import dataStorage from '@ohos.data.storage'
import featureAbility from '@ohos.ability.featureAbility'
export default {
    data: {
        title: ''
    },
    onShow() {
        sleep(1000);

        var context = featureAbility.getContext()
        context.getFilesDir((err, path) => {
            if (err) {
                console.error('getFilesDir failed. err: ' + JSON.stringify(err));
                return;
            }
            console.info('getFilesDir successful. path:' + JSON.stringify(path));
            let storage = dataStorage.getStorageSync(path + '/User')
            this.title = storage.getSync('UserInfo', '')
            console.info("The value of startup is " + this.title);
            if(this.title.length==0) {

                router.replace({
                    uri: 'pages/index/index',
                });
            }
            else {

                var obj = JSON.parse(this.title);
                this.$app.$def.data.username = obj.username;
                this.$app.$def.data.email = obj.email;
                this.$app.$def.data.sessionToken = obj.sessionToken;
                this.$app.$def.data.objectId = obj.objectId;
                try
                {
                    this.$app.$def.data.photo=obj.photo.url;
                }catch{
                    this.$app.$def.data.photo="common/images/more.png";
                }
                router.replace({
                    uri: 'pages/home/home',
                })
            }

        });
    },
}
function sleep(delay) {
    var start=(new Date()).getTime();
    while((new Date()).getTime()-start<delay) continue;
}
