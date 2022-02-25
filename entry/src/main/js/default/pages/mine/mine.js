import router from '@system.router';
export default {
    data: {

    },
    change_password() {
        router.push ({
            uri: 'pages/change_password/change_password',
        });
    }
}
