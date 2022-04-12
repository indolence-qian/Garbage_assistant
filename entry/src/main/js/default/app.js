export default {
    data: {
        username: "",
        email: "",
        sessionToken: "",
        objectId: "",
        album_array:[],
        caidan: 0,
        photo: "common/images/more.png",

    },
    onCreate() {
        console.info('AceApplication onCreate');
    },
    onDestroy() {
        console.info('AceApplication onDestroy');
    }
    
};
