import file from '@system.file';
import router from '@system.router';
import prompt from '@system.prompt';
import dataStorage from '@ohos.data.storage'
import featureAbility from '@ohos.ability.featureAbility'
import http from '@ohos.net.http';
export default {
    data: {
        account1: "",
        password1: "",
    },
    get_account(e) {
        this.account1 = e.value;
    },
    get_password(e) {
        this.password1 = e.value;
    },
    login() {
        router.push({
            uri: 'pages/login/login',
        });
    },
    find() {
        router.push({
            uri: 'pages/forget_cipher/forget_cipher'
        })
    },
    register() {
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
            "https://api2.bmob.cn/1/login?username=" + this.account1 + "&password=" + this.password1,
            {
                method: 'GET', // 可选，默认为“GET”
                // 开发者根据自身业务需要添加header字段
                header: {
                    'Content-Type': 'application/json',
                    'X-Bmob-Application-Id': '1f2dd70b89cd3c240cb1afaf361fe41b',
                    'X-Bmob-REST-API-Key': '9702909da94801b56e9d488842516bb3'
                },
                // 当使用POST请求时此字段用于传递内容
                readTimeout: 60000, // 可选，默认为60000ms
                connectTimeout: 60000 // 可选，默认为60000ms
            }, (err, data) => {
            if (!err) {
                // data.result为http响应内容，可根据业务需要进行解析
                //console.info('Result:' + data.result);
                var context = featureAbility.getContext()
                context.getFilesDir((err, path) => {
                    if (err) {
                        console.error('getFilesDir failed. err: ' + JSON.stringify(err));
                        return;
                    }
                    console.info('getFilesDir successful. path:' + JSON.stringify(path));
                    let storage = dataStorage.getStorageSync(path + '/User')
                    storage.putSync('UserInfo', data.result)
                    storage.flushSync()
                });


                var obj = JSON.parse(data.result);
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
                console.info('sessionToken:' + obj.sessionToken);
                console.info('code:' + data.responseCode);
                // data.header为http响应头，可根据业务需要进行解析
                console.info('header:' + data.header);
            } else {
                console.info('error:' + err.data);

            }
            if (data.responseCode == 200) {
                prompt.showToast({
                    message: '登录成功'
                });
                router.replace({
                    uri: 'pages/home/home',
                });
            }
            else {
                prompt.showToast({
                    message: '登录失败，请检查账号或密码'
                });
            }
        }
        );
    }
}
