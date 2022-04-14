// @ts-nocheck
import md5 from 'blueimp-md5';
import http from '@ohos.net.http';
import prompt from '@system.prompt';
export default {
    data: {
        uri: "",
        cate_name: "可回收物",
        ps: "参考样例",
        confidence: "1",
    },
    onInit() {
        console.info("Base64:  "+this.uri);
        const Base=require('js-base64');
        var imageBuf=Base.encodeURI(this.uri);
        console.info("Base64:  "+imageBuf);
        var imgs = imageBuf.replace(/^data:image\/\w+;base64,/, "");//去掉base64位头部
        console.info("Base64:  "+imgs);
        var time=new Date();
        var hashStr=new String;
        hashStr=md5("0cfe3897c87c64ffe3c3de05f44ad145"+time.getTime());
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
            "https://aiapi.jd.com/jdai/garbageImageSearch?appkey=4019e6bfb68a7c0a52e83d10e68e16a2&timestamp="+time.getTime()+"&sign="+hashStr,
            {
                method: 'POST', // 可选，默认为“GET”
                // 开发者根据自身业务需要添加header字段
                header: {
                    'Content-Type': 'application/json'
                },
                // 当使用POST请求时此字段用于传递内容
                extraData: {
                    imgBase64: imgs,
                    cityId: "310000"
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
                var obj = JSON.parse(data.result);
                this.cate_name=obj.result.garbage_info[0].cate_name;
                this.ps=obj.result.garbage_info[0].ps;
                this.confidence=obj.result.garbage_info[0].confidence;
                console.info("the answer "+ this.cate_name+"    "+ this.ps+"   "+this.confidence);
            } else {
                console.info('error:' + err.data);
            }
        }
        );
    }
}
