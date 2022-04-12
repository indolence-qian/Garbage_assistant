import file from '@system.file';
import prompt from '@system.prompt';
import router from '@system.router';
export default {
    data: {
        title: 'World',
        album_array: [],
        uri: ""
    },
    onInit() {
        console.info('plus result is:' + JSON.stringify(this.$app.$def.data.album_array));
        for(var i=this.$app.$def.data.album_array.length-1;i>=0;i--) {
            this.album_array.push({
                img: this.$app.$def.data.album_array[i],
                choose: false
            });
        }
        console.info('plus result is:' + JSON.stringify(this.album_array));
    },
    select(e)
    {
        this.uri=e.value;
    },
    confirm()
    {
        console.log("call uri   "+this.uri);
        if(this.uri.length!=0){
            file.readArrayBuffer({
                uri: this.uri,
                success: function(data) {
                    console.log('call readArrayBuffer success: ' + data.buffer);
                },
                fail: function(data, code) {
                    console.error('call fail callback fail, code: ' + code + ', data: ' + data);
                },
            });
            this.$app.$def.data.photo=this.uri;
            router.clear();
            router.push({
                uri: 'pages/mine/mine',
                params: {
                    avatar : this.uri
                }
            })
        }
        else {
            prompt.showToast({
                message: "请选择头像"
            })
        }
    }
}
