export default {
    data: {
        title: 'World',
        flash: "off",
        direction: "back",
        uri: ""
    },
    flashlight() {
        if(this.flash=="off"){
            this.flash="torch";
        }
        else {
            this.flash="off";
        }
    },
    reverse() {
        if(this.direction=="back") {
            this.direction="front";
        }
        else {
            this.direction="back";
        }
    },
    shoot() {
        console.info("camera success!");
        this.$element('cameraapp').takePhoto({
            quality: "normal",
            success: function (data) {
                console.info("success :  "+data);
            },
            fail: function (data) {
                console.info("fail : "+data);
            }
        })
    }
}
