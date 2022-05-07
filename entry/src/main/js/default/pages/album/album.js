import file from '@system.file';
import prompt from '@system.prompt';
import router from '@system.router';
import dataStorage from '@ohos.data.storage'
import featureAbility from '@ohos.ability.featureAbility'
import http from '@ohos.net.http';
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
// syncOption(Optional, default sync): 0-Sync; 1-Async
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;
const ACTION_MESSAGE_CODE_HTTP = 10001;
export default {
    data: {
        title: 'World',
        album_array: [],
        uri: "",
        test: ''
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
        if(this.uri.length!=0){
            prompt.showToast({message:"正在上传数据......."});
            this.$app.$def.data.photo=this.uri;
            this.plus();
            var context = featureAbility.getContext()
            context.getFilesDir((err, path) => {
                if (err) {
                    console.error('getFilesDir failed. err: ' + JSON.stringify(err));
                    return;
                }
                console.info('getFilesDir successful. path:' + JSON.stringify(path));
                let storage = dataStorage.getStorageSync(path + '/User')
                this.test=storage.getSync('UserInfo', '');
                var obj = JSON.parse(this.test);
                obj.photo.url=this.uri;
                this.test = JSON.stringify(obj);
                storage.putSync('UserInfo', this.test);
                storage.flushSync()
            });
            prompt.showToast({message:"上传成功！"});
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
    },
    push(obj) {
        console.info(obj.url);
        var httpRequest = http.createHttp();
        httpRequest.on('headerReceive', (err, data) => {
            if (!err) {
                console.info('header: ' + data.header);
            } else {
                console.info('error:' + err.data);
            }
        });
        httpRequest.request(
        // 填写http请求的url地址，可以带参数也可以不带参数。URL地址需要开发者自定义。GET请求的参数可以在extraData中指定
            "https://api2.bmob.cn/1/users/"+this.$app.$def.data.objectId,
            {
                method: 'PUT', // 可选，默认为“GET”
                // 开发者根据自身业务需要添加header字段
                header: {
                    'Content-Type': 'application/json',
                    'X-Bmob-Application-Id': '1f2dd70b89cd3c240cb1afaf361fe41b',
                    'X-Bmob-REST-API-Key': '9702909da94801b56e9d488842516bb3',
                    "X-Bmob-Session-Token": this.$app.$def.data.sessionToken
                },
                // 当使用POST请求时此字段用于传递内容
                extraData: { "photo":{
                    "__type" : "File",
                    "group" : obj.cdn,
                    "filename" : obj.filename,
                    "url" : obj.url,
                } },
                readTimeout: 60000, // 可选，默认为60000ms
                connectTimeout: 60000 // 可选，默认为60000ms
            },(err, data) => {
            if (!err) {
                // data.result为http响应内容，可根据业务需要进行解析

                console.info('Result:' + data.result);
                console.info('code:' + data.responseCode);
                // data.header为http响应头，可根据业务需要进行解析
                console.info('header:' + data.header);
            } else {
                console.info('error:' + err.data);
            }
        }
        );
    },
    plus: async function () {
        var actionData = this.uri;
        var action = {};
        action.bundleName = 'com.indolence.garbage_assistant';
        action.abilityName = 'com.indolence.garbage_assistant.DataAbility';
        action.messageCode = ACTION_MESSAGE_CODE_HTTP;
        action.data=actionData;
        action.abilityType = ABILITY_TYPE_EXTERNAL;
        action.syncOption = ACTION_SYNC;
        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);
        console.info("hahaha a  " + result);
        this.push(ret);
    },
}
