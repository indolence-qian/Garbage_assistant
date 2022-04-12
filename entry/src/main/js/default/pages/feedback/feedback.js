import router from '@system.router';
import document from '@ohos.document';
import prompt from '@system.prompt';
import http from '@ohos.net.http';
export default {
    data: {
        text: ""
    },
    get_text(e) {
        this.text=e.value;
    },
    feedback() {
        // 每一个httpRequest对应一个http请求任务，不可复用
        let httpRequest = http.createHttp();
        // 用于订阅http响应头，此接口会比request请求先返回。可以根据业务需要订阅此消息
        httpRequest.on('headerReceive', (err, data) => {
            if (!err) {
                console.info('header: ' + data.header);
            } else {
                console.info('error:' + err.data);
            }
        });
        httpRequest.request(
        // 填写http请求的url地址，可以带参数也可以不带参数。URL地址需要开发者自定义。GET请求的参数可以在extraData中指定
            "https://api2.bmob.cn//1/classes/feedback/",
            {
                method: 'POST', // 可选，默认为“GET”
                // 开发者根据自身业务需要添加header字段
                header: {
                    'X-Bmob-Application-Id': '1f2dd70b89cd3c240cb1afaf361fe41b',
                    'X-Bmob-REST-API-Key': '9702909da94801b56e9d488842516bb3',
                    'Content-Type': 'application/json',
                },
                // 当使用POST请求时此字段用于传递内容
                extraData: {
                    connect : this.text,
                    username : this.$app.$def.data.username,
                },
                readTimeout: 60000, // 可选，默认为60000ms
                connectTimeout: 60000 // 可选，默认为60000ms
            },(err, data) => {
            if (!err) {
                // data.result为http响应内容，可根据业务需要进行解析
                console.info('Result:' + data.result);
                console.info('code:' + data.responseCode);
                // data.header为http响应头，可根据业务需要进行解析
                console.info('header:' + data.header);
                prompt.showToast({
                    message: "提交成功！"
                })
                router.back();
            } else {
                console.info('error:' + err.data);
            }
        }
        );
    }
}
