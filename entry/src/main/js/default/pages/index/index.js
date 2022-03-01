import prompt from '@system.prompt';
import router from '@system.router';

export default {
    data: {
        title: "",
        headTitle:"rrr",
        bb:"gggg"
    },
    login() {
        prompt.showToast({
            message: this.headTitle
        });
        //        router.push ({
//            uri: 'pages/login/login',
//        });
    },
    find() {
        router.push({
            uri: 'pages/forget_cipher/forget_cipher'
        })
    }

}
