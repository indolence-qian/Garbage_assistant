// @ts-nocheck
import router from '@system.router';
import http from '@ohos.net.http';
import prompt from '@system.prompt';
export default {
    data: {
        title: "",
        old_password: "",
        new_password: "",
        new_password1: "",
    },
    get_oldPassword(e) {
        this.old_password=e.value;
    },
    get_newPassword(e) {
        this.new_password=e.value;
    },
    get_newPassword1(e) {
        this.new_password1=e.value;
    },
    login() {
        if(this.new_password==this.new_password1)
        {
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
                "https://api2.bmob.cn/1/updateUserPassword/"+this.$app.$def.data.objectId,
                {
                    method: 'PUT', // 可选，默认为“GET”
                    // 开发者根据自身业务需要添加header字段
                    header: {
                        'Content-Type': 'application/json',
                        'X-Bmob-Application-Id': '1f2dd70b89cd3c240cb1afaf361fe41b',
                        'X-Bmob-REST-API-Key': '9702909da94801b56e9d488842516bb3',
                        'X-Bmob-Session-Token': this.$app.$def.data.sessionToken
                    },
                    // 当使用POST请求时此字段用于传递内容
                    extraData: {"oldPassword": this.old_password,"newPassword": this.new_password },
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
                if(data.responseCode==200)
                {
                    prompt.showToast({ message: "修改成功" });
                    router.back();
                }
                else {
                    prompt.showToast({ message: "修改失败，请检查密码是否正确"});
                }
            }
            );
        }
        else{
            prompt.showToast({ message: "两次输入的新密码不一致，请重新输入" });
        }
    }
}
