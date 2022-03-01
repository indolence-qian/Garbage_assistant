export default {
    data: {
        username: "",
        email: "",
        sessionToken: "",
        objectId: ""
    },
    onCreate() {
        console.info('AceApplication onCreate');
    },
    onDestroy() {
        console.info('AceApplication onDestroy');
    }
    
};
