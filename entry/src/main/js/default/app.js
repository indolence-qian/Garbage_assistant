export default {
    data: {
        username: "",
        email: "",
        sessionToken: "",
        objectId: "",
        album_array:[],
    },
    onCreate() {
        console.info('AceApplication onCreate');
    },
    onDestroy() {
        console.info('AceApplication onDestroy');
    }
    
};
