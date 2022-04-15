// @ts-nocheck
import md5 from 'blueimp-md5';
import http from '@ohos.net.http';
import prompt from '@system.prompt';
// abilityType: 0-Ability; 1-Internal Ability
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
// syncOption(Optional, default sync): 0-Sync; 1-Async
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;
var garbage2="有害垃圾";
var garbage3="湿垃圾";
var garbage4="干垃圾";
const ACTION_MESSAGE_CODE_BASE = 64;
export default {
    data: {
        uri: "",
        cate_name: "可回收物",
        ps: "参考样例",
        photo_uri: "common/images/garbage_1.png",
        confidence: 0,
    },
    onInit() {

        this.plus();
        /*
        this.plus();
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
        */
    },
    plus: async function () {
        console.info("hahahha  "+this.uri);
        var actionData = this.uri;
        var action = {};
        action.bundleName = 'com.indolence.garbage_assistant';
        action.abilityName = 'com.indolence.garbage_assistant.DataAbility';
        action.messageCode = ACTION_MESSAGE_CODE_BASE;
        action.data=actionData;
        action.abilityType = ABILITY_TYPE_EXTERNAL;
        action.syncOption = ACTION_SYNC;
        var result = await FeatureAbility.callAbility(action);
        console.info("yes!!!   "+result);
        var ret = JSON.parse(result);
        if (ret.code == 10000) {
            console.info('plus result is:' + JSON.stringify(ret.result));
            this.cate_name=ret.result.garbage_info[0].cate_name;
            this.ps=ret.result.garbage_info[0].ps;
            this.confidence=ret.result.garbage_info[0].confidence;
            console.info("the answer "+ this.cate_name+"    "+ this.ps+"   "+this.confidence);
            if(this.cate_name==garbage2){
                this.photo_uri="common/images/garbage_2.png";
                console.info('message:' + this.cate_name);
            }
            else if(this.cate_name==garbage3) {
                this.photo_uri="common/images/garbage_3.png";
                this.cate_name="厨余垃圾";
                console.info('message:' + this.cate_name);
            }
            else if(this.cate_name==garbage4) {
                this.photo_uri="common/images/garbage_4.png";
                this.cate_name="其他垃圾";
                console.info('message:' + this.cate_name);
            }
        } else {
            console.error('plus error code:' + JSON.stringify(ret.code));
        }
        console.info("end!!!");
    },
}
