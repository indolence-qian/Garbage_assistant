// @ts-nocheck
import router from '@system.router';
import http from '@ohos.net.http';
export default {
    data: {
        title: "",
        email1: "",
        account1: "",
        password1: ""
    },
    get_email(e) {
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;;
        if(!reg.test(e.value))
        {
            this.$element("id_1").showError("邮箱格式不正确，请重新输入");
            this.$element("id_1").focus();
            console.info("1234567890");
        }
        else
        {
            this.email1=e.value;
            console.info("0987654321");
        }
    },
    get_account(e) {
        this.account1=e.value;
    },
    get_password(e) {
        this.password1=e.value;
    },
    login() {
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
            "https://api2.bmob.cn/1/users",
            {
                method: 'POST', // 可选，默认为“GET”
                // 开发者根据自身业务需要添加header字段
                header: {
                    'Content-Type': 'application/json',
                    'X-Bmob-Application-Id': '1f2dd70b89cd3c240cb1afaf361fe41b',
                    'X-Bmob-REST-API-Key': '9702909da94801b56e9d488842516bb3'
                },
                // 当使用POST请求时此字段用于传递内容
                extraData: { "username": this.account1, "password": this.password1, "email": this.email1},
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
        router.back();
    }
}
