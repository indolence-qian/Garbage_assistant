import router from '@system.router';
export default {
    data: {
        title: ""
    },
    login() {
        router.push ({
            uri: 'pages/login/login',
        });
    },
    find() {
        router.push({
            uri: 'pages/forget_cipher/forget_cipher'
        })
    }

}
